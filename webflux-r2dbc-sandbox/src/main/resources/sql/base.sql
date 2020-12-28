CREATE TABLE employee
(
    id            INT NOT NULL PRIMARY KEY,
    name          VARCHAR(256),
    age           INT,
    department_id INT,
    FOREIGN KEY fk_department (department_id) REFERENCES department(id)
);

CREATE TABLE department
(
    id   INT NOT NULL PRIMARY KEY,
    name VARCHAR(256)
);

CREATE TABLE phone
(
    id          INT NOT NULL,
    employee_id INT NOT NULL,
    number      VARCHAR(256) NOT NULL,
    PRIMARY KEY (employee_id, id),
    FOREIGN KEY fk_employee (employee_id) REFERENCES employee(id)
);

INSERT INTO department (id, name)
VALUES (1, 'sales');
INSERT INTO department (id, name)
VALUES (2, 'development');
INSERT INTO department (id, name)
VALUES (3, 'human resources');
INSERT INTO department (id, name)
VALUES (4, 'accounting');

INSERT INTO employee (id, name, age, department_id)
VALUES (1, 'alpha', 25, 1);
INSERT INTO employee (id, name, age, department_id)
VALUES (2, 'bravo', 35, 1);
INSERT INTO employee (id, name, age, department_id)
VALUES (3, 'charlie', 45, 1);
INSERT INTO employee (id, name, age, department_id)
VALUES (4, 'delta', 25, 2);
INSERT INTO employee (id, name, age, department_id)
VALUES (5, 'echo', 35, 2);
INSERT INTO employee (id, name, age, department_id)
VALUES (6, 'foxtrot', 45, 2);
INSERT INTO employee (id, name, age, department_id)
VALUES (7, 'golf', 25, 3);
INSERT INTO employee (id, name, age, department_id)
VALUES (8, 'hotel', 35, 3);
INSERT INTO employee (id, name, age, department_id)
VALUES (9, 'india', 25, 4);
INSERT INTO employee (id, name, age, department_id)
VALUES (10, 'Juliet', 35, 4);

INSERT INTO phone (id, employee_id, number)
VALUES (1, 1, '05000010001');
INSERT INTO phone (id, employee_id, number)
VALUES (2, 1, '05000010002');
INSERT INTO phone (id, employee_id, number)
VALUES (3, 1, '05000010003');
INSERT INTO phone (id, employee_id, number)
VALUES (1, 2, '05000020001');
INSERT INTO phone (id, employee_id, number)
VALUES (2, 2, '05000020002');
INSERT INTO phone (id, employee_id, number)
VALUES (1, 3, '05000030001');
INSERT INTO phone (id, employee_id, number)
VALUES (2, 3, '05000030002');
INSERT INTO phone (id, employee_id, number)
VALUES (1, 4, '05000040001');
INSERT INTO phone (id, employee_id, number)
VALUES (2, 4, '05000040002');
INSERT INTO phone (id, employee_id, number)
VALUES (1, 5, '05000050001');
INSERT INTO phone (id, employee_id, number)
VALUES (2, 5, '05000050002');
INSERT INTO phone (id, employee_id, number)
VALUES (1, 6, '05000060001');
INSERT INTO phone (id, employee_id, number)
VALUES (2, 6, '05000060002');
INSERT INTO phone (id, employee_id, number)
VALUES (1, 7, '05000070001');
INSERT INTO phone (id, employee_id, number)
VALUES (2, 7, '05000070002');
INSERT INTO phone (id, employee_id, number)
VALUES (1, 8, '05000080001');
INSERT INTO phone (id, employee_id, number)
VALUES (2, 8, '05000080002');
INSERT INTO phone (id, employee_id, number)
VALUES (1, 9, '05000090001');
INSERT INTO phone (id, employee_id, number)
VALUES (2, 9, '05000090002');
INSERT INTO phone (id, employee_id, number)
VALUES (1, 10, '05000100001');
INSERT INTO phone (id, employee_id, number)
VALUES (2, 10, '05000100002');
