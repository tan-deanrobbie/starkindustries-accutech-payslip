package com.starkindustries.accutech.payroll.payslip.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import com.starkindustries.accutech.payroll.payslip.dto.EmployeeDTO;
import com.starkindustries.accutech.payroll.payslip.dto.PayslipDTO;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("PayslipService")
public class PayslipServiceImpl implements PayslipService {

    private static final Logger LOG = LogManager.getLogger(PayslipService.class);

    @Autowired
    private IncomeTaxCalculatorService incomeTaxCalculatorService;

    @Override
    public PayslipDTO generateMonthlyPayslip(EmployeeDTO employee) {
        LOG.info("PayslipService | generateMonthlyPayslip | generating monthly payslip for employee {} {}",
                employee.getFirstName(), employee.getLastName());

        int grossIncome = calculateGrossIncome(employee.getAnnualSalary());
        int incomeTax = calculateIncomeTax(employee.getAnnualSalary());

        return PayslipDTO.builder()
                .employee(employee)
                .fromDate(getFromDate(employee.getPaymentMonth()))
                .toDate(getToDate(employee.getPaymentMonth()))
                .grossIncome(grossIncome)
                .incomeTax(incomeTax)
                .superAnnuation(calculateSuperAnnuation(grossIncome, employee.getSuperRate()))
                .netIncome(grossIncome - incomeTax)
                .build();
    }

    private Date getFromDate(int paymentMonth) {
        // get 1st day of the month
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, paymentMonth);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    private Date getToDate(int paymentMonth) {
        // get last day of the month
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, paymentMonth);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    private int calculateGrossIncome(int annualSalary) {
        return getRoundValue(annualSalary / 12);
    }

    private int calculateIncomeTax(int annualSalary) {
        return getRoundValue(incomeTaxCalculatorService.calculateMonthly(annualSalary));
    }

    private int calculateSuperAnnuation(int grossIncome, BigDecimal superRate) {
        return getRoundValue(grossIncome * superRate.floatValue());
    }

    private int getRoundValue(float value) {
        return Math.round(value);
    }

}
