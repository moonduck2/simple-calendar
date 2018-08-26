package moonduck.calendar.simple.validator;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import moonduck.calendar.simple.dto.MeetingDto;
import moonduck.calendar.simple.validator.annotation.ValidMeeting;

public class MeetingValidator implements ConstraintValidator<ValidMeeting, MeetingDto> {
	@Override
	public boolean isValid(MeetingDto value, ConstraintValidatorContext context) {
		LocalDate startDate = value.getStartDate();
		if (startDate == null) {
			return false;
		}
		LocalDate endDate = value.getEndDate();

		if (value.getRecurrence() == null) {
			//반복이 없는데 end가 없으면 잘못된 요청으로 간주
			if (endDate == null) {
				return false;
			}
		} 
		//endDate는 startDate보다 미래여야 함
		if (endDate != null && endDate.isBefore(startDate)) {
			return false;
		}
		
		LocalTime startTime = value.getStartTime();
		LocalTime endTime = value.getEndTime();
		if (!ValidatorUtils.isValidTimeRange(startTime, endTime)) {
			return false;
		}

		if (!ValidatorUtils.isValidMeetingMinutes(startTime.getMinute()) ||
				!ValidatorUtils.isValidMeetingMinutes(endTime.getMinute())) {
			return false;
		}
		return true;
	}

}
