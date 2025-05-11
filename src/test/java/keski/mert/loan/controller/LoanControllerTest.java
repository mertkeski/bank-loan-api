package keski.mert.loan.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import keski.mert.loan.dto.*;
import keski.mert.loan.exception.CustomerNotFoundException;
import keski.mert.loan.exception.NoLoanFoundException;
import keski.mert.loan.service.LoanService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoanController.class)
class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoanService loanService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateLoanSuccessfully() throws Exception {
        NewLoanRequest request = new NewLoanRequest(1L, new BigDecimal("0.1"), new BigDecimal("6000"), 6);
        NewLoanInstallmentResponse installment1 = new NewLoanInstallmentResponse(new BigDecimal("2750.00"), LocalDate.now().plusMonths(1));
        NewLoanInstallmentResponse installment2 = new NewLoanInstallmentResponse(new BigDecimal("2750.00"), LocalDate.now().plusMonths(2));
        NewLoanResponse response = new NewLoanResponse(1L, new BigDecimal("5000"), List.of(installment1, installment2));

        when(loanService.createLoan(any(NewLoanRequest.class))).thenReturn(response);

        mockMvc.perform(post("/v1/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerId").value(1L))
                .andExpect(jsonPath("$.loanAmount").value(5000))
                .andExpect(jsonPath("$.installments.length()").value(2));
    }

    @Test
    void shouldReturnNotFoundWhenCustomerDoesNotExist() throws Exception {
        NewLoanRequest request = new NewLoanRequest(999L, new BigDecimal("0.1"), new BigDecimal("6000"), 6);

        when(loanService.createLoan(any(NewLoanRequest.class)))
                .thenThrow(new CustomerNotFoundException());

        mockMvc.perform(post("/v1/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Customer not found."));
    }

    @Test
    void shouldReturnNoLoanFoundErrorWhenNoLoansExist() throws Exception {
        Long customerId = 1L;
        when(loanService.getLoansByCustomer(customerId)).thenThrow(new NoLoanFoundException());

        mockMvc.perform(get("/v1/loans")
                        .param("customerId", customerId.toString()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(ErrorType.NO_LOAN_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorType.NO_LOAN_FOUND.getMessage()));
    }

    @Test
    void shouldReturnLoansWhenLoansExist() throws Exception {
        Long customerId = 1L;
        LoanQueryResponse loanResponse = new LoanQueryResponse(customerId, new BigDecimal("5000"), 12, LocalDate.now(), false, Collections.emptyList());
        when(loanService.getLoansByCustomer(customerId)).thenReturn(List.of(loanResponse));

        mockMvc.perform(get("/v1/loans")
                        .param("customerId", customerId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId").value(customerId))
                .andExpect(jsonPath("$[0].loanAmount").value(5000));
    }

}
