package sharafi.parsa.validator.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import sharafi.parsa.validator.model.AccountType;

public class AccountTypeValidator implements ConstraintValidator<AccountTypeValidation, String> {

	private List<String> accountTypes = null;
	
	@Override
	public boolean isValid(String accountType, ConstraintValidatorContext context) {
		
		return accountTypes.contains(Optional.ofNullable(accountType).isPresent() ? accountType.toUpperCase().replaceAll("\\s", "_") : "null");
	}
	
	@Override
	public void initialize(AccountTypeValidation constraintAnnotation) {
		
		accountTypes = new ArrayList<>();
		for (AccountType accountType: AccountType.values())
			accountTypes.add(accountType.toString());
	}
}
