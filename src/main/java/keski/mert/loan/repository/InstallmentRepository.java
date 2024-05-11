package keski.mert.loan.repository;

import keski.mert.loan.model.Installment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstallmentRepository extends JpaRepository<Installment, Long> {

}
