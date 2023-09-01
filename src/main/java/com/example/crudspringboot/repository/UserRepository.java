package com.example.crudspringboot.repository;

import com.example.crudspringboot.entity.RoleEntity;
import com.example.crudspringboot.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findUserEntitiesByEmail(String email);
//    Optional<UserEntity> findByIdAndRoles(Integer id, Set<RoleEntity> roles);
}
