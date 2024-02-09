package com.entis.testspring.controller;

import com.entis.testspring.entity.db.EmotionalState;
import com.entis.testspring.entity.dto.EmotionalStateRequest;
import com.entis.testspring.entity.dto.EmotionalStateResponse;
import com.entis.testspring.service.EmotionalStateService;
import com.entis.testspring.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Routes.EMOTIONAL_STATES)
@AllArgsConstructor
public class EmotionalStateController {

  private EmotionalStateService emotionalStateService;
  private UserService userService;

  @PostMapping("/today")
  public EmotionalStateResponse createForToday(@AuthenticationPrincipal String username,
      @RequestBody Integer mark) {
    return new EmotionalStateResponse(emotionalStateService.createForUserForToday(username, mark));
  }

  @PutMapping
  public EmotionalStateResponse save(@RequestBody EmotionalStateRequest emotionalStateRequest) {
    EmotionalState emotionalState = emotionalStateRequest.toEntity();
    emotionalState.setUser(userService.findByUsername(emotionalStateRequest.username()));
    return new EmotionalStateResponse(emotionalStateService.save(emotionalState));
  }
}
