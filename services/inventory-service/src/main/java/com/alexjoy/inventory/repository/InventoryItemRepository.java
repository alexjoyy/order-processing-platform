package com.alexjoy.inventory.repository;

import com.alexjoy.inventory.model.InventoryItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
  Optional<InventoryItem> findByProductCode(String productCode);
}
