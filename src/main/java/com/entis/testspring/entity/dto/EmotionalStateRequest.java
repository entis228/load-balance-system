package com.entis.testspring.entity.dto;

import com.entis.testspring.entity.db.EmotionalState;
import java.time.LocalDate;

public record EmotionalStateRequest(LocalDate date, String username, Integer mark) {

  public EmotionalState toEntity(){
    EmotionalState emotionalState = new EmotionalState();
    emotionalState.setDate(date);
    emotionalState.setMark(mark);
    return emotionalState;
  }
}
