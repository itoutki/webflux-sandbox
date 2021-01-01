package com.example.reactive.r2dbc.repository;

import com.example.reactive.r2dbc.model.Department;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;

public interface DepartmentSortingRepository extends ReactiveSortingRepository<Department, Integer> {
}
