package com.starkindustries.accutech.payroll.payslip.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PayslipDTO {

    private EmployeeDTO employee;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd MMMM")
    private Date fromDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd MMMM")
    private Date toDate;

    private int grossIncome;
    private int incomeTax;
    private int superAnnuation;
    private int netIncome;

}
