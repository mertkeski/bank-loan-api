package keski.mert.loan.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record LoanQueryResponse(
        Long customerId,
        BigDecimal loanAmount,
        Integer numberOfInstallments,
        LocalDate createdDate,
        boolean isPaid,
        List<LoanQueryInstallmentResponse> installments
) {
}
