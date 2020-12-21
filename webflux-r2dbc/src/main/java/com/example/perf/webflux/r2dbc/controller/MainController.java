package com.example.perf.webflux.r2dbc.controller;

import com.example.perf.webflux.r2dbc.model.Person;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Update;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static org.springframework.data.r2dbc.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

@RestController
public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    private DatabaseClient databaseClient;
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    public MainController(DatabaseClient databaseClient, R2dbcEntityTemplate r2dbcEntityTemplate) {
        this.databaseClient = databaseClient;
        this.r2dbcEntityTemplate = r2dbcEntityTemplate;
    }

    @GetMapping("/")
    public Mono<JsonNode> home() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.put("message", "Hello, World!");
        return Mono.just(root);
    }

    @GetMapping("/delay/{millis}")
    public Mono<ObjectNode> delay(@PathVariable("millis") int millis) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.put("message", "Hello, World!");
        return Mono.just(root).delayElement(Duration.ofMillis(millis));
    }

    @GetMapping("/select")
    public Flux<Person> select() {
        return r2dbcEntityTemplate.select(Person.class)
                .all();
    }

    @GetMapping("/select/{id}")
    public Mono<Person> get(@PathVariable("id") int id) {
        return r2dbcEntityTemplate.select(Person.class)
                .from("person")
                .matching(query(where("id").is(id)))
                .one();
    }

    @PostMapping("/insert")
    public Mono<Person> insert(@RequestBody Person person) {
        return r2dbcEntityTemplate.insert(Person.class)
                .into("person")
                .using(person);
    }

    @PostMapping("/update")
    public Mono<Person> update(@RequestBody Person person) {
        return r2dbcEntityTemplate.update(Person.class)
                .inTable("person")
                .matching(query(where("id").is(person.getId())))
                .apply(Update.update("name", person.getName()))
                .map(i -> person);
    }
}
