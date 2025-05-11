package keski.mert.loan.util;

import keski.mert.loan.dto.NewLoanInstallmentResponse;
import keski.mert.loan.dto.NewLoanResponse;
import keski.mert.loan.dto.LoanQueryInstallmentResponse;
import keski.mert.loan.dto.LoanQueryResponse;
import keski.mert.loan.model.Installment;
import keski.mert.loan.model.Loan;

import java.util.List;

public class MapperUtil {

    private MapperUtil() {
    }

    public static NewLoanResponse toNewLoanResponse(Loan loan) {
        List<Installment> installments = loan.getInstallments();
        List<NewLoanInstallmentResponse> mappedInstallments = installments.stream()
                .map(i -> new NewLoanInstallmentResponse(i.getAmount(), i.getDueDate())).toList();

        return new NewLoanResponse(loan.getCustomer().getId(), loan.getLoanAmount(), mappedInstallments);
    }

    public static LoanQueryResponse toLoanQueryResponse(Loan loan) {
        List<Installment> installments = loan.getInstallments();
        List<LoanQueryInstallmentResponse> mappedInstallments = installments.stream()
                .map(i -> new LoanQueryInstallmentResponse(i.getAmount(), i.getDueDate(), i.getPaidAmount(), i.getPaymentDate(), i.isPaid()))
                .toList();

        return new LoanQueryResponse(loan.getCustomer().getId(), loan.getLoanAmount(), loan.getNumberOfInstallments(),
                loan.getCreatedDate(), loan.isPaid(), mappedInstallments);
    }

}
