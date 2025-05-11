package keski.mert.loan.service;

import keski.mert.loan.dto.NewLoanRequest;
import keski.mert.loan.dto.NewLoanResponse;
import keski.mert.loan.exception.CustomerNotFoundException;
import keski.mert.loan.model.Customer;
import keski.mert.loan.model.Loan;
import keski.mert.loan.repository.CustomerRepository;
import keski.mert.loan.repository.LoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private LoanService loanService;

    private NewLoanRequest newLoanRequest;
    private Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        newLoanRequest = new NewLoanRequest(
                1L,
                new BigDecimal("0.15"),
                new BigDecimal("5000"),
                12);

        customer = new Customer();
        customer.setId(1L);
        customer.setCreditLimit(new BigDecimal("10000"));
        customer.setUsedCreditLimit(new BigDecimal("2000"));
    }

    @Test
    void createLoan_shouldThrowException_whenCustomerNotFound() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class,
                () -> loanService.createLoan(newLoanRequest));
    }

    @Test
    void createLoan_shouldReturnNewLoanResponseWithInstallments() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(loanRepository.save(any(Loan.class))).thenAnswer(invocation -> invocation.getArgument(0));

        NewLoanResponse response = loanService.createLoan(newLoanRequest);

        System.out.println(response);

        assertNotNull(response);
        assertEquals(1L, response.customerId());
        assertEquals(newLoanRequest.loanAmount(), response.loanAmount());
        assertEquals(12, response.installments().size());

        assertNotNull(response.installments().getFirst().dueDate());
        assertTrue(response.installments().getFirst().amount().compareTo(BigDecimal.ZERO) > 0);

        verify(customerRepository).save(any(Customer.class));
        verify(loanRepository).save(any(Loan.class));
    }

}
