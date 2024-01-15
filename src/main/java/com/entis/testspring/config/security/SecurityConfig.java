package com.entis.testspring.config.security;

import com.entis.testspring.config.security.filters.JWTAuthenticationFilter;
import com.entis.testspring.config.security.filters.JWTAuthorizationFilter;
import com.entis.testspring.config.security.properties.JWTProperties;
import com.entis.testspring.controller.Routes;
import com.entis.testspring.entity.UserRole;
import com.entis.testspring.entity.db.EmotionalState;
import com.entis.testspring.entity.db.Task;
import com.entis.testspring.entity.db.User;
import com.entis.testspring.service.EmotionalStateService;
import com.entis.testspring.service.TaskService;
import com.entis.testspring.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.ResponseStatusException;

@Configuration
@EnableWebSecurity
@Slf4j
@EnableConfigurationProperties(JWTProperties.class)
@AllArgsConstructor
public class SecurityConfig {

  private static final String MANAGER_ROLE = "MANAGER";
  private static final String DEVELOPER_ROLE = "DEVELOPER";

  private final JWTProperties securityProperties;

  private final PasswordEncoder passwordEncoder;

  private final UserService userService;

  private final ObjectMapper objectMapper;

  private final TaskService taskService;

  private final EmotionalStateService emotionalStateService;

  @PostConstruct
  public void init() {
    initDb();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http,
      AuthenticationManager authenticationManager) throws Exception {
    http.authorizeHttpRequests(
            requests -> requests
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                .requestMatchers(HttpMethod.POST, Routes.TOKEN, Routes.TOKEN + "/refresh").permitAll()
                .requestMatchers(Routes.USERS + "/**").hasRole(MANAGER_ROLE)
                .anyRequest().authenticated())
        .addFilter(jwtAuthenticationFilter(authenticationManager))
        .addFilter(jwtAuthorizationFilter(authenticationManager))
        .exceptionHandling(configurer -> configurer
            .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
        .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(
            corsConfigurationSource()))
        .authenticationManager(authenticationManager)
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(
            httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS));
    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder){
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userService);
    authProvider.setPasswordEncoder(passwordEncoder);
    return new ProviderManager(authProvider);
  }

  private JWTAuthenticationFilter jwtAuthenticationFilter(
      AuthenticationManager authenticationManager) throws Exception {
    var filter = new JWTAuthenticationFilter(authenticationManager, objectMapper);
    filter.setFilterProcessesUrl(Routes.TOKEN);
    return filter;
  }

  private JWTAuthorizationFilter jwtAuthorizationFilter(AuthenticationManager authenticationManager) {
    return new JWTAuthorizationFilter(authenticationManager, securityProperties);
  }

  private CorsConfigurationSource corsConfigurationSource() {
    var source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
    return source;
  }

  private void initDb() {
    try {
      userService.findFirstByUserRole(UserRole.ROLE_MANAGER);
    } catch (ResponseStatusException e) {
      User manager = new User();
      manager.setUserRole(UserRole.ROLE_MANAGER);
      manager.setUsername("manager");
      manager.setPassword(passwordEncoder.encode("password").getBytes());
      User dev = new User();
      dev.setUserRole(UserRole.ROLE_DEVELOPER);
      dev.setUsername("dev1");
      dev.setPassword(passwordEncoder.encode("password").getBytes());
      userService.saveAll(List.of(manager, dev));
      Task task = new Task();
      task.setAssignee(dev);
      task.setName("[A1] Setup project");
      taskService.save(task);
      EmotionalState state = new EmotionalState();
      state.setDate(LocalDate.now());
      state.setMark(9);
      state.setUser(dev);
      emotionalStateService.save(state);
    }
  }
}
