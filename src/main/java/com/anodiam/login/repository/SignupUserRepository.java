package com.anodiam.login.repository;

import com.anodiam.login.models.SignupUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SignupUserRepository extends JpaRepository<SignupUser, Long> {
  Optional<SignupUser> findByEmail(String email);

  Boolean existsByEmail(String email);
}
