package com.devsuperior.dscatalog.repository;

import com.devsuperior.dscatalog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
