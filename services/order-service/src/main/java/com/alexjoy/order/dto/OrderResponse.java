package com.alexjoy.order.dto;

import com.alexjoy.order.model.OrderStatus;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record OrderResponse(
    Long id,
    String productCode,
    Integer quantity,
    BigDecimal amount,
    OrderStatus status,
    OffsetDateTime createdAt
) {
}
