package com.alexjoy.payment.service;

import com.alexjoy.payment.dto.CapturePaymentRequest;
import com.alexjoy.payment.model.Payment;
import com.alexjoy.payment.model.PaymentStatus;
import com.alexjoy.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentAppService {

  private final PaymentRepository paymentRepository;

  public PaymentAppService(PaymentRepository paymentRepository) {
    this.paymentRepository = paymentRepository;
  }

  public Payment capture(CapturePaymentRequest request) {
    Payment payment = new Payment();
    payment.setOrderId(request.orderId());
    payment.setAmount(request.amount());
    payment.setStatus(PaymentStatus.CAPTURED);
    return paymentRepository.save(payment);
  }
}
