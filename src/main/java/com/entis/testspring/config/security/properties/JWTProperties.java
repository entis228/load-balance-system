package com.entis.testspring.config.security.properties;

import lombok.Getter;
import lombok.Setter;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.security")
public class JWTProperties {

  private char[] secret;

  private Duration accessExpireIn;

  private Duration refreshExpireIn;

}
