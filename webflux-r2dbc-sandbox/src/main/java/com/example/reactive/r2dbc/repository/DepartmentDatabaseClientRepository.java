package com.example.reactive.r2dbc.repository;

import com.example.reactive.r2dbc.model.Department;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public class DepartmentDatabaseClientRepository {

    private DatabaseClient client;

    public DepartmentDatabaseClientRepository(DatabaseClient client) {
        this.client = client;
    }

    public Flux<Department> findAll() {
        return client.sql("SELECT id, name FROM department;")
                .map(row -> {
                    int id = row.get("id", Integer.class);
                    String name = row.get("name", String.class);
                    return new Department(id, name);
                }).all();
    }
}
