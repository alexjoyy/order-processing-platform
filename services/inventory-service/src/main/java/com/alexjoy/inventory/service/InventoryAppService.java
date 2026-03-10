package com.alexjoy.inventory.service;

import com.alexjoy.inventory.dto.ReserveInventoryRequest;
import com.alexjoy.inventory.model.InventoryItem;
import com.alexjoy.inventory.repository.InventoryItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryAppService {

  private final InventoryItemRepository inventoryItemRepository;

  public InventoryAppService(InventoryItemRepository inventoryItemRepository) {
    this.inventoryItemRepository = inventoryItemRepository;
  }

  @Transactional
  public void reserve(ReserveInventoryRequest request) {
    InventoryItem item = inventoryItemRepository.findByProductCode(request.productCode())
        .orElseThrow(() -> new IllegalArgumentException("Unknown product: " + request.productCode()));

    if (item.getAvailableQuantity() < request.quantity()) {
      throw new IllegalStateException("Insufficient inventory for product: " + request.productCode());
    }

    item.setAvailableQuantity(item.getAvailableQuantity() - request.quantity());
    inventoryItemRepository.save(item);
  }
}
