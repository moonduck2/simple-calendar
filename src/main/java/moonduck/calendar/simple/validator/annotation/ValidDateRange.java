package moonduck.calendar.simple.validator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import moonduck.calendar.simple.validator.DateRangeValidator;

@Constraint(validatedBy = DateRangeValidator.class)
@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDateRange {
	String message() default "Invalid date range";
	Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
