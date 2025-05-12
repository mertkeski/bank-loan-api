package keski.mert.loan.controller;

import jakarta.validation.Valid;
import keski.mert.loan.dto.*;
import keski.mert.loan.service.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    public ResponseEntity<NewLoanResponse> createLoan(@RequestBody @Valid NewLoanRequest newLoanRequest) {
        NewLoanResponse response = loanService.createLoan(newLoanRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<LoanQueryResponse>> getLoansByCustomer(@RequestParam Long customerId) {
        List<LoanQueryResponse> loans = loanService.getLoansByCustomer(customerId);
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/{loanId}/installments")
    public ResponseEntity<List<LoanQueryInstallmentResponse>> getInstallmentsByLoanId(@PathVariable Long loanId) {
        List<LoanQueryInstallmentResponse> installments = loanService.getInstallmentsByLoanId(loanId);
        return ResponseEntity.ok(installments);
    }

    @PostMapping("/{loanId}/payments")
    public ResponseEntity<PaymentResponse> payLoanInstallments(@PathVariable Long loanId,
                                                               @RequestBody PaymentRequest paymentRequest) {
        PaymentResponse response = loanService.payLoanInstallments(loanId, paymentRequest);
        return ResponseEntity.ok(response);
    }

}
