package com.alexjoy.inventory.controller;

import com.alexjoy.inventory.api.ApiResponse;
import com.alexjoy.inventory.dto.ReserveInventoryRequest;
import com.alexjoy.inventory.service.InventoryAppService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.time.OffsetDateTime;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

  private final InventoryAppService inventoryAppService;

  public InventoryController(InventoryAppService inventoryAppService) {
    this.inventoryAppService = inventoryAppService;
  }

  @PostMapping("/reserve")
  public ApiResponse<Void> reserve(
      @RequestBody @Valid ReserveInventoryRequest request,
      HttpServletRequest httpRequest
  ) {
    inventoryAppService.reserve(request);
    return new ApiResponse<>(
        OffsetDateTime.now().toString(),
        HttpStatus.OK.value(),
        "Inventory reserved",
        httpRequest.getRequestURI(),
        null
    );
  }
}
