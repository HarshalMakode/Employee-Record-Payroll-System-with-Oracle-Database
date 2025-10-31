package com.example.payroll;

import com.example.payroll.dao.EmployeeDAO;
import com.example.payroll.model.Employee;
import com.example.payroll.model.Payroll;
import com.example.payroll.service.PayrollService;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final EmployeeDAO employeeDAO = new EmployeeDAO();
    private static final PayrollService payrollService = new PayrollService();
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== Employee Payroll System (Oracle JDBC) ===");

        while (true) {
            System.out.println("\n1. Add Employee");
            System.out.println("2. List Employees");
            System.out.println("3. Update Employee");
            System.out.println("4. Delete Employee");
            System.out.println("5. Generate & Save Payslip");
            System.out.println("6. View Payslips for Employee");
            System.out.println("7. Exit");
            System.out.print("Choose: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1" -> addEmployee();
                case "2" -> listEmployees();
                case "3" -> updateEmployee();
                case "4" -> deleteEmployee();
                case "5" -> generatePayslip();
                case "6" -> viewPayslips();
                case "7" -> { System.out.println("Bye"); System.exit(0); }
                default -> System.out.println("Invalid option");
            }
        }
    }

    private static void addEmployee() {
        System.out.print("Name: ");
        String name = sc.nextLine();
        System.out.print("Department: ");
        String dept = sc.nextLine();
        System.out.print("Designation: ");
        String des = sc.nextLine();
        System.out.print("Basic Salary: ");
        double basic = Double.parseDouble(sc.nextLine());
        Employee e = new Employee(0, name, dept, des, basic);
        if (employeeDAO.addEmployee(e)) System.out.println("Added.");
        else System.out.println("Add failed.");
    }

    private static void listEmployees() {
        List<Employee> list = employeeDAO.getAll();
        if (list.isEmpty()) System.out.println("No employees.");
        else list.forEach(System.out::println);
    }

    private static void updateEmployee() {
        System.out.print("Enter emp id: ");
        int id = Integer.parseInt(sc.nextLine());
        Employee e = employeeDAO.findById(id);
        if (e == null) { System.out.println("Not found."); return; }
        System.out.println("Current: " + e);
        System.out.print("Name (" + e.getName() + "): ");
        String name = sc.nextLine(); if (!name.isBlank()) e.setName(name);
        System.out.print("Department (" + e.getDepartment() + "): ");
        String dept = sc.nextLine(); if (!dept.isBlank()) e.setDepartment(dept);
        System.out.print("Designation (" + e.getDesignation() + "): ");
        String des = sc.nextLine(); if (!des.isBlank()) e.setDesignation(des);
        System.out.print("Basic (" + e.getBasicSalary() + "): ");
        String basicStr = sc.nextLine(); if (!basicStr.isBlank()) e.setBasicSalary(Double.parseDouble(basicStr));
        if (employeeDAO.update(e)) System.out.println("Updated.");
        else System.out.println("Update failed.");
    }

    private static void deleteEmployee() {
        System.out.print("Enter emp id to delete: ");
        int id = Integer.parseInt(sc.nextLine());
        if (employeeDAO.delete(id)) System.out.println("Deleted.");
        else System.out.println("Delete failed / not found.");
    }

    private static void generatePayslip() {
        System.out.print("Enter emp id: ");
        int id = Integer.parseInt(sc.nextLine());
        Employee e = employeeDAO.findById(id);
        if (e == null) { System.out.println("Employee not found."); return; }
        Payroll p = payrollService.computePayrollFor(e);
        System.out.println("\n--- Payslip (preview) ---");
        System.out.println("Employee: " + e.getName() + " | Dept: " + e.getDepartment());
        System.out.println("Basic: " + e.getBasicSalary());
        System.out.println("Gross: " + p.getGrossSalary());
        System.out.println("PF: " + p.getPf());
        System.out.println("Professional Tax: " + p.getProfessionalTax());
        System.out.println("Income Tax: " + p.getIncomeTax());
        System.out.println("Net: " + p.getNetSalary());

        System.out.print("Save payslip to DB? (y/n): ");
        String ans = sc.nextLine().trim();
        if (ans.equalsIgnoreCase("y")) {
            if (payrollService.persistPayroll(p)) System.out.println("Payslip saved.");
            else System.out.println("Save failed.");
        } else System.out.println("Not saved.");
    }

    private static void viewPayslips() {
        System.out.print("Enter emp id: ");
        int id = Integer.parseInt(sc.nextLine());
        List<Payroll> list = payrollService.getPayrollsForEmployee(id);
        if (list.isEmpty()) System.out.println("No payslips for this employee.");
        else {
            System.out.println("Payslips:");
            list.forEach(pay -> {
                System.out.printf("ID:%d | %s %d | Net: %.2f | Generated: %s\n",
                        pay.getPayrollId(), pay.getPayMonth(), pay.getPayYear(),
                        pay.getNetSalary(), pay.getGeneratedOn());
            });
        }
    }
}
