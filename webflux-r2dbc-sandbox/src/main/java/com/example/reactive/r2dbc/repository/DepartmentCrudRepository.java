package com.example.reactive.r2dbc.repository;

import com.example.reactive.r2dbc.model.Department;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface DepartmentCrudRepository extends ReactiveCrudRepository<Department, Integer> {

    Flux<Department> findByName(String name);
}
