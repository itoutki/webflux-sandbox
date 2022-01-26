package com.example.reactive.r2dbc.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import reactor.test.StepVerifier;

@DataR2dbcTest
public class DepartmentCrudRepositoryTest {

    @Autowired
    DepartmentCrudRepository repository;

    @Test
    void testFindAll() throws Exception {
        StepVerifier.create(repository.findAll())
                .expectNextCount(4)
                .verifyComplete();
    }

    @Test
    void testFindByName() throws Exception {
        StepVerifier.create(repository.findByName("sales"))
                .expectNextCount(1)
                .verifyComplete();
    }
}
