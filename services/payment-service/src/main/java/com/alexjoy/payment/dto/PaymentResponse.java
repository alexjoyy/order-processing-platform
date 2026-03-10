package com.alexjoy.payment.dto;

import com.alexjoy.payment.model.PaymentStatus;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record PaymentResponse(
    Long id,
    Long orderId,
    BigDecimal amount,
    PaymentStatus status,
    OffsetDateTime createdAt
) {
}
