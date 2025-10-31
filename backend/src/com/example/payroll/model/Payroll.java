package com.example.payroll.model;

import java.util.Date;

public class Payroll {
    private int payrollId;
    private int empId;
    private String payMonth;
    private int payYear;
    private double grossSalary;
    private double pf;
    private double professionalTax;
    private double incomeTax;
    private double netSalary;
    private Date generatedOn;

    // getters / setters
    public int getPayrollId() { return payrollId; }
    public void setPayrollId(int payrollId) { this.payrollId = payrollId; }
    public int getEmpId() { return empId; }
    public void setEmpId(int empId) { this.empId = empId; }
    public String getPayMonth() { return payMonth; }
    public void setPayMonth(String payMonth) { this.payMonth = payMonth; }
    public int getPayYear() { return payYear; }
    public void setPayYear(int payYear) { this.payYear = payYear; }
    public double getGrossSalary() { return grossSalary; }
    public void setGrossSalary(double grossSalary) { this.grossSalary = grossSalary; }
    public double getPf() { return pf; }
    public void setPf(double pf) { this.pf = pf; }
    public double getProfessionalTax() { return professionalTax; }
    public void setProfessionalTax(double professionalTax) { this.professionalTax = professionalTax; }
    public double getIncomeTax() { return incomeTax; }
    public void setIncomeTax(double incomeTax) { this.incomeTax = incomeTax; }
    public double getNetSalary() { return netSalary; }
    public void setNetSalary(double netSalary) { this.netSalary = netSalary; }
    public Date getGeneratedOn() { return generatedOn; }
    public void setGeneratedOn(Date generatedOn) { this.generatedOn = generatedOn; }
}
