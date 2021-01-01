package com.example.reactive.r2dbc.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataR2dbcTest
class DepartmentEntityTemplateRepositoryTest {

    @Autowired
    R2dbcEntityTemplate template;

    DepartmentEntityTemplateRepository departmentRepository;

    @BeforeEach
    void prepare() {
        this.departmentRepository = new DepartmentEntityTemplateRepository(template);
    }

    @Test
    void testFindAll() {
        StepVerifier.create(departmentRepository.findAll())
                .expectNextCount(4)
                .verifyComplete();
    }

    @Test
    void testFindById() {
        StepVerifier
                .create(departmentRepository.findById(1))
                .assertNext(department -> {
                    assertEquals(department.getId(), 1);
                    assertEquals(department.getName(), "sales");
                })
                .verifyComplete();
    }

    @Test
    void testFindByName() {
        StepVerifier
                .create(departmentRepository.findByName("human resources"))
                .assertNext(department -> {
                    assertEquals(department.getId(), 3);
                    assertEquals(department.getName(), "human resources");
                })
                .verifyComplete();
    }

    @Test
    void testInsert() {
    }

    @Test
    void testUpdateById() {
    }

    @Test
    void testDeleteById() {
    }
}