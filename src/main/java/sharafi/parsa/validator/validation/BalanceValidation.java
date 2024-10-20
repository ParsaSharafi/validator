package sharafi.parsa.validator.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = BalanceValidator.class)
public @interface BalanceValidation {

	String message() default "account balance must be lower than account limit";
	
	Class<?>[] groups() default { };
	
	Class<? extends Payload>[] payload() default { };
	
}
