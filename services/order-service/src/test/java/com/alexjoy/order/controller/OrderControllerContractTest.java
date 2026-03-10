package com.alexjoy.order.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.alexjoy.order.model.Order;
import com.alexjoy.order.model.OrderStatus;
import com.alexjoy.order.service.OrderAppService;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(com.alexjoy.order.exception.GlobalExceptionHandler.class)
class OrderControllerContractTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private OrderAppService orderAppService;

  @Test
  void createReturnsSuccessEnvelope() throws Exception {
    Order order = new Order();
    order.setProductCode("JAVA-BOOK");
    order.setQuantity(2);
    order.setAmount(new BigDecimal("499.00"));
    order.setStatus(OrderStatus.COMPLETED);
    when(orderAppService.createOrder(any())).thenReturn(order);

    mockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"productCode\":\"JAVA-BOOK\",\"quantity\":2,\"amount\":499.00}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.status").value(201))
        .andExpect(jsonPath("$.message").value("Order created"))
        .andExpect(jsonPath("$.path").value("/api/orders"))
        .andExpect(jsonPath("$.data.productCode").value("JAVA-BOOK"))
        .andExpect(jsonPath("$.data.quantity").value(2));
  }

  @Test
  void getMissingOrderReturnsErrorEnvelope() throws Exception {
    when(orderAppService.getOrder(99999L))
        .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found: 99999"));

    mockMvc.perform(get("/api/orders/99999"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.error").value("Not Found"))
        .andExpect(jsonPath("$.message").value("Order not found: 99999"))
        .andExpect(jsonPath("$.path").value("/api/orders/99999"));
  }
}
