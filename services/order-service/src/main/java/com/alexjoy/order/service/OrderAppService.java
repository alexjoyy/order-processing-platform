package com.alexjoy.order.service;

import com.alexjoy.order.client.InventoryClient;
import com.alexjoy.order.client.PaymentClient;
import com.alexjoy.order.dto.CreateOrderRequest;
import com.alexjoy.order.model.Order;
import com.alexjoy.order.model.OrderStatus;
import com.alexjoy.order.repository.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class OrderAppService {

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
        order.setStatus(OrderStatus.FAILED);
        return orderRepository.save(order);
      }

      boolean paid = paymentClient.capture(order.getId(), order.getAmount());
      order.setStatus(paid ? OrderStatus.COMPLETED : OrderStatus.FAILED);
      return orderRepository.save(order);
    } catch (Exception ex) {
      order.setStatus(OrderStatus.FAILED);
      return orderRepository.save(order);
    }
  }

  public Order getOrder(Long id) {
    return orderRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found: " + id));
  }
}
