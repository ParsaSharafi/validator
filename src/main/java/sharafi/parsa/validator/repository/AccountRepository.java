package sharafi.parsa.validator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sharafi.parsa.validator.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
	
}
