package com.alexjoy.inventory.security;

import com.alexjoy.inventory.exception.ApiError;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.OffsetDateTime;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JsonAuthEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper;

  public JsonAuthEntryPoint(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException
  ) throws IOException, ServletException {
    ApiError body = new ApiError(
        OffsetDateTime.now().toString(),
        HttpServletResponse.SC_UNAUTHORIZED,
        "Unauthorized",
        "Missing or invalid bearer token",
        request.getRequestURI()
    );

    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.getWriter().write(objectMapper.writeValueAsString(body));
  }
}
