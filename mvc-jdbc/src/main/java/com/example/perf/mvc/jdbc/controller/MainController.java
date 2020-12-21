package com.example.perf.mvc.jdbc.controller;

import com.example.perf.mvc.jdbc.model.Person;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    private JdbcTemplate jdbcTemplate;

    public MainController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/")
    public JsonNode home() {
        logger.info("home start");
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.put("message", "Hello, World!");
        return root;
    }

    @GetMapping("/delay/{millis}")
    public JsonNode delay(@PathVariable("millis") int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        return root;
    }

    @GetMapping("/select")
    public List<Person> select() {
        long start = System.currentTimeMillis();
        RowMapper<Person> rowMapper = new BeanPropertyRowMapper<>(Person.class);

        List<Person> result = jdbcTemplate.query("SELECT id, name FROM person", rowMapper);
        long end = System.currentTimeMillis();
        logger.info("select end. {} ms", end - start);
        return result;
    }

    @GetMapping("/select/{id}")
    public Person get(@PathVariable("id") int id) {
        long start = System.currentTimeMillis();
        RowMapper<Person> personRowMapper = new BeanPropertyRowMapper<>(Person.class);

        Person result = jdbcTemplate.queryForObject("SELECT * FROM person WHERE id = ? ", personRowMapper, id);
        long end = System.currentTimeMillis();
        logger.info("select for id {} end. {} ms", id, end - start);
        return result;
    }

    @PostMapping("/insert")
    public JsonNode insert(@RequestBody Person person) {
        long start = System.currentTimeMillis();
        int result = jdbcTemplate.update("INSERT INTO person (name) VALUES (?)", person.getName());
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.put("message", String.format("%d data is inserted.", result));
        long end = System.currentTimeMillis();
        logger.info("insert end. {} ms", end - start);
        return root;
    }

    @PostMapping("/update")
    public Person update(@RequestBody Person person) {
        long start = System.currentTimeMillis();

        jdbcTemplate.update("UPDATE person SET name = ? WHERE id = ?", person.getName(), person.getId());

        long end = System.currentTimeMillis();
        logger.info("update end. {} ms", end - start);
        return person;
    }
}
