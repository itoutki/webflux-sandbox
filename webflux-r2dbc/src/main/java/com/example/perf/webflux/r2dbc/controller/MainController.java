package com.example.perf.webflux.r2dbc.controller;

import com.example.perf.webflux.r2dbc.model.Person;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    private DatabaseClient databaseClient;

    public MainController(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @GetMapping("/")
    public Mono<JsonNode> home() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.put("message", "Hello, World!");
        return Mono.just(root);
    }

    @GetMapping("/delay/{seconds}")
    public Mono<ObjectNode> delay(@PathVariable("seconds") int seconds) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.put("message", "Hello, World!");
        return Mono.just(root).delayElement(Duration.ofSeconds(seconds));
    }

    @GetMapping("/select")
    public Flux<Person> select() {
        return databaseClient.execute("SELECT id, name FROM person")
                .as(Person.class)
                .fetch().all();
    }

    @GetMapping("/select/{id}")
    public Mono<Person> get(@PathVariable("id") int id) {
        return databaseClient.execute("SELECT id, name FROM person WHERE id = :id")
                .bind("id", id)
                .as(Person.class)
                .fetch()
                .first();

    }

    @PostMapping("/insert")
    public Mono<ObjectNode> insert(@RequestBody Person person) {
        return databaseClient.execute("INSERT INTO person (name) VALUES (:name)")
                .bind("name", person.getName())
                .fetch()
                .rowsUpdated()
                .map(count -> {
                    ObjectMapper mapper = new ObjectMapper();
                    ObjectNode root = mapper.createObjectNode();
                    root.put("message", String.format("%d data is inserted.", count));
                    return root;
                });
    }

    @PostMapping("/update")
    public Mono<Person> update(@RequestBody Person person) {
        return databaseClient.execute("UPDATE person SET name = :name WHERE id = :id")
                .bind("name", person.getName())
                .bind("id", person.getId())
                .fetch()
                .rowsUpdated()
                .then(Mono.just(person));
    }
}
