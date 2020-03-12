package com.psikku.backend.repository;

import com.psikku.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByUsername(String username);
}
