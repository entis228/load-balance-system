package com.entis.testspring.repository;

import com.entis.testspring.entity.db.Task;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

  List<Task> findAllByEndDateNull();
  List<Task> findAllByEndDateNullAndAssigneeNull();
}
