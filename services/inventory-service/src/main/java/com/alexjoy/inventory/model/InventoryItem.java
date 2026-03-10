package com.alexjoy.inventory.model;

import jakarta.persistence.*;

@Entity
@Table(name = "inventory_items")
public class InventoryItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String productCode;

  @Column(nullable = false)
  private Integer availableQuantity;

  public Long getId() {
    return id;
  }

  public String getProductCode() {
    return productCode;
  }

  public void setProductCode(String productCode) {
    this.productCode = productCode;
  }

  public Integer getAvailableQuantity() {
    return availableQuantity;
  }

  public void setAvailableQuantity(Integer availableQuantity) {
    this.availableQuantity = availableQuantity;
  }
}
