package com.alexjoy.order.client;

import com.alexjoy.order.security.JwtService;
import java.math.BigDecimal;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PaymentClient {

  private final RestTemplate restTemplate;
  private final JwtService jwtService;
  private final String paymentBaseUrl;
  private final String serviceSubject;

  public PaymentClient(
      RestTemplate restTemplate,
      JwtService jwtService,
      @Value("${client.payment.base-url}") String paymentBaseUrl,
      @Value("${security.internal-client.subject}") String serviceSubject
  ) {
    this.restTemplate = restTemplate;
    this.jwtService = jwtService;
    this.paymentBaseUrl = paymentBaseUrl;
    this.serviceSubject = serviceSubject;
  }

  public boolean capture(Long orderId, BigDecimal amount) {
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(jwtService.generateToken(serviceSubject));

    ResponseEntity<Void> response = restTemplate.postForEntity(
        paymentBaseUrl + "/api/payments/capture",
        new HttpEntity<>(Map.of("orderId", orderId, "amount", amount), headers),
        Void.class
    );
    return response.getStatusCode().is2xxSuccessful();
  }
}
