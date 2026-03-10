package com.alexjoy.inventory.auth;

import com.alexjoy.inventory.api.ApiResponse;
import com.alexjoy.inventory.auth.dto.AuthRequest;
import com.alexjoy.inventory.auth.dto.AuthResponse;
import com.alexjoy.inventory.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.time.OffsetDateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final JwtService jwtService;
  private final String configuredUsername;
  private final String configuredPassword;

  public AuthController(
      JwtService jwtService,
      @Value("${security.demo.username}") String configuredUsername,
      @Value("${security.demo.password}") String configuredPassword
  ) {
    this.jwtService = jwtService;
    this.configuredUsername = configuredUsername;
    this.configuredPassword = configuredPassword;
  }

  @PostMapping("/token")
  public ApiResponse<AuthResponse> token(
      @RequestBody @Valid AuthRequest request,
      HttpServletRequest httpRequest
  ) {
    if (!configuredUsername.equals(request.username()) || !configuredPassword.equals(request.password())) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
    }

    String token = jwtService.generateToken(request.username());
    return new ApiResponse<>(
        OffsetDateTime.now().toString(),
        HttpStatus.OK.value(),
        "Token issued",
        httpRequest.getRequestURI(),
        new AuthResponse(token)
    );
  }
}
