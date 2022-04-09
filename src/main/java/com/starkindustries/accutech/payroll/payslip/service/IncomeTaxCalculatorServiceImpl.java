package com.starkindustries.accutech.payroll.payslip.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("IncomeTaxCalculatorService")
public class IncomeTaxCalculatorServiceImpl implements IncomeTaxCalculatorService {

    @Value("${incometax.table.bracket}")
    private int[] bracket;

    @Value("${incometax.table.rate}")
    private float[] rate;

    @Override
    public float calculateMonthly(int annualSalary) {
        return calculateAnnual(annualSalary) / 12;
    }

    @Override
    public float calculateAnnual(int annualSalary) {
        float tax = 0f;
        int value = annualSalary;
        int range = 0;

        for (int i = 0; i < bracket.length; i++) {
            if (i < bracket.length - 1) {
                // get the value from the gross income within the bracket range
                range = bracket[i] - (i == 0 ? 0 : bracket[i - 1]);
                value -= range;

                if (value > 0) {
                    // value is exceeding range, calculate tax of range
                    tax += range * rate[i];
                } else {
                    // value is within range (negative value), calculate tax of remaining value and stop
                    tax += (value + range) * rate[i];
                    break;
                }
            } else {
                // last bracket calculation (no ceiling)
                tax += value * rate[i];
            }
        }

        return tax;
    }

}
