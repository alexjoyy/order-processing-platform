package com.alexjoy.inventory.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.alexjoy.inventory.service.InventoryAppService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(InventoryController.class)
@Import(com.alexjoy.inventory.exception.GlobalExceptionHandler.class)
class InventoryControllerContractTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private InventoryAppService inventoryAppService;

  @Test
  void reserveReturnsSuccessEnvelope() throws Exception {
    doNothing().when(inventoryAppService).reserve(any());

    mockMvc.perform(post("/api/inventory/reserve")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"productCode\":\"JAVA-BOOK\",\"quantity\":2}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(200))
        .andExpect(jsonPath("$.message").value("Inventory reserved"))
        .andExpect(jsonPath("$.path").value("/api/inventory/reserve"))
        .andExpect(jsonPath("$.data").value(Matchers.nullValue()));
  }

  @Test
  void unknownProductReturnsErrorEnvelope() throws Exception {
    doThrow(new IllegalArgumentException("Unknown product: BAD-CODE"))
        .when(inventoryAppService).reserve(any());

    mockMvc.perform(post("/api/inventory/reserve")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"productCode\":\"BAD-CODE\",\"quantity\":1}"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.error").value("Not Found"))
        .andExpect(jsonPath("$.message").value("Unknown product: BAD-CODE"))
        .andExpect(jsonPath("$.path").value("/api/inventory/reserve"));
  }
}
