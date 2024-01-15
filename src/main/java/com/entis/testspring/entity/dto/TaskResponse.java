package com.entis.testspring.entity.dto;

import com.entis.testspring.entity.db.Task;
import java.time.LocalDate;

public record TaskResponse(Long id, String name, Integer difficulty, String assigneeUsername,
                           LocalDate startDate, LocalDate endDate) {

  public TaskResponse(Task task) {
    this(task.getId(), task.getName(), task.getDifficulty(), task.getAssignee().getUsername(),
        task.getStartDate(), task.getEndDate());
  }
}
