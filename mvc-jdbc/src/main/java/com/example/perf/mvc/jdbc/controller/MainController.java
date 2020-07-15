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
        logger.info("home end   : response => {}", root.toString());
        return root;
    }

    @GetMapping("/delay/{second}")
    public JsonNode delay(@PathVariable("second") int second) {
        logger.info("delay start. wait {} sec", second);
        try {
            Thread.sleep(second * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.put("message", String.format("wait %d sec", second));
        logger.info("delay end   : response => {}", root.toString());
        return root;
    }

    @GetMapping("/select")
    public List<Person> select() {
        logger.info("select start.");
        RowMapper<Person> rowMapper = new BeanPropertyRowMapper<>(Person.class);

        List<Person> result = jdbcTemplate.query("SELECT id, name FROM person", rowMapper);
        logger.info("select end.");
        return result;
    }

    @GetMapping("/select/{id}")
    public Person get(@PathVariable("id") int id) {
        logger.info("select for id {} start.", id);
        RowMapper<Person> personRowMapper = new BeanPropertyRowMapper<>(Person.class);

        Person result = jdbcTemplate.queryForObject("SELECT * FROM person WHERE id = ? ", personRowMapper, id);
        logger.info("select for id {} end.", id);
        return result;
    }

    @PostMapping("/insert")
    public JsonNode insert(@RequestBody Person person) {
        logger.info("insert start.");
        int result = jdbcTemplate.update("INSERT INTO person (name) VALUES (?)", person.getName());
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.put("message", String.format("%d data is inserted.", result));
        logger.info("insert end.");
        return root;
    }

    @PostMapping("/update")
    public Person update(@RequestBody Person person) {
        logger.info("update start.");

        jdbcTemplate.update("UPDATE person SET name = ? WHERE id = ?", person.getName(), person.getId());

        logger.info("update end.");
        return person;
    }
}
