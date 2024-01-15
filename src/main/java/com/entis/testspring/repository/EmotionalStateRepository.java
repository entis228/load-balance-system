package com.entis.testspring.repository;

import com.entis.testspring.entity.db.EmotionalState;
import com.entis.testspring.entity.db.User;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmotionalStateRepository extends JpaRepository<EmotionalState, Long> {

  Optional<EmotionalState> findFirstByDateAndUserOrderByDate(LocalDate date, User user);

  Boolean existsFirstByDateAndUserOrderByDate(LocalDate date, User user);
}
