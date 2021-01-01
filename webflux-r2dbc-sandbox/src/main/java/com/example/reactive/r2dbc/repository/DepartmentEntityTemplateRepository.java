package com.example.reactive.r2dbc.repository;

import com.example.reactive.r2dbc.model.Department;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.domain.Sort.Order.desc;
import static org.springframework.data.domain.Sort.by;
import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;
import static org.springframework.data.relational.core.query.Update.update;

@Repository
public class DepartmentEntityTemplateRepository {
    private R2dbcEntityTemplate template;

    public DepartmentEntityTemplateRepository(R2dbcEntityTemplate template) {
        this.template = template;
    }

    public Flux<Department> findAll() {
        return template.select(Department.class)
                .all();
    }

    public Mono<Department> findById(int id) {
        return template.select(Department.class)
                .matching(query(where("id").is(id)))
                .one();
    }

    public Flux<Department> findByName(String name) {
        return template.select(
                query(where("name").is(name))
                .sort(by(desc("id"))),
                Department.class);
    }

    public Mono<Department> insert(Department department) {
        return template.insert(Department.class)
                .into("department")
                .using(department);
    }

    public Mono<Department> updateById(Department department) {
        return template.update(Department.class)
                .matching(query(where("id").is(department.getId())))
                .apply(update("name", department.getName()))
                .thenReturn(department);
    }

    public Mono<Department> deleteById(Department department) {
        return template.delete(department);
    }
}
