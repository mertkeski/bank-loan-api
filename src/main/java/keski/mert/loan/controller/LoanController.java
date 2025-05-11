package keski.mert.loan.controller;

import jakarta.validation.Valid;
import keski.mert.loan.dto.LoanRequest;
import keski.mert.loan.dto.NewLoanResponse;
import keski.mert.loan.service.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    public ResponseEntity<NewLoanResponse> createLoan(@RequestBody @Valid LoanRequest loanRequest) {
        NewLoanResponse response = loanService.createLoan(loanRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
