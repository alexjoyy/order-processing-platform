package com.alexjoy.payment.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CapturePaymentRequest(
    @NotNull Long orderId,
    @NotNull @DecimalMin("0.01") BigDecimal amount
) {
}
