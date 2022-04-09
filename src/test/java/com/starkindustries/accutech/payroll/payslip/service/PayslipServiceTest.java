package com.starkindustries.accutech.payroll.payslip.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import com.starkindustries.accutech.payroll.payslip.dto.EmployeeDTO;
import com.starkindustries.accutech.payroll.payslip.dto.PayslipDTO;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class PayslipServiceTest {

    @Autowired
    private PayslipService payslipService;

    @MockBean
    private IncomeTaxCalculatorService incomeTaxCalculatorService;

    /**
     * Test OK generateMonthlyPayslip
     * 
	 * Given: an EmployeeDTO
	 * When: generateMonthlyPayslip is called
	 * Then: a correct PayslipDTO is returned
	 */
	@Test
	void testOk_generateMonthlyPayslip_shouldReturnCorrectPayslipDTO() {
        // prepare test values
        int annualSalary = 12000;
		EmployeeDTO employeeDTO = EmployeeDTO.builder()
                .firstName("Tony")
                .lastName("Stark")
                .annualSalary(annualSalary)
                .superRate(BigDecimal.valueOf(0.1))
                .paymentMonth(2)
                .build();

        Calendar fromDate = Calendar.getInstance();
        fromDate.set(Calendar.MONTH, Calendar.MARCH);
        fromDate.set(Calendar.DAY_OF_MONTH, 1);
        Calendar toDate = Calendar.getInstance();
        toDate.set(Calendar.MONTH, Calendar.MARCH);
        toDate.set(Calendar.DAY_OF_MONTH, toDate.getActualMaximum(Calendar.DAY_OF_MONTH));
        PayslipDTO payslipDTO = PayslipDTO.builder()
                .employee(employeeDTO)
                .fromDate(fromDate.getTime())
                .toDate(toDate.getTime())
                .grossIncome(1000)
                .incomeTax(1) // calculation not tested here, only rounding of value
                .netIncome(999)
                .superAnnuation(100)
                .build();
        when(incomeTaxCalculatorService.calculateMonthly(annualSalary)).thenReturn(0.5f);
        
        // perform test
        PayslipDTO actualPayslipDTO = payslipService.generateMonthlyPayslip(employeeDTO);
        assertTrue(getDateDays(actualPayslipDTO.getFromDate()) == getDateDays(payslipDTO.getFromDate()), "FromDate day is incorrect");
        assertTrue(getDateMonths(actualPayslipDTO.getFromDate()) == getDateMonths(payslipDTO.getFromDate()), "FromDate month is incorrect");
        assertTrue(getDateDays(actualPayslipDTO.getToDate()) == getDateDays(payslipDTO.getToDate()), "ToDate day is incorrect");
        assertTrue(getDateMonths(actualPayslipDTO.getToDate()) == getDateMonths(payslipDTO.getToDate()), "ToDate month is incorrect");
        assertEquals(payslipDTO.getGrossIncome(), actualPayslipDTO.getGrossIncome());
        assertEquals(payslipDTO.getIncomeTax(), actualPayslipDTO.getIncomeTax());
        assertEquals(payslipDTO.getNetIncome(), actualPayslipDTO.getNetIncome());
        assertEquals(payslipDTO.getSuperAnnuation(), actualPayslipDTO.getSuperAnnuation());
        verify(incomeTaxCalculatorService, times(1)).calculateMonthly(annualSalary);
    }

    private int getDateDays(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DATE);
    }

    private int getDateMonths(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH);
    }
}
