package moonduck.calendar.simple.validator;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import moonduck.calendar.simple.entity.Meeting;
import moonduck.calendar.simple.validator.annotation.ValidMeeting;

public class MeetingValidator implements ConstraintValidator<ValidMeeting, Meeting> {

	@Override
	public boolean isValid(Meeting value, ConstraintValidatorContext context) {
		LocalDate startDate = value.getStartDate();
		LocalDate endDate = value.getEndDate();
		if (!ValidatorUtils.isValidDateRange(startDate, endDate)) {
			return false;
		}
		
		if (!ValidatorUtils.isValidTimeRange(value.getStartTime(), value.getEndTime())) {
			return false;
		}
		
		//반복이 없는데 start와 end가 없으면 잘못된 요청으로 간주
		if (CollectionUtils.isEmpty(value.getRecurrence()) && !startDate.isEqual(endDate)) {
			return false;
		}
		return !StringUtils.isEmpty(value.getMeetingRoom()) || !"".equals(value.getMeetingRoom().trim());
	}

}
