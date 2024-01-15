package com.entis.testspring.service;

import com.entis.testspring.entity.UserRole;
import com.entis.testspring.entity.db.Task;
import com.entis.testspring.entity.db.User;
import com.entis.testspring.repository.EmotionalStateRepository;
import com.entis.testspring.repository.TaskRepository;
import com.entis.testspring.repository.UserRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.IntegerRange;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class TaskService {

  private TaskRepository taskRepository;
  private EmotionalStateRepository emotionalStateRepository;
  private UserRepository userRepository;

  @Transactional
  public Task save(Task task) {
    return taskRepository.save(task);
  }

  @Transactional(readOnly = true)
  public List<Task> findIncomplete() {
    List<Task> tasks = taskRepository.findAllByEndDateNull();
    if (tasks == null) {
      return Collections.emptyList();
    }
    return tasks;
  }

  @Transactional
  public void deleteById(String id){
    taskRepository.deleteById(Long.valueOf(id));
  }

  @Transactional(readOnly = true)
  public List<Task> findMostAvailable(String username) {
    LocalDate dateToday = LocalDate.now();
    User currentUser = userRepository.findByUsername(username)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.VARIANT_ALSO_NEGOTIATES, "Authorised user with username %s doesn't exists"));
    emotionalStateRepository.findFirstByDateAndUserOrderByDate(LocalDate.now(), currentUser)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
            "No emotional state mark for today, complete the survey first"));
    List<Task> freeTasks = taskRepository.findAllByEndDateNullAndAssigneeNull()
        .stream().sorted(Comparator.comparingInt(Task::getDifficulty)).toList();
    if (freeTasks.isEmpty()) {
      return Collections.emptyList();
    }
    if (IntegerRange.of(1, 2).contains(freeTasks.size())) {
      return freeTasks;
    } else {
      List<User> allFreeUsers = userRepository.findAllByUserRole(UserRole.ROLE_DEVELOPER).stream()
          .filter(
              user -> emotionalStateRepository.existsFirstByDateAndUserOrderByDate(dateToday, user))
          .filter(
              user -> user.getTasks().stream().filter(task -> task.getEndDate() == null).toList()
                  .isEmpty())
          .sorted((first, second) ->
              Integer.compare(
                  emotionalStateRepository.findFirstByDateAndUserOrderByDate(dateToday, first)
                      .orElseThrow().getMark(),
                  emotionalStateRepository.findFirstByDateAndUserOrderByDate(dateToday, second)
                      .orElseThrow().getMark()))
          .toList();
      if (allFreeUsers.contains(currentUser)) {
        List<Task> chosenTasks = new LinkedList<>();
        int firstTaskIndex = (BigDecimal.valueOf(allFreeUsers.indexOf(currentUser))
            .multiply(BigDecimal.valueOf(freeTasks.size()))).divide(
            BigDecimal.valueOf(allFreeUsers.size()), RoundingMode.HALF_UP).intValue();
        chosenTasks.add(freeTasks.get(firstTaskIndex));
        if (firstTaskIndex == freeTasks.size() - 1) {
          chosenTasks.add(freeTasks.get(firstTaskIndex - 1));
        } else {
          chosenTasks.add(freeTasks.get(firstTaskIndex + 1));
        }
        return chosenTasks;
      } else {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
            "You have assigned tasks, finish them first");
      }
    }
  }
}
