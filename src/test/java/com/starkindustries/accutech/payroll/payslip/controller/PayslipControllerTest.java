package com.starkindustries.accutech.payroll.payslip.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.starkindustries.accutech.payroll.payslip.utils.PayloadUtil.writeJsonString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.containsInAnyOrder;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.starkindustries.accutech.payroll.payslip.dto.EmployeeDTO;
import com.starkindustries.accutech.payroll.payslip.dto.PayslipDTO;
import com.starkindustries.accutech.payroll.payslip.service.PayslipService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@AutoConfigureMockMvc
@SpringBootTest
class PayslipControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PayslipService service;

	// in-memory basic authentication
	private static String ENCONDED_BASIC_AUTH = "Basic aWFtaXJvbm1hbjpzaDR3NHJtNF8xc0wwdjM=";
	private static String WRONG_ENCONDED_BASIC_AUTH  = "Basic aWFtaXJvbm1hbjpzaDR3NHJtNF8=";

	// error messages
	private static String BAD_PAYMENT_MONTH = "Field 'paymentMonth' must be between 0 and 11 (January to December).";
	private static String BAD_SUPER_RATE = "Field 'superRate' must not exceed 0.5.";
	private static String BAD_ANNUAL_SALARY = "Field 'annualSalary' must be a positive value.";

	/**
	 * Test OK getMonthlyPayslip
	 * 
	 * Given: a proper EmployeeDTO json
	 * When: /payslip/month is called
	 * Then: List of PayslipDTO is returned
	 */
	@Test
	void testOk_getMonthlyPayslip_shouldReturnPayslipDTOList() throws JsonProcessingException, Exception {
		// prepare test values
		EmployeeDTO employeeDTO = EmployeeDTO.builder()
				.firstName("Tony")
				.lastName("Stark")
				.annualSalary(1550250000)
				.superRate(BigDecimal.valueOf(0.2))
				.paymentMonth(1)
				.build();
		PayslipDTO payslipDTO = PayslipDTO.builder()
				.employee(employeeDTO)
				.fromDate(new Date())
				.toDate(new Date())
				.grossIncome(100000)
				.incomeTax(20000)
				.netIncome(30000)
				.superAnnuation(4000)
				.build();
		when(service.generateMonthlyPayslip(any())).thenReturn(payslipDTO);

		// perform test
		mockMvc.perform(post("/payslip/month")
				.content(writeJsonString(Collections.singletonList(employeeDTO)))
				.contentType(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, ENCONDED_BASIC_AUTH))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk())
				.andExpect(content().json(writeJsonString(Collections.singletonList(payslipDTO))));
		verify(service, times(1)).generateMonthlyPayslip(any());
	}

	/**
	 * Test OK getMonthlyPayslip
	 * 
	 * Given: a proper EmployeeDTO json without paymentMonth
	 * When: /payslip/month is called
	 * Then: List of PayslipDTO is returned with paymentMonth value as 1
	 */
	@Test
	void testOk_getMonthlyPayslip_shouldReturnPayslipDTOListWithPaymentMonth() throws JsonProcessingException, Exception {
		// prepare test values
		EmployeeDTO employeeDTO = EmployeeDTO.builder()
				.firstName("Tony")
				.lastName("Stark")
				.annualSalary(1550250000)
				.superRate(BigDecimal.valueOf(0.2))
				.build();
		PayslipDTO payslipDTO = PayslipDTO.builder()
				.employee(employeeDTO.toBuilder()
						.paymentMonth(1).build())
				.fromDate(new Date())
				.toDate(new Date())
				.grossIncome(100000)
				.incomeTax(20000)
				.netIncome(30000)
				.superAnnuation(4000)
				.build();
		when(service.generateMonthlyPayslip(any())).thenReturn(payslipDTO);

		// perform test
		mockMvc.perform(post("/payslip/month")
				.content(writeJsonString(Collections.singletonList(employeeDTO)))
				.contentType(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, ENCONDED_BASIC_AUTH))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk())
				.andExpect(content().json(writeJsonString(Collections.singletonList(payslipDTO))));
	}

	/**
	 * Test NG getMonthlyPayslip
	 * 
	 * Given: a bad payload (failing validations)
	 * When: /payslip/month is called
	 * Then: HTTP 400 and proper errors are returned
	 */
	@Test
	void testNg_getMonthlyPayslip_shouldFailOnBadRequest() throws JsonProcessingException, Exception {
		// prepare test values
		EmployeeDTO employeeDTO = EmployeeDTO.builder()
				.firstName("Tony")
				.lastName("Stark")
				.annualSalary(-300)
				.superRate(BigDecimal.valueOf(0.7))
				.paymentMonth(40)
				.build();

		// perform test
		mockMvc.perform(post("/payslip/month")
				.content(writeJsonString(Collections.singletonList(employeeDTO)))
				.contentType(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, ENCONDED_BASIC_AUTH))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors[*].message")
						.value(containsInAnyOrder(BAD_ANNUAL_SALARY, BAD_PAYMENT_MONTH, BAD_SUPER_RATE)));
	}

	/**
	 * Test NG getMonthlyPayslip
	 * 
	 * Given: wrong credentials
	 * When: /payslip/month is called
	 * Then: HTTP 401 & proper error is returned
	 */
	@Test
	void testNg_getMonthlyPayslip_shouldFailOnWrongCredentials() throws JsonProcessingException, Exception {
		// perform test
		mockMvc.perform(post("/payslip/month")
				.header(HttpHeaders.AUTHORIZATION, WRONG_ENCONDED_BASIC_AUTH ))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.errors[0].message").value("Bad credentials"));
	}

}
