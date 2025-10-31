package com.example.payroll.service;

import com.example.payroll.dao.PayrollDAO;
import com.example.payroll.model.Employee;
import com.example.payroll.model.Payroll;

import java.util.Calendar;
import java.util.List;

public class PayrollService {

    private final PayrollDAO payrollDAO = new PayrollDAO();

    // Get allowances from DB if you'd like; for simplicity use defaults or you can add DAO for allowances
    private static final double HRA_PERCENT = 20.0;
    private static final double DA_PERCENT = 10.0;
    private static final double PF_PERCENT = 12.0;
    private static final double PROFESSIONAL_TAX = 200.0;

    public Payroll computePayrollFor(Employee emp) {
        double basic = emp.getBasicSalary();
        double hra = basic * HRA_PERCENT / 100.0;
        double da = basic * DA_PERCENT / 100.0;
        double gross = basic + hra + da;
        double pf = basic * PF_PERCENT / 100.0;
        double taxable = gross - pf - PROFESSIONAL_TAX;
        double incomeTax = computeTax(taxable);
        double net = gross - pf - PROFESSIONAL_TAX - incomeTax;

        Payroll p = new Payroll();
        p.setEmpId(emp.getEmpId());
        p.setGrossSalary(round(gross));
        p.setPf(round(pf));
        p.setProfessionalTax(round(PROFESSIONAL_TAX));
        p.setIncomeTax(round(incomeTax));
        p.setNetSalary(round(net));

        Calendar cal = Calendar.getInstance();
        p.setPayMonth(getMonthName(cal.get(Calendar.MONTH)));
        p.setPayYear(cal.get(Calendar.YEAR));

        return p;
    }

    private String getMonthName(int monthIndex) {
        String[] names = {"January","February","March","April","May","June","July","August","September","October","November","December"};
        return names[monthIndex];
    }

    // basic tax calculation (annualize then slab then monthly)
    private double computeTax(double monthlyTaxable) {
        double annual = monthlyTaxable * 12.0;
        double annualTax = 0.0;
        if (annual <= 250000) annualTax = 0;
        else if (annual <= 500000) annualTax = (annual - 250000) * 0.05;
        else if (annual <= 1000000) annualTax = (250000.0 * 0.05) + (annual - 500000) * 0.20;
        else annualTax = (250000.0 * 0.05) + (500000.0 * 0.20) + (annual - 1000000.0) * 0.30;
        return annualTax / 12.0;
    }

    private double round(double v) {
        return Math.round(v * 100.0) / 100.0;
    }

    public boolean persistPayroll(Payroll p) {
        return payrollDAO.savePayroll(p);
    }

    public List<Payroll> getPayrollsForEmployee(int empId) {
        return payrollDAO.findByEmployee(empId);
    }
}
