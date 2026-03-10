package com.alexjoy.order.controller;

import com.alexjoy.order.api.ApiResponse;
import com.alexjoy.order.dto.CreateOrderRequest;
import com.alexjoy.order.dto.OrderResponse;
import com.alexjoy.order.model.Order;
import com.alexjoy.order.service.OrderAppService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.OffsetDateTime;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

  private final OrderAppService orderAppService;

  public OrderController(OrderAppService orderAppService) {
    this.orderAppService = orderAppService;
  }

  @PostMapping
  public ResponseEntity<ApiResponse<OrderResponse>> create(
      @RequestBody @Valid CreateOrderRequest request,
      HttpServletRequest httpRequest
  ) {
    OrderResponse body = toResponse(orderAppService.createOrder(request));
    return ResponseEntity.status(HttpStatus.CREATED).body(
        new ApiResponse<>(
            OffsetDateTime.now().toString(),
            HttpStatus.CREATED.value(),
            "Order created",
            httpRequest.getRequestURI(),
            body
        )
    );
  }

  @GetMapping("/{id}")
  public ApiResponse<OrderResponse> getById(
      @PathVariable("id") Long id,
      HttpServletRequest httpRequest
  ) {
    OrderResponse body = toResponse(orderAppService.getOrder(id));
    return new ApiResponse<>(
        OffsetDateTime.now().toString(),
        HttpStatus.OK.value(),
        "Order fetched",
        httpRequest.getRequestURI(),
        body
    );
  }

  private OrderResponse toResponse(Order order) {
    return new OrderResponse(
        order.getId(),
        order.getProductCode(),
        order.getQuantity(),
        order.getAmount(),
        order.getStatus(),
        order.getCreatedAt()
    );
  }
}
