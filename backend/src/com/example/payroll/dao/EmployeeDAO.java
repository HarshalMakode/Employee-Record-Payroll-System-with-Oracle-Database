package com.example.payroll.dao;

import com.example.payroll.db.DBConnection;
import com.example.payroll.model.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    public boolean addEmployee(Employee emp) {
        String sql = "INSERT INTO employees (emp_name, department, designation, basic_salary) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, emp.getName());
            ps.setString(2, emp.getDepartment());
            ps.setString(3, emp.getDesignation());
            ps.setDouble(4, emp.getBasicSalary());
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.err.println("Add employee error: " + ex.getMessage());
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Employee> getAll() {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT emp_id, emp_name, department, designation, basic_salary FROM employees ORDER BY emp_id";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Employee e = new Employee(
                        rs.getInt("emp_id"),
                        rs.getString("emp_name"),
                        rs.getString("department"),
                        rs.getString("designation"),
                        rs.getDouble("basic_salary")
                );
                list.add(e);
            }
        } catch (Exception e) {
            System.err.println("Get all employees error: " + e.getMessage());
        }
        return list;
    }

    public Employee findById(int id) {
        String sql = "SELECT emp_id, emp_name, department, designation, basic_salary FROM employees WHERE emp_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Employee(
                            rs.getInt("emp_id"),
                            rs.getString("emp_name"),
                            rs.getString("department"),
                            rs.getString("designation"),
                            rs.getDouble("basic_salary")
                    );
                }
            }
        } catch (Exception e) {
            System.err.println("Find by id error: " + e.getMessage());
        }
        return null;
    }

    public boolean update(Employee emp) {
        String sql = "UPDATE employees SET emp_name=?, department=?, designation=?, basic_salary=? WHERE emp_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, emp.getName());
            ps.setString(2, emp.getDepartment());
            ps.setString(3, emp.getDesignation());
            ps.setDouble(4, emp.getBasicSalary());
            ps.setInt(5, emp.getEmpId());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            System.err.println("Update error: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM employees WHERE emp_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            System.err.println("Delete error: " + e.getMessage());
            return false;
        }
    }
}
