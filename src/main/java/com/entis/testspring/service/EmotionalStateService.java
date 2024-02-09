package com.entis.testspring.service;

import com.entis.testspring.entity.db.EmotionalState;
import com.entis.testspring.entity.db.User;
import com.entis.testspring.repository.EmotionalStateRepository;
import com.entis.testspring.repository.UserRepository;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class EmotionalStateService {

  private EmotionalStateRepository emotionalStateRepository;
  private UserRepository userRepository;

  @Transactional
  public EmotionalState save(EmotionalState emotionalState) {
    return emotionalStateRepository.save(emotionalState);
  }

  @Transactional
  public EmotionalState createForUserForToday(String username, Integer mark) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username not found"));
    EmotionalState emotionalState = new EmotionalState();
    emotionalState.setUser(user);
    emotionalState.setMark(mark);
    emotionalState.setDate(LocalDate.now());
    return emotionalStateRepository.save(emotionalState);
  }
}
