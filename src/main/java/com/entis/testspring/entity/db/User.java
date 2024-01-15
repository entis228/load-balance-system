package com.entis.testspring.entity.db;

import com.entis.testspring.entity.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.NaturalId;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NaturalId(mutable = true)
  @Column(nullable = false, unique = true)
  private String username;

  @Enumerated(EnumType.ORDINAL)
  @Column(name = "role")
  private UserRole userRole = UserRole.ROLE_DEVELOPER;

  private byte[] password;

  @OneToMany(mappedBy = "assignee")
  private List<Task> tasks = new ArrayList<>();

  @OneToMany(mappedBy = "user")
  private List<EmotionalState> emotionalStates = new ArrayList<>();

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    User user = (User) o;

    return new EqualsBuilder().append(id, user.id)
        .append(username, user.username).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(id).append(username).toHashCode();
  }
}
