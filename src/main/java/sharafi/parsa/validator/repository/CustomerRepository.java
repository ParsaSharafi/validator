package sharafi.parsa.validator.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sharafi.parsa.validator.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
	
	@Query(value = "SELECT * FROM customer JOIN account ON customer_id = account_customer_id WHERE account_balance > 1000", nativeQuery = true)
	List<Customer> findHighBalanceCustomers();
}
