package com.example.crudspringboot.repository;

import com.example.crudspringboot.entity.ProductsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductsRepository extends JpaRepository<ProductsEntity,Integer> {
}
