package com.alexjoy.order.client;

import com.alexjoy.order.security.JwtService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class InventoryClient {

  private final RestTemplate restTemplate;
  private final JwtService jwtService;
  private final String inventoryBaseUrl;
  private final String serviceSubject;

  public InventoryClient(
      RestTemplate restTemplate,
      JwtService jwtService,
      @Value("${client.inventory.base-url}") String inventoryBaseUrl,
      @Value("${security.internal-client.subject}") String serviceSubject
  ) {
    this.restTemplate = restTemplate;
    this.jwtService = jwtService;
    this.inventoryBaseUrl = inventoryBaseUrl;
    this.serviceSubject = serviceSubject;
  }

  public boolean reserve(String productCode, Integer quantity) {
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(jwtService.generateToken(serviceSubject));

    ResponseEntity<Void> response = restTemplate.postForEntity(
        inventoryBaseUrl + "/api/inventory/reserve",
        new HttpEntity<>(Map.of("productCode", productCode, "quantity", quantity), headers),
        Void.class
    );
    return response.getStatusCode().is2xxSuccessful();
  }
}
