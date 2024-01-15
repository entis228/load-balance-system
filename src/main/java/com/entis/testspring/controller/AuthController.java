package com.entis.testspring.controller;

import com.entis.testspring.entity.dto.AccessTokenResponse;
import com.entis.testspring.entity.dto.RefreshTokenRequest;
import com.entis.testspring.entity.dto.SignInRequest;
import com.entis.testspring.exception.InvalidRefreshTokenException;
import com.entis.testspring.exception.TokenHttpExceptions;
import com.entis.testspring.service.JWTAuthService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.entis.testspring.entity.db.auth.AuthUserDetails;

@RestController
@RequestMapping(Routes.TOKEN)
public class AuthController {

  private final JWTAuthService authOperations;

  public AuthController(JWTAuthService authOperations) {
    this.authOperations = authOperations;
  }

  /*
   * JWTAuthenticationFilter sets the principle (user-details from UserService) using auth manager
   */
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @io.swagger.v3.oas.annotations.parameters.RequestBody(
      content = @Content(schema = @Schema(implementation = SignInRequest.class)))
  public AccessTokenResponse login(@AuthenticationPrincipal AuthUserDetails userDetails) {
    return authOperations.getToken(userDetails);
  }

  @PostMapping(
      value = "/refresh",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public AccessTokenResponse refresh(@RequestBody RefreshTokenRequest request) {
    try {
      return authOperations.refreshToken(request.refreshToken());
    } catch (InvalidRefreshTokenException e) {
      throw TokenHttpExceptions.invalidRefreshToken(e);
    }
  }

  @PostMapping(value = "/invalidate", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void invalidate(@RequestBody RefreshTokenRequest request, @AuthenticationPrincipal String username) {
    try {
      authOperations.invalidateToken(request.refreshToken(), username);
    } catch (InvalidRefreshTokenException e) {
      throw TokenHttpExceptions.invalidRefreshToken(e);
    }
  }

}
