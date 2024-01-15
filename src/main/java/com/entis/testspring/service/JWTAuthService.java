package com.entis.testspring.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.entis.testspring.config.security.SecurityConstants;
import com.entis.testspring.config.security.properties.JWTProperties;
import com.entis.testspring.entity.db.User;
import com.entis.testspring.entity.db.auth.AuthUserDetails;
import com.entis.testspring.entity.db.auth.RefreshToken;
import com.entis.testspring.entity.dto.AccessTokenResponse;
import com.entis.testspring.exception.InvalidRefreshTokenException;
import com.entis.testspring.repository.RefreshTokenRepository;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class JWTAuthService {

  private final RefreshTokenRepository refreshTokenRepository;

  private final Duration jwtExpiration;

  private final Duration refreshExpiration;

  private final Algorithm algorithm;

  public JWTAuthService(JWTProperties securityProperties,
      RefreshTokenRepository refreshTokenRepository) {
    this.refreshTokenRepository = refreshTokenRepository;
    this.jwtExpiration = securityProperties.getAccessExpireIn();
    this.refreshExpiration = securityProperties.getRefreshExpireIn();
    this.algorithm = Algorithm.HMAC512(new String(securityProperties.getSecret()).getBytes());
  }

  @Transactional
  public AccessTokenResponse getToken(AuthUserDetails userDetails) {
    RefreshToken newToken = issueRefreshToken(userDetails.getSource());
    return response(userDetails.getUsername(), userDetails.getAuthorities(), newToken);
  }

  @Transactional
  public AccessTokenResponse refreshToken(String refreshToken)
      throws InvalidRefreshTokenException {

    RefreshToken storedToken = refreshTokenRepository.findIfValid(
        verifyRefreshToken(refreshToken),
        OffsetDateTime.now()
    ).orElseThrow(InvalidRefreshTokenException::new);

    checkIfRotated(storedToken);

    User user = storedToken.getUser();

    var nextToken = issueRefreshToken(user);

    refreshTokenRepository.updateChain(storedToken, nextToken);

    return response(user.getUsername(), List.of(user.getUserRole()), nextToken);
  }

  @Transactional
  public void invalidateToken(String refreshToken, String ownerEmail) throws InvalidRefreshTokenException {
    RefreshToken storedToken = refreshTokenRepository.findById(verifyRefreshToken(refreshToken))
        .orElseThrow(InvalidRefreshTokenException::new);
    checkTokenOwner(storedToken, ownerEmail);
    checkIfRotated(storedToken);
    refreshTokenRepository.deleteChain(storedToken);
  }

  private void checkTokenOwner(RefreshToken storedToken, String username) throws InvalidRefreshTokenException {
    User user = storedToken.getUser();
    if (!user.getUsername().equals(username)) {
      String message = "!! ATTENTION !! User {} engaged in a suspicious activity, " +
          "trying to use a refresh token issued to another user. " +
          "Blocking the suspicious actor's account pending investigation!";
      log.error(message, username);
      // invalidate token
      refreshTokenRepository.deleteChain(storedToken);
      throw new InvalidRefreshTokenException();
    }
  }

  private void checkIfRotated(RefreshToken storedToken) throws InvalidRefreshTokenException {
    if (storedToken.getNext() != null) {
      String message = "!! ATTENTION !! An old refresh token used for user {}, " +
          "signifying possible token theft! Invalidating the entire token chain.";
      log.error(message, storedToken.getUser().getUsername());
      refreshTokenRepository.deleteChain(storedToken.getNext());
      throw new InvalidRefreshTokenException();
    }
  }

  private RefreshToken issueRefreshToken(User user) {
    var refreshToken = new RefreshToken();
    var now = OffsetDateTime.now();
    refreshToken.setIssuedAt(now);
    refreshToken.setExpireAt(now.plus(refreshExpiration));
    refreshToken.setUser(user);
    return refreshTokenRepository.save(refreshToken);
  }

  private AccessTokenResponse response(String subject,
      Collection<? extends GrantedAuthority> authorities,
      RefreshToken refreshToken) {
    String accessToken = issueJWT(subject, authorities);
    return new AccessTokenResponse(
        accessToken,
        signRefreshToken(refreshToken),
        jwtExpiration.toSeconds()
    );
  }

  private UUID verifyRefreshToken(String refreshJWT) throws InvalidRefreshTokenException {
    try {
      String id = JWT.require(algorithm)
          .build()
          .verify(refreshJWT)
          .getId();
      Objects.requireNonNull(id, "jti must be present in refresh token");
      return UUID.fromString(id);
    } catch (Exception e) {
      throw new InvalidRefreshTokenException();
    }
  }

  private String signRefreshToken(RefreshToken token) {
    return JWT.create()
        .withSubject(token.getUser().getUsername())
        .withJWTId(token.getValue().toString())
        .withIssuedAt(Date.from(token.getIssuedAt().toInstant()))
        .withExpiresAt(Date.from(token.getExpireAt().toInstant()))
        .sign(algorithm);
  }

  private String issueJWT(String subject, Collection<? extends GrantedAuthority> authorities) {
    long issuedAt = System.currentTimeMillis();
    return JWT.create()
        .withSubject(subject)
        .withIssuedAt(new Date(issuedAt))
        .withExpiresAt(new Date(issuedAt + jwtExpiration.toMillis()))
        .withArrayClaim(SecurityConstants.AUTHORITIES_CLAIM, authorities.stream()
            .map(GrantedAuthority::getAuthority)
            .toArray(String[]::new))
        .sign(algorithm);
  }

}
