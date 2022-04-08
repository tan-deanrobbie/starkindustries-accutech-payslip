package com.starkindustries.accutech.payroll.payslip.controller.error;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import com.starkindustries.accutech.payroll.payslip.dto.error.ErrorDTO;
import com.starkindustries.accutech.payroll.payslip.dto.error.ErrorsDTO;
import com.starkindustries.accutech.payroll.payslip.dto.error.ValidationErrorDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorController {
    
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorsDTO> handleConstraintViolationException(ConstraintViolationException exception) {
        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations(); 
        List<ErrorDTO> validationErrorDTOs = violations.stream()
                .map(v -> ValidationErrorDTO.builder()
                            .field(v.getPropertyPath().toString())
                            .value(v.getInvalidValue().toString())
                            .message(v.getMessage())
                            .build())
                .collect(Collectors.toList());
        return new ResponseEntity<>(
                ErrorsDTO.builder()
                    .errors(validationErrorDTOs)
                    .build(),
                HttpStatus.BAD_REQUEST);
    }
}
