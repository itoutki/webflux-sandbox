package com.example.reactive.r2dbc.repository;

import com.example.reactive.r2dbc.model.Department;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentCrudRepository extends ReactiveCrudRepository<Department, Integer> {
}
