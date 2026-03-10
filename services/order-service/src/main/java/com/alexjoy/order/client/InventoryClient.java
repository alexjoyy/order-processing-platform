package com.alexjoy.order.client;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class InventoryClient {

  private final RestTemplate restTemplate;
  private final String inventoryBaseUrl;

  public InventoryClient(
      RestTemplate restTemplate,
      @Value("${client.inventory.base-url}") String inventoryBaseUrl
  ) {
    this.restTemplate = restTemplate;
    this.inventoryBaseUrl = inventoryBaseUrl;
  }

  public boolean reserve(String productCode, Integer quantity) {
    ResponseEntity<Void> response = restTemplate.postForEntity(
        inventoryBaseUrl + "/api/inventory/reserve",
        Map.of("productCode", productCode, "quantity", quantity),
        Void.class
    );
    return response.getStatusCode().is2xxSuccessful();
  }
}
