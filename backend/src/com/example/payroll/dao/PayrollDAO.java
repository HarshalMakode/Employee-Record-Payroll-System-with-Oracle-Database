package com.example.payroll.dao;

import com.example.payroll.db.DBConnection;
import com.example.payroll.model.Payroll;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PayrollDAO {

    public boolean savePayroll(Payroll p) {
        String sql = "INSERT INTO payroll (emp_id, month, year, gross_salary, pf, professional_tax, income_tax, net_salary) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, p.getEmpId());
            ps.setString(2, p.getPayMonth());
            ps.setInt(3, p.getPayYear());
            ps.setDouble(4, p.getGrossSalary());
            ps.setDouble(5, p.getPf());
            ps.setDouble(6, p.getProfessionalTax());
            ps.setDouble(7, p.getIncomeTax());
            ps.setDouble(8, p.getNetSalary());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.err.println("Save payroll error: " + e.getMessage());
            return false;
        }
    }

    public List<Payroll> findByEmployee(int empId) {
        List<Payroll> list = new ArrayList<>();
        String sql = "SELECT * FROM payroll WHERE emp_id = ? ORDER BY generated_on DESC";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, empId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Payroll p = new Payroll();
                    p.setPayrollId(rs.getInt("payroll_id"));
                    p.setEmpId(rs.getInt("emp_id"));
                    p.setPayMonth(rs.getString("month"));
                    p.setPayYear(rs.getInt("year"));
                    p.setGrossSalary(rs.getDouble("gross_salary"));
                    p.setPf(rs.getDouble("pf"));
                    p.setProfessionalTax(rs.getDouble("professional_tax"));
                    p.setIncomeTax(rs.getDouble("income_tax"));
                    p.setNetSalary(rs.getDouble("net_salary"));
                    p.setGeneratedOn(rs.getDate("generated_on"));
                    list.add(p);
                }
            }
        } catch (Exception e) {
            System.err.println("Find payroll by employee error: " + e.getMessage());
        }
        return list;
    }
}
