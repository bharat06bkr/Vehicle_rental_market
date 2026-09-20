package com.vehiclerental.repository;

import com.vehiclerental.entity.User;
import com.vehiclerental.enums.Role;
import com.vehiclerental.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    List<User> findByRole(Role role);
    List<User> findByRoleAndStatus(Role role, UserStatus status);
    long countByRole(Role role);
    long countByRoleAndStatus(Role role, UserStatus status);
}
