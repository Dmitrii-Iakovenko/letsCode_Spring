package com.wutreg.sarafan.repository;

import com.wutreg.sarafan.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
