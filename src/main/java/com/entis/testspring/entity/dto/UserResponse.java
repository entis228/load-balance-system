package com.entis.testspring.entity.dto;

import com.entis.testspring.entity.db.User;

public record UserResponse(Long id, String username, String userRole) {

  public UserResponse(User user) {
    this(user.getId(), user.getUsername(), user.getUserRole().name());
  }
}
