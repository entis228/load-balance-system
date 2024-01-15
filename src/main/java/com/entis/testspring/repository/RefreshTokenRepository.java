package com.entis.testspring.repository;

import com.entis.testspring.entity.db.auth.RefreshToken;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

  @Query("select rt from RefreshToken rt inner join fetch rt.user u " +
      "where rt.value = :value and rt.expireAt > :when")
  Optional<RefreshToken> findIfValid(UUID value, OffsetDateTime when);

  @Query("delete from RefreshToken rt where rt = :head or rt.next = :head")
  @Modifying
  void deleteChain(RefreshToken head);

  @Query("update RefreshToken rt set rt.next = :newHead where rt = :oldHead or rt.next = :oldHead")
  @Modifying
  void updateChain(RefreshToken oldHead, RefreshToken newHead);

}
