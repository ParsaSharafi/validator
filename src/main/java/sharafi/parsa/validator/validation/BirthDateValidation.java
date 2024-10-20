package sharafi.parsa.validator.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = BirthDateValidator.class)
public @interface BirthDateValidation {

	String message() default "customer birth date must be before 1995";
	
	Class<?>[] groups() default { };
	
	Class<? extends Payload>[] payload() default { };
}
