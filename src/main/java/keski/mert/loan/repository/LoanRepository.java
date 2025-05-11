package keski.mert.loan.repository;

import keski.mert.loan.model.Customer;
import keski.mert.loan.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByCustomer(Customer customer);
}
