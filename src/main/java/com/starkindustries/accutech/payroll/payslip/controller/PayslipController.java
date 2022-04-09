package com.starkindustries.accutech.payroll.payslip.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.starkindustries.accutech.payroll.payslip.dto.EmployeeDTO;
import com.starkindustries.accutech.payroll.payslip.dto.PayslipDTO;
import com.starkindustries.accutech.payroll.payslip.service.PayslipService;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping(path = "/payslip", produces = "application/json")
public class PayslipController {

    private static final Logger LOG = LogManager.getLogger(PayslipController.class);

    @Autowired
    private PayslipService payslipService;

    @PostMapping(path = "/month")
    public List<PayslipDTO> getMonthlyPayslip(@Valid @RequestBody List<EmployeeDTO> employees) {
        LOG.info("PayslipController | getMonthlyPayslip | generating payslips");
        return employees.stream()
                .map(e -> payslipService.generateMonthlyPayslip(e))
                .collect(Collectors.toList());
    }
}
