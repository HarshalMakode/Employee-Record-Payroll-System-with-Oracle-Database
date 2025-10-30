CREATE TABLE employees (
  eid NUMBER(5),
  ename VARCHAR2(100) NOT NULL,
  designation VARCHAR2(100),
  salary NUMBER(10,2),
  CONSTRAINT employees_PK_eid PRIMARY KEY (eid),
  CONSTRAINT employees_CK_salary CHECK (salary >= 0)
);
