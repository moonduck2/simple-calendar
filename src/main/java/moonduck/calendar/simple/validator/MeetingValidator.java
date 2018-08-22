package moonduck.calendar.simple.validator;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.util.StringUtils;

import moonduck.calendar.simple.entity.Meeting;
import moonduck.calendar.simple.validator.annotation.ValidMeeting;

public class MeetingValidator implements ConstraintValidator<ValidMeeting, Meeting> {
	@Override
	public boolean isValid(Meeting value, ConstraintValidatorContext context) {
		LocalDate startDate = value.getStartDate();
		if (startDate == null) {
			return false;
		}
		LocalDate endDate = value.getEndDate();

		//반복이 없는데 start와 end가 같지 않으면 잘못된 요청으로 간주
		if (value.getRecurrence() == null && !startDate.equals(endDate)) {
			return false;
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
		return !StringUtils.isEmpty(value.getMeetingRoom()) || !"".equals(value.getMeetingRoom().trim());
	}

}
