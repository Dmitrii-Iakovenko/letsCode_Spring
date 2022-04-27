package com.wutreg.sarafan.repository;

import com.wutreg.sarafan.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
