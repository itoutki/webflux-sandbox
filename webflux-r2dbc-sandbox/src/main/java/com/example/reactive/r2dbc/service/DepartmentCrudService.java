package com.example.reactive.r2dbc.service;

import com.example.reactive.r2dbc.model.Department;
import com.example.reactive.r2dbc.repository.DepartmentCrudRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class DepartmentCrudService {

    DepartmentCrudRepository departmentCrudRepository;

    public DepartmentCrudService(DepartmentCrudRepository departmentCrudRepository) {
        this.departmentCrudRepository = departmentCrudRepository;
    }

    public Mono<Department> findById(int id) {
        return departmentCrudRepository.findById(id);
    }

    public Mono<Department> save(Department department) {
        return departmentCrudRepository.save(department);
    }
}
