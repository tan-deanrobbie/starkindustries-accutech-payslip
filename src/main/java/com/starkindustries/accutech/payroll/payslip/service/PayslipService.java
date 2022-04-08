package com.starkindustries.accutech.payroll.payslip.service;

import com.starkindustries.accutech.payroll.payslip.dto.EmployeeDTO;
import com.starkindustries.accutech.payroll.payslip.dto.PayslipDTO;

public interface PayslipService {

    public PayslipDTO generateMonthlyPayslip(EmployeeDTO employee);

}
