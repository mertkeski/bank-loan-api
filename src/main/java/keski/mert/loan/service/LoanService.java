package keski.mert.loan.service;

import jakarta.transaction.Transactional;
import keski.mert.loan.dto.NewLoanRequest;
import keski.mert.loan.dto.NewLoanResponse;
import keski.mert.loan.exception.CustomerNotFoundException;
import keski.mert.loan.exception.InsufficientCreditLimitException;
import keski.mert.loan.model.Customer;
import keski.mert.loan.model.Installment;
import keski.mert.loan.model.Loan;
import keski.mert.loan.repository.CustomerRepository;
import keski.mert.loan.repository.LoanRepository;
import keski.mert.loan.util.MapperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoanService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoanService.class);

    private final CustomerRepository customerRepository;
    private final LoanRepository loanRepository;

    public LoanService(CustomerRepository customerRepository, LoanRepository loanRepository) {
        this.customerRepository = customerRepository;
        this.loanRepository = loanRepository;
    }

    /**
     * Creates a loan for a customer, checks available credit limit, and generates installments.
     *
     * @param request the loan request DTO
     * @return the created Loan details
     * @throws CustomerNotFoundException if the customer cannot be found by the provided ID
     * @throws InsufficientCreditLimitException if the customer does not have enough credit
     */
    @Transactional
    public NewLoanResponse createLoan(NewLoanRequest request) {
        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(CustomerNotFoundException::new);

        checkAndUpdateCreditLimit(customer, request.loanAmount());

        Loan loan = new Loan();
        loan.setCustomer(customer);
        loan.setLoanAmount(request.loanAmount());
        loan.setNumberOfInstallments(request.numberOfInstallments());
        loan.setCreatedDate(LocalDate.now());
        loan.setPaid(false);

        BigDecimal installmentAmount = calculateInstallmentAmount(request);
        List<Installment> installments = createInstallments(loan, installmentAmount);
        loan.setInstallments(installments);

        LOGGER.info("Loan created for customer {} with amount {} in {} installments.",
                customer.getId(), request.loanAmount(), installments.size());
        Loan savedLoan = loanRepository.save(loan);
        return MapperUtil.toNewLoanResponse(savedLoan);
    }

    /**
     * Checks if the customer has enough available credit and updates the used credit limit.
     *
     * @param customer the customer requesting the loan
     * @param loanAmount the requested loan amount
     * @throws InsufficientCreditLimitException if the customer does not have enough available credit
     */
    private void checkAndUpdateCreditLimit(Customer customer, BigDecimal loanAmount) {
        BigDecimal availableLimit = customer.getCreditLimit().subtract(customer.getUsedCreditLimit());
        if (availableLimit.compareTo(loanAmount) < 0) {
            LOGGER.warn("Customer {} does not have enough credit limit for a loan of {}", customer.getId(), loanAmount);
            throw new InsufficientCreditLimitException("Customer " + customer.getId() + " does not have enough credit limit.");
        }

        customer.setUsedCreditLimit(customer.getUsedCreditLimit().add(loanAmount));
        customerRepository.save(customer);
        LOGGER.info("Customer {}'s credit limit updated after loan of {}.", customer.getId(), loanAmount);
    }

    /**
     * Calculates the amount to be paid for each installment based on the loan amount, interest rate, and number of installments.
     *
     * @param newLoanRequest the loan request containing the loan amount, interest rate, and number of installments
     * @return the amount to be paid for each installment
     */
    private BigDecimal calculateInstallmentAmount(NewLoanRequest newLoanRequest) {
        BigDecimal loanAmount = newLoanRequest.loanAmount();
        BigDecimal interestRate = newLoanRequest.interestRate();
        Integer numberOfInstallments = newLoanRequest.numberOfInstallments();

        BigDecimal totalRepayment = loanAmount.multiply(BigDecimal.ONE.add(interestRate));
        return totalRepayment.divide(BigDecimal.valueOf(numberOfInstallments), 2, RoundingMode.HALF_UP);
    }

    /**
     * Creates a list of installments based on the loan details and calculated installment amount.
     *
     * @param loan the loan object for which installments are being created
     * @param installmentAmount the amount to be paid for each installment
     * @return a list of Installment objects with due dates and amounts
     */
    private List<Installment> createInstallments(Loan loan, BigDecimal installmentAmount) {
        LocalDate nearestDueDate = loan.getCreatedDate().withDayOfMonth(1).plusMonths(1);

        List<Installment> installments = new ArrayList<>();
        for (int i = 0; i < loan.getNumberOfInstallments(); i++) {
            Installment installment = new Installment();
            installment.setLoan(loan);
            installment.setAmount(installmentAmount);
            installment.setPaidAmount(BigDecimal.ZERO);
            installment.setDueDate(nearestDueDate.plusMonths(i));
            installments.add(installment);
        }

        return installments;
    }

}
