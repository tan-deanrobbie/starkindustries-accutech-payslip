package com.starkindustries.accutech.payroll.payslip.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Test Reference:
 * 
 * Taxable income vs Tax on this income
 * $0 - $18,200       | Nil Nil
 * $18,201 - $37,000  | 19c for each $1 over $18,200
 * $37,001 - $87,000  | $3,572 plus 32.5c for each $1 over $37,000
 * $87,001 - $180,000 | $19,822 plus 37c for each $1 over $87,000
 * $180,001 and over  | $54,232 plus 45c for each $1 over $180,000
 */
@SpringBootTest
public class IncomeTaxCalculatorServiceTest {
    
    @Autowired
    private IncomeTaxCalculatorService service;

    /**
	 * Test OK calculateAnnual (first bracket)
	 * 
	 * Given: an annual salary of 10,000
	 * When: calculateAnnual is called
	 * Then: income tax should be 0
	 */
    @Test
    void testOK_calculateAnnual_firstBracket() {
        // prepare test data
        int annualSalary = 10000;

        // perform test
        float incomeTax = service.calculateAnnual(annualSalary);
        assertEquals(0, incomeTax);
    }

    /**
	 * Test OK calculateAnnual (second bracket)
	 * 
	 * Given: an annual salary of 20,000
	 * When: calculateAnnual is called
	 * Then: income tax should be 0
	 */
    @Test
    void testOK_calculateAnnual_secondBracket() {
        // prepare test data
        int annualSalary = 20000;

        // perform test
        float incomeTax = service.calculateAnnual(annualSalary);
        assertEquals(342f, incomeTax);
    }
    /**
	 * Test OK calculateAnnual (third bracket)
	 * 
	 * Given: an annual salary of 40,000
	 * When: calculateAnnual is called
	 * Then: income tax should be 0
	 */
    @Test
    void testOK_calculateAnnual_thirdBracket() {
        // prepare test data
        int annualSalary = 40000;

        // perform test
        float incomeTax = service.calculateAnnual(annualSalary);
        assertEquals(4547f, incomeTax);
    }
    /**
	 * Test OK calculateAnnual (fourth bracket)
	 * 
	 * Given: an annual salary of 90,000
	 * When: calculateAnnual is called
	 * Then: income tax should be 0
	 */
    @Test
    void testOK_calculateAnnual_fourthBracket() {
        // prepare test data
        int annualSalary = 90000;

        // perform test
        float incomeTax = service.calculateAnnual(annualSalary);
        assertEquals(20932f, incomeTax);
    }
    /**
	 * Test OK calculateAnnual (fifth bracket)
	 * 
	 * Given: an annual salary of 200,000
	 * When: calculateAnnual is called
	 * Then: income tax should be 0
	 */
    @Test
    void testOK_calculateAnnual_fifthBracket() {
        // prepare test data
        int annualSalary = 200000;

        // perform test
        float incomeTax = service.calculateAnnual(annualSalary);
        assertEquals(63232f, incomeTax);
    }
    /**
	 * Test OK calculateMonthly
	 * 
	 * Given: an annual salary of 20,000
	 * When: calculateAnnual is called
	 * Then: income tax should be 0 (divided by 12 of annual)
	 */
    @Test
    void testOK_calculateMonthly() {
        // prepare test data
        int annualSalary = 20000;

        // perform test
        float incomeTax = service.calculateMonthly(annualSalary);
        assertEquals(28.5f, incomeTax);
    }
}
