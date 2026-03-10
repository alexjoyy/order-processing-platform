package com.alexjoy.inventory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReserveInventoryRequest(
    @NotBlank String productCode,
    @NotNull @Min(1) Integer quantity
) {
}
