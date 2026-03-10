package com.alexjoy.payment.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.alexjoy.payment.model.Payment;
import com.alexjoy.payment.model.PaymentStatus;
import com.alexjoy.payment.service.PaymentAppService;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PaymentController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(com.alexjoy.payment.exception.GlobalExceptionHandler.class)
class PaymentControllerContractTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private PaymentAppService paymentAppService;

  @Test
  void captureReturnsSuccessEnvelope() throws Exception {
    Payment payment = new Payment();
    payment.setOrderId(101L);
    payment.setAmount(new BigDecimal("999.00"));
    payment.setStatus(PaymentStatus.CAPTURED);
    when(paymentAppService.capture(any())).thenReturn(payment);

    mockMvc.perform(post("/api/payments/capture")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"orderId\":101,\"amount\":999.00}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(200))
        .andExpect(jsonPath("$.message").value("Payment captured"))
        .andExpect(jsonPath("$.path").value("/api/payments/capture"))
        .andExpect(jsonPath("$.data.orderId").value(101))
        .andExpect(jsonPath("$.data.status").value("CAPTURED"));
  }
}
