package com.entis.testspring.entity.db.auth;

import com.entis.testspring.entity.db.User;
import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

  @Id
  @GeneratedValue
  private UUID value;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "issued_at", nullable = false)
  private OffsetDateTime issuedAt;

  @Column(name = "expire_at", nullable = false)
  private OffsetDateTime expireAt;

  @SuppressWarnings("FieldMayBeFinal")
  @OneToMany(mappedBy = "next", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<RefreshToken> previous = new ArrayList<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "next")
  @Access(AccessType.PROPERTY)
  private RefreshToken next;
}
