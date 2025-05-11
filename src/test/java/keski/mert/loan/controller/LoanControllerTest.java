package keski.mert.loan.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import keski.mert.loan.dto.LoanRequest;
import keski.mert.loan.dto.NewLoanInstallmentResponse;
import keski.mert.loan.dto.NewLoanResponse;
import keski.mert.loan.exception.CustomerNotFoundException;
import keski.mert.loan.service.LoanService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
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
        LoanRequest request = new LoanRequest(1L, new BigDecimal("0.1"), new BigDecimal("6000"), 6);
        NewLoanInstallmentResponse installment1 = new NewLoanInstallmentResponse(new BigDecimal("2750.00"), LocalDate.now().plusMonths(1));
        NewLoanInstallmentResponse installment2 = new NewLoanInstallmentResponse(new BigDecimal("2750.00"), LocalDate.now().plusMonths(2));
        NewLoanResponse response = new NewLoanResponse(1L, new BigDecimal("5000"), List.of(installment1, installment2));

        Mockito.when(loanService.createLoan(any(LoanRequest.class))).thenReturn(response);

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
        LoanRequest request = new LoanRequest(999L, new BigDecimal("0.1"), new BigDecimal("6000"), 6);

        Mockito.when(loanService.createLoan(any(LoanRequest.class)))
                .thenThrow(new CustomerNotFoundException());

        mockMvc.perform(post("/v1/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Customer not found."));
    }

}
