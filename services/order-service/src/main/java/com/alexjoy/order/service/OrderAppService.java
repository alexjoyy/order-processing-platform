package com.alexjoy.order.service;

import com.alexjoy.order.client.InventoryClient;
import com.alexjoy.order.client.PaymentClient;
import com.alexjoy.order.dto.CreateOrderRequest;
import com.alexjoy.order.exception.ResourceNotFoundException;
import com.alexjoy.order.model.Order;
import com.alexjoy.order.model.OrderStatus;
import com.alexjoy.order.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpStatusCodeException;

@Service
public class OrderAppService {

  private static final Logger log = LoggerFactory.getLogger(OrderAppService.class);

  private final OrderRepository orderRepository;
  private final InventoryClient inventoryClient;
  private final PaymentClient paymentClient;

  public OrderAppService(
      OrderRepository orderRepository,
      InventoryClient inventoryClient,
      PaymentClient paymentClient
  ) {
    this.orderRepository = orderRepository;
    this.inventoryClient = inventoryClient;
    this.paymentClient = paymentClient;
  }

  @Transactional
  public Order createOrder(CreateOrderRequest request) {
    Order order = new Order();
    order.setProductCode(request.productCode());
    order.setQuantity(request.quantity());
    order.setAmount(request.amount());
    order.setStatus(OrderStatus.CREATED);
    order = orderRepository.save(order);

    try {
      order.setStatus(OrderStatus.PROCESSING);
      orderRepository.save(order);

      boolean reserved = inventoryClient.reserve(order.getProductCode(), order.getQuantity());
      if (!reserved) {
        log.error("Inventory reservation returned non-2xx for orderId={}", order.getId());
        order.setStatus(OrderStatus.FAILED);
        return orderRepository.save(order);
      }

      boolean paid = paymentClient.capture(order.getId(), order.getAmount());
      if (!paid) {
        log.error("Payment capture returned non-2xx for orderId={}", order.getId());
      }
      order.setStatus(paid ? OrderStatus.COMPLETED : OrderStatus.FAILED);
      return orderRepository.save(order);
    } catch (HttpStatusCodeException ex) {
      log.error(
          "Downstream HTTP error while processing orderId={} status={} response={}",
          order.getId(),
          ex.getStatusCode(),
          ex.getResponseBodyAsString()
      );
      order.setStatus(OrderStatus.FAILED);
      return orderRepository.save(order);
    } catch (Exception ex) {
      log.error("Unexpected downstream error while processing orderId={}", order.getId(), ex);
      order.setStatus(OrderStatus.FAILED);
      return orderRepository.save(order);
    }
  }

  public Order getOrder(Long id) {
    return orderRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + id));
  }
}
