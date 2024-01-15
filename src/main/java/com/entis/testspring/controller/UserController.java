package com.entis.testspring.controller;

import com.entis.testspring.entity.db.User;
import com.entis.testspring.entity.dto.UserResponse;
import com.entis.testspring.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Routes.USERS)
@AllArgsConstructor
public class UserController {

  private UserService userService;

  @GetMapping("/{username}")
  public UserResponse findByUsername(
      @PathVariable("username")
      String username) {
    return new UserResponse(userService.findByUsername(username));
  }

  @PutMapping
  public UserResponse createOrUpdate(@RequestBody User user) {
    return new UserResponse(userService.save(user));
  }

  @DeleteMapping("/{username}")
  public void deleteByUsername(@PathVariable("username") String username) {
    userService.delete(username);
  }

  @GetMapping("/current")
  public UserResponse getCurrent(@AuthenticationPrincipal String username){
    return new UserResponse(userService.findByUsername(username));
  }
}
