package com.alexjoy.order.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CreateOrderRequest(
    @NotBlank String productCode,
    @NotNull @Min(1) Integer quantity,
    @NotNull @DecimalMin("0.01") BigDecimal amount
) {
}
