package com.example.reactive.r2dbc.model;

import java.util.List;

public class Employee {
    private int id;
    private String name;
    private int age;
    private Department department;
    private List<Phone> phones;

    public Employee(int id, String name, int age, Department department, List<Phone> phones) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.department = department;
        this.phones = phones;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public Department getDepartment() {
        return department;
    }

    public List<Phone> getPhones() {
        return phones;
    }
}
