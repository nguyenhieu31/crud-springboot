package com.example.crudspringboot.repository;

import com.example.crudspringboot.entity.WhitelistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WhitelistRepository extends JpaRepository<WhitelistEntity,Integer> {
}
