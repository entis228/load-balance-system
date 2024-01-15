package com.entis.testspring.controller;

import com.entis.testspring.entity.db.Task;
import com.entis.testspring.entity.dto.TaskResponse;
import com.entis.testspring.service.TaskService;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Routes.TASKS)
@AllArgsConstructor
public class TaskController {

  private final TaskService taskService;

  @GetMapping("/incomplete")
  public List<TaskResponse> findIncomplete(){
    return taskService.findIncomplete().stream().map(TaskResponse::new).toList();
  }

  @GetMapping("/theBest")
  public List<TaskResponse> findMostAvailable(@AuthenticationPrincipal String username){
    return taskService.findMostAvailable(username).stream().map(TaskResponse::new).toList();
  }

  @PutMapping
  public TaskResponse save(@RequestBody Task task){
    return new TaskResponse(taskService.save(task));
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable String id){
    taskService.deleteById(id);
  }
}
