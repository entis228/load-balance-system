package com.entis.testspring.service;

import com.entis.testspring.entity.db.EmotionalState;
import com.entis.testspring.repository.EmotionalStateRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class EmotionalStateService {

  private EmotionalStateRepository repository;

  @Transactional
  public EmotionalState save(EmotionalState emotionalState){
    return repository.save(emotionalState);
  }
}
