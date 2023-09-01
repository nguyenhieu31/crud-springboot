package com.example.crudspringboot.repository;

import com.example.crudspringboot.entity.BlacklistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlacklistRepository extends JpaRepository<BlacklistEntity,Integer> {
    Optional<BlacklistEntity> findByToken(String token);
}
