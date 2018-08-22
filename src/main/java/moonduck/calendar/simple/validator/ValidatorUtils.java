package moonduck.calendar.simple.validator;

import java.time.LocalDate;
import java.time.LocalTime;

public final class ValidatorUtils {
	private ValidatorUtils() {}
	
	public static boolean isValidDateRange(LocalDate start, LocalDate end) {
		if (start == null) {
			return false;
		}
		if (end == null) {
			return false;
		}
		return start.isBefore(end) || start.isEqual(end); //1회성 예약의 경우 start와 end가 같을 수 있음
	}
	//30분 단위만 유효
	public static boolean isValidMeetingMinutes(int minutes) {
		return minutes == 0 || minutes == 30;
	}
	
	public static boolean isValidTimeRange(LocalTime startTime, LocalTime endTime) {
		if (startTime == null) {
			return false;
		}
		if (endTime == null) {
			return false;
		}

		//시간은 반드시 endTime이 startTime보다 커야 함
		return startTime.isBefore(endTime);
	}
}
