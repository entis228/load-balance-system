package com.entis.testspring.config.security.filters;

import com.entis.testspring.entity.dto.SignInRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UncheckedIOException;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final ObjectMapper objectMapper;

  public JWTAuthenticationFilter(
      AuthenticationManager authenticationManager,
      ObjectMapper objectMapper
  ) {
    setAuthenticationManager(authenticationManager);
    setUsernameParameter("login");
    this.objectMapper = objectMapper;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest req,
      HttpServletResponse res) throws AuthenticationException {
    SignInRequest credentials;
    try {
      credentials = objectMapper.readValue(req.getInputStream(), SignInRequest.class);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
    var authToken = new UsernamePasswordAuthenticationToken(
        credentials.username(),
        credentials.password()
    );
    return getAuthenticationManager().authenticate(authToken);
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest req,
      HttpServletResponse res,
      FilterChain chain,
      Authentication auth) throws IOException, ServletException {

    SecurityContextHolder.getContext().setAuthentication(auth);

    chain.doFilter(req, res);
  }

}
