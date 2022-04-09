package com.starkindustries.accutech.payroll.payslip.config;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.starkindustries.accutech.payroll.payslip.dto.error.ErrorDTO;
import com.starkindustries.accutech.payroll.payslip.dto.error.ErrorsDTO;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component("BasicAuthEntryPoint")
public class BasicAuthEntryPoint extends BasicAuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException {

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        PrintWriter writer = response.getWriter();
        ErrorsDTO errorResponse = ErrorsDTO.builder()
                .errors(Collections.singletonList(new ErrorDTO(exception.getMessage())))
                .build();
        writer.write(new ObjectMapper().writeValueAsString(errorResponse));
    }

    @Override
    public void afterPropertiesSet() {
        setRealmName("starkindustries");
        super.afterPropertiesSet();
    }
}
