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
@Constraint(validatedBy = AccountTypeValidator.class)
public @interface AccountTypeValidation {

	String message() default "invalid value for account type";
	
	Class<?>[] groups() default { };
	
	Class<? extends Payload>[] payload() default { };
}
