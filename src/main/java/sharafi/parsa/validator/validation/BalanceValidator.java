package sharafi.parsa.validator.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import sharafi.parsa.validator.model.Account;

public class BalanceValidator implements ConstraintValidator<BalanceValidation, Account> {

	@Override
	public boolean isValid(Account account, ConstraintValidatorContext context) {
		
		return account.getAccountBalance() <= account.getAccountLimit();
	}
}
