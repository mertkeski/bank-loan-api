package keski.mert.loan.service;

import keski.mert.loan.dto.*;
import keski.mert.loan.exception.CustomerNotFoundException;
import keski.mert.loan.exception.NoLoanFoundForCustomerException;
import keski.mert.loan.model.Customer;
import keski.mert.loan.model.Installment;
import keski.mert.loan.model.Loan;
import keski.mert.loan.repository.CustomerRepository;
import keski.mert.loan.repository.LoanRepository;
import keski.mert.loan.util.UserUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
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
        try (MockedStatic<UserUtil> userUtil = mockStatic(UserUtil.class)) {
            doNothing().when(UserUtil.class);
            UserUtil.checkAccess(any());

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

    @Test
    void shouldThrowNoLoanFoundExceptionWhenNoLoansForCustomer() {
        try (MockedStatic<UserUtil> userUtil = mockStatic(UserUtil.class)) {
            doNothing().when(UserUtil.class);
            UserUtil.checkAccess(any());

            Long customerId = 1L;
            when(customerRepository.findById(customerId)).thenReturn(java.util.Optional.of(customer));
            when(loanRepository.findByCustomer(customer)).thenReturn(Collections.emptyList());

            assertThrows(NoLoanFoundForCustomerException.class, () -> loanService.getLoansByCustomer(customerId));
        }
    }

    @Test
    void shouldReturnLoansWhenLoansExistForCustomer() {
        try (MockedStatic<UserUtil> userUtil = mockStatic(UserUtil.class)) {
            doNothing().when(UserUtil.class);
            UserUtil.checkAccess(any());

            Long customerId = 1L;
            Loan loan = new Loan();
            loan.setId(1L);
            loan.setLoanAmount(new BigDecimal("5000"));
            loan.setCustomer(customer);

            when(customerRepository.findById(customerId)).thenReturn(java.util.Optional.of(customer));
            when(loanRepository.findByCustomer(customer)).thenReturn(List.of(loan));

            List<LoanQueryResponse> loans = loanService.getLoansByCustomer(customerId);

            assertFalse(loans.isEmpty());
            assertEquals(1, loans.size());
        }
    }

    @Test
    void shouldPayEligibleInstallmentsCorrectly() {
        try (MockedStatic<UserUtil> userUtil = mockStatic(UserUtil.class)) {
            doNothing().when(UserUtil.class);
            UserUtil.checkAccess(any());

            Long loanId = 1L;
            BigDecimal paymentAmount = BigDecimal.valueOf(230);
            PaymentRequest request = new PaymentRequest(paymentAmount);

            LocalDate now = LocalDate.now();

            Installment i1 = createInstallment(BigDecimal.valueOf(80), now.plusDays(10)); // eligible
            Installment i2 = createInstallment(BigDecimal.valueOf(80), now.plusDays(30)); // eligible
            Installment i3 = createInstallment(BigDecimal.valueOf(80), now.plusDays(60)); // eligible but exceeds payment amoun
            Installment i4 = createInstallment(BigDecimal.valueOf(80), now.plusDays(120)); // not eligible (4 months ahead)

            Loan loan = new Loan();
            loan.setInstallments(List.of(i1, i2, i3, i4));
            loan.setPaid(false);
            loan.setCustomer(customer);

            when(loanRepository.findById(loanId)).thenReturn(Optional.of(loan));

            PaymentResponse response = loanService.payLoanInstallments(loanId, request);

            assertEquals(2, response.numberOfInstallmentsPaid());
            assertEquals(BigDecimal.valueOf(160), response.totalAmountPaid());
            assertFalse(response.isLoanPaidCompletely());
            assertTrue(i1.isPaid());
            assertTrue(i2.isPaid());
            assertFalse(i3.isPaid());
            assertFalse(i4.isPaid());
            verify(loanRepository).save(loan);
        }
    }

    private Installment createInstallment(BigDecimal amount, LocalDate dueDate) {
        Installment installment = new Installment();
        installment.setAmount(amount);
        installment.setDueDate(dueDate);
        installment.setPaid(false);
        return installment;
    }

}
