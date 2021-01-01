package com.example.reactive.r2dbc.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.data.domain.Sort.Order.desc;
import static org.springframework.data.domain.Sort.by;

@DataR2dbcTest
public class DepartmentSortingRepositoryTest {

    @Autowired
    DepartmentSortingRepository repository;

    @Test
    void testFindAll() throws Exception {
        StepVerifier.create(repository.findAll(by(desc("id"))).map(d -> d.getName()))
                .expectNext("accounting")
                .expectNext("human resources")
                .expectNext("development")
                .expectNext("sales")
                .verifyComplete();
    }
}
