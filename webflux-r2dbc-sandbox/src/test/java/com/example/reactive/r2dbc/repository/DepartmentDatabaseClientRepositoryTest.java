package com.example.reactive.r2dbc.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@DataR2dbcTest
class DepartmentDatabaseClientRepositoryTest {

    @Autowired
    DatabaseClient client;

    DepartmentDatabaseClientRepository repository;

    @BeforeEach
    void prepare() {
        this.repository = new DepartmentDatabaseClientRepository(this.client);
    }

    @Test
    void testFindAll() {
        StepVerifier.create(repository.findAll())
                .expectNextCount(4)
                .verifyComplete();
    }
}