package com.entis.testspring.service;

import com.entis.testspring.entity.db.User;
import com.entis.testspring.entity.UserRole;
import com.entis.testspring.entity.db.auth.AuthUserDetails;
import com.entis.testspring.repository.UserRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

  private UserRepository userRepository;

  @Transactional
  public User save(User user) {
    return userRepository.save(user);
  }

  @Transactional
  public void saveAll(List<User> users) {
    userRepository.saveAll(users);
  }

  @Transactional(readOnly = true)
  public User findByUsername(String username) {
    return userRepository.findByUsername(username).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
            "User with username %s doesn't exists".formatted(username)));
  }

  @Transactional(readOnly = true)
  public void findFirstByUserRole(UserRole role) {
    userRepository.findFirstByUserRole(role)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
  }

  @Transactional
  public void delete(String username) {
    userRepository.deleteByUsername(username);
  }

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));
    return new AuthUserDetails(user);
  }
}
