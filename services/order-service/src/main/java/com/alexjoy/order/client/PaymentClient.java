package com.alexjoy.order.client;

import java.math.BigDecimal;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PaymentClient {

  private final RestTemplate restTemplate;
  private final String paymentBaseUrl;

  public PaymentClient(
      RestTemplate restTemplate,
      @Value("${client.payment.base-url}") String paymentBaseUrl
  ) {
    this.restTemplate = restTemplate;
    this.paymentBaseUrl = paymentBaseUrl;
  }

  public boolean capture(Long orderId, BigDecimal amount) {
    ResponseEntity<Void> response = restTemplate.postForEntity(
        paymentBaseUrl + "/api/payments/capture",
        Map.of("orderId", orderId, "amount", amount),
        Void.class
    );
    return response.getStatusCode().is2xxSuccessful();
  }
}
