package keski.mert.loan.dto;

import java.math.BigDecimal;
import java.util.List;

public record NewLoanResponse(
        Long customerId,
        BigDecimal loanAmount,
        List<NewLoanInstallmentResponse> installments
) {
}
