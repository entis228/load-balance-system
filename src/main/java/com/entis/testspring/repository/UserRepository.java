package com.entis.testspring.repository;

import com.entis.testspring.entity.db.User;
import com.entis.testspring.entity.UserRole;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUsername(String username);

  Optional<User> findFirstByUserRole(UserRole role);

  void deleteByUsername(String username);

  List<User> findAllByUserRole(UserRole userRole);
}
