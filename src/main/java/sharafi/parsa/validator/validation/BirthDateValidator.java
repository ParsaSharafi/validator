package sharafi.parsa.validator.validation;

import java.time.LocalDate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BirthDateValidator implements ConstraintValidator<BirthDateValidation, LocalDate> {

	@Override
	public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
		
		return date.isBefore(LocalDate.of(1995, 1, 1));
	}
}
