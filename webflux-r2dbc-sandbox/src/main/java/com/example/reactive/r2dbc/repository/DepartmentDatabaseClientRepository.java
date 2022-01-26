package com.example.reactive.r2dbc.repository;

import com.example.reactive.r2dbc.model.Department;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    public Flux<Department> findByName(String departmentName) {
        return client.sql("SELECT id, name FROM department WHERE name = :name")
                .bind("name", departmentName)
                .map(row -> {
                    int id = row.get("id", Integer.class);
                    String name = row.get("name", String.class);
                    return new Department(id, name);
                })
                .all();
    }

    public Mono<Integer> insert(Department department) {
        return client.sql("INSERT INTO department (id, name) VALUES (:id, :name)")
                .bind("id", department.getId())
                .bind("name", department.getName())
                .fetch()
                .rowsUpdated();
    }

    public Mono<Integer> updateById(Department department) {
        return client.sql("UPDATE department SET name = :name WHERE id = :id")
                .bind("id", department.getId())
                .bind("name", department.getName())
                .fetch()
                .rowsUpdated();
    }

    public Mono<Integer> deleteById(Department department) {
        return client.sql("DELETE FROM department WHERE id = :id")
                .bind("id", department.getId())
                .fetch()
                .rowsUpdated();
    }

}
