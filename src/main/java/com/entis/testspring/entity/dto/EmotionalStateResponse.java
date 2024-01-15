package com.entis.testspring.entity.dto;

import com.entis.testspring.entity.db.EmotionalState;
import java.time.LocalDate;

public record EmotionalStateResponse(Long id, LocalDate date, String username, Integer mark) {

  public EmotionalStateResponse(EmotionalState state) {
    this(state.getId(), state.getDate(), state.getUser().getUsername(), state.getMark());
  }
}
