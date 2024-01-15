package com.entis.testspring.entity;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {

  ROLE_MANAGER,
  ROLE_DEVELOPER;

  @Override
  public String getAuthority() {
    return name();
  }
}
