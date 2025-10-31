package com.example.payroll.model;

public class Employee {
    private int empId;
    private String name;
    private String department;
    private String designation;
    private double basicSalary;

    public Employee() {}

    public Employee(int empId, String name, String department, String designation, double basicSalary) {
        this.empId = empId;
        this.name = name;
        this.department = department;
        this.designation = designation;
        this.basicSalary = basicSalary;
    }

    // getters / setters
    public int getEmpId() { return empId; }
    public void setEmpId(int empId) { this.empId = empId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }
    public double getBasicSalary() { return basicSalary; }
    public void setBasicSalary(double basicSalary) { this.basicSalary = basicSalary; }

    @Override
    public String toString() {
        return String.format("ID:%d | %s | %s | %s | Basic: %.2f",
                empId, name, department, designation, basicSalary);
    }
}
