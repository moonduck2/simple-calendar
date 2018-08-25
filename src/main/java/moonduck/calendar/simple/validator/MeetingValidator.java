package moonduck.calendar.simple.validator;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import moonduck.calendar.simple.dto.MeetingDto;
import moonduck.calendar.simple.dto.RecurrenceDto;
import moonduck.calendar.simple.util.TemporalCalculator;
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
		} else if (endDate != null) {
			RecurrenceDto recurrence = value.getRecurrence();
			//횟수가 없으면 endDate를 따름
			if (recurrence.getCount() != null) {
				//반복횟수와 endDate가 맞지 않으면 잘못된 요청
				LocalDate minEndDate = TemporalCalculator.calcLastDate(startDate, DayOfWeek.of(recurrence.getDayOfWeek()), recurrence.getCount());
				if (minEndDate.isAfter(endDate)) {
					return false;
				}
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
