package com.alexjoy.payment.controller;

import com.alexjoy.payment.api.ApiResponse;
import com.alexjoy.payment.dto.CapturePaymentRequest;
import com.alexjoy.payment.dto.PaymentResponse;
import com.alexjoy.payment.model.Payment;
import com.alexjoy.payment.service.PaymentAppService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.time.OffsetDateTime;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

  private final PaymentAppService paymentAppService;

  public PaymentController(PaymentAppService paymentAppService) {
    this.paymentAppService = paymentAppService;
  }

  @PostMapping("/capture")
  public ApiResponse<PaymentResponse> capture(
      @RequestBody @Valid CapturePaymentRequest request,
      HttpServletRequest httpRequest
  ) {
    Payment payment = paymentAppService.capture(request);
    PaymentResponse body = new PaymentResponse(
        payment.getId(),
        payment.getOrderId(),
        payment.getAmount(),
        payment.getStatus(),
        payment.getCreatedAt()
    );
    return new ApiResponse<>(
        OffsetDateTime.now().toString(),
        HttpStatus.OK.value(),
        "Payment captured",
        httpRequest.getRequestURI(),
        body
    );
  }
}
