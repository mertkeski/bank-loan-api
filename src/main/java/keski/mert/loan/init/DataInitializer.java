package keski.mert.loan.init;

import jakarta.annotation.PostConstruct;
import keski.mert.loan.model.Customer;
import keski.mert.loan.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DataInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataInitializer.class);

    private final CustomerRepository customerRepository;

    public DataInitializer(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @PostConstruct
    public void initializeData() {
        Customer customer1 = new Customer();
        customer1.setName("Mert");
        customer1.setSurname("Keski");
        customer1.setCreditLimit(new BigDecimal("40000"));
        customer1.setUsedCreditLimit(BigDecimal.ZERO);

        Customer customer2 = new Customer();
        customer2.setName("John");
        customer2.setSurname("Smith");
        customer2.setCreditLimit(new BigDecimal("150000"));
        customer2.setUsedCreditLimit(BigDecimal.ZERO);

        customerRepository.save(customer1);
        customerRepository.save(customer2);
        LOGGER.info("Sample customers initialized: {}, {}", customer1.getName(), customer2.getName());
    }

}
