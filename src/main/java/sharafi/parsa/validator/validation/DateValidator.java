package sharafi.parsa.validator.validation;

import org.apache.commons.validator.GenericValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DateValidator implements ConstraintValidator<DateValidation, String> {

	@Override
	public boolean isValid(String date, ConstraintValidatorContext context) {
		return GenericValidator.isDate(date, "yyyy-MM-dd", false);
	}
}
