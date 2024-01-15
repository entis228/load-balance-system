package com.entis.testspring.entity.db.auth;

import com.entis.testspring.entity.db.User;
import java.util.List;
import lombok.Getter;

@Getter
public class AuthUserDetails extends org.springframework.security.core.userdetails.User {

  private final User source;

  public AuthUserDetails(User source) {
    super(source.getUsername(),
        new String(source.getPassword()),
        true,
        true,
        true,
        true,
        List.of(source.getUserRole())
    );
    this.source = source;
  }
}
