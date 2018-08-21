package moonduck.calendar.simple.validator;

import java.time.LocalDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import moonduck.calendar.simple.type.HasDateRange;
import moonduck.calendar.simple.validator.annotation.ValidDateRange;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, HasDateRange> {

	@Override
	public boolean isValid(HasDateRange value, ConstraintValidatorContext context) {
		LocalDate start = value.getStartDate();
		if (start == null) {
			return false;
		}
		LocalDate end = value.getEndDate();
		if (end == null) {
			return false;
		}
		return start.isBefore(end) || start.isEqual(end);
	}

}
