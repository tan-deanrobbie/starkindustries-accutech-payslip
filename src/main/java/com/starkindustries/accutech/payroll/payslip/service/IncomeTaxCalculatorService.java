package com.starkindustries.accutech.payroll.payslip.service;

public interface IncomeTaxCalculatorService {

    float calculateMonthly(int annualSalary);
    float calculateAnnual(int annualSalary);

}
