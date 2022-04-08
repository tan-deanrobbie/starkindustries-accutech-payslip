package com.starkindustries.accutech.payroll.payslip.service;

public interface IncomeTaxCalculatorService {
    
    public float calculateMonthly(int annualSalary);
    public float calculateAnnual(int annualSalary);

}
