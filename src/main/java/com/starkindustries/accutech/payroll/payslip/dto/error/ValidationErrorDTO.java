package com.starkindustries.accutech.payroll.payslip.dto.error;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ValidationErrorDTO extends ErrorDTO {
    
    private String field;
    private String value;

    @Builder
    public ValidationErrorDTO(String message, String field, String value) {
        super(message);
        this.field = field;
        this.value = value;
    }
}
