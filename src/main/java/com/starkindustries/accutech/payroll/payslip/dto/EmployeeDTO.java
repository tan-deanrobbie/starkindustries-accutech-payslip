package com.starkindustries.accutech.payroll.payslip.dto;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class EmployeeDTO {

    private String firstName;
    private String lastName;

    @Positive(message = "Field 'annualSalary' must be a positive value.")
    private int annualSalary;

    @Min(value = 0, message = "Field 'paymentMonth' must be between 0 and 11 (January to December).")
    @Max(value = 11, message = "Field 'paymentMonth' must be between 0 and 11 (January to December).")
    private int paymentMonth;

    @DecimalMin(value = "0.0", message = "Field 'superRate' must not be below 0.0.")
    @DecimalMax(value = "0.5", message = "Field 'superRate' must not exceed 0.5.")
    private BigDecimal superRate;
}
