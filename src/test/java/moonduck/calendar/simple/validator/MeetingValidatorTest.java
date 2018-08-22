package moonduck.calendar.simple.validator;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.Test;

import moonduck.calendar.simple.entity.Meeting;
import moonduck.calendar.simple.entity.Recurrence;
import moonduck.calendar.simple.enumeration.RecurrenceType;

public class MeetingValidatorTest {

	@Test
	public void start가_null이면_invalid() {
		Meeting invalidMeeting = new Meeting()
			.setEndDate(LocalDate.now())
			.setStartTime(LocalTime.of(0, 0))
			.setEndTime(LocalTime.of(1, 0))
			.setMeetingRoom("회의실");
		
		assertFalse(new MeetingValidator().isValid(invalidMeeting, null));
	}
	
	@Test
	public void 반복없이_end가_null이면_invalid() {
		Meeting invalidMeeting = new Meeting()
			.setStartDate(LocalDate.now())
			.setStartTime(LocalTime.of(0, 0))
			.setEndTime(LocalTime.of(1, 0))
			.setMeetingRoom("회의실");
		
		assertFalse(new MeetingValidator().isValid(invalidMeeting, null));
	}
	
	@Test
	public void startTime이_null이면_invalid() {
		Meeting invalidMeeting = new Meeting()
			.setStartDate(LocalDate.of(2018, 1, 1))
			.setEndDate(LocalDate.of(2018, 2, 1))
			.setEndTime(LocalTime.of(1, 0))
			.setMeetingRoom("회의실");
		
		assertFalse(new MeetingValidator().isValid(invalidMeeting, null));
	}
	
	@Test
	public void endTime이_null이면_invalid() {
		Meeting invalidMeeting = new Meeting()
			.setStartDate(LocalDate.of(2018, 1, 1))
			.setEndDate(LocalDate.of(2018, 2, 1))
			.setStartTime(LocalTime.of(1, 0))
			.setMeetingRoom("회의실");
		
		assertFalse(new MeetingValidator().isValid(invalidMeeting, null));
	}
	
	@Test
	public void 회의실이_null이면_invalid() {
		Meeting invalidMeeting = new Meeting()
			.setStartDate(LocalDate.of(2018, 1, 1))
			.setEndDate(LocalDate.of(2018, 2, 1))
			.setStartTime(LocalTime.of(0, 0))
			.setEndTime(LocalTime.of(1, 0));
		
		assertFalse(new MeetingValidator().isValid(invalidMeeting, null));
	}
	
	@Test
	public void 회의실이_빈_문자열이면_invalid() {
		Meeting invalidMeeting = new Meeting()
			.setStartDate(LocalDate.of(2018, 1, 1))
			.setEndDate(LocalDate.of(2018, 2, 1))
			.setStartTime(LocalTime.of(0, 0))
			.setEndTime(LocalTime.of(1, 0))
			.setMeetingRoom("");
		
		assertFalse(new MeetingValidator().isValid(invalidMeeting, null));
	}
	
	@Test
	public void 회의실이_공백_문자열이면_invalid() {
		Meeting invalidMeeting = new Meeting()
			.setStartDate(LocalDate.of(2018, 1, 1))
			.setEndDate(LocalDate.of(2018, 2, 1))
			.setStartTime(LocalTime.of(0, 0))
			.setEndTime(LocalTime.of(1, 0))
			.setMeetingRoom("  ");
		
		assertFalse(new MeetingValidator().isValid(invalidMeeting, null));
	}
	
	@Test
	public void startDate가_endDate보다_미래면_invalid() {
		Meeting invalidMeeting = new Meeting()
			.setStartDate(LocalDate.of(2018, 2, 1))
			.setEndDate(LocalDate.of(2018, 1, 1))
			.setStartTime(LocalTime.of(0, 0))
			.setEndTime(LocalTime.of(1, 0))
			.setMeetingRoom("회의실");
		
		assertFalse(new MeetingValidator().isValid(invalidMeeting, null));
	}
	
	@Test
	public void startTime이_endTime보다_미래면_invalid() {
		Meeting invalidMeeting = new Meeting()
			.setStartDate(LocalDate.of(2018, 1, 1))
			.setEndDate(LocalDate.of(2018, 2, 1))
			.setStartTime(LocalTime.of(1, 0))
			.setEndTime(LocalTime.of(0, 0))
			.setMeetingRoom("회의실");
		
		assertFalse(new MeetingValidator().isValid(invalidMeeting, null));
	}
	
	@Test
	public void startTime과_endTime이_같으면_invalid() {
		Meeting invalidMeeting = new Meeting()
			.setStartDate(LocalDate.of(2018, 1, 1))
			.setEndDate(LocalDate.of(2018, 2, 1))
			.setStartTime(LocalTime.of(1, 0))
			.setEndTime(LocalTime.of(1, 0))
			.setMeetingRoom("회의실");
		
		assertFalse(new MeetingValidator().isValid(invalidMeeting, null));
	}
	
	@Test
	public void startTime이_30분_단위가_아니면_invalid() {
		Meeting invalidMeeting = new Meeting()
			.setStartDate(LocalDate.of(2018, 1, 1))
			.setEndDate(LocalDate.of(2018, 2, 1))
			.setStartTime(LocalTime.of(0, 10))
			.setEndTime(LocalTime.of(0, 30))
			.setMeetingRoom("회의실");
		
		assertFalse(new MeetingValidator().isValid(invalidMeeting, null));
	}
	
	@Test
	public void endTime이_30분_단위가_아니면_invalid() {
		Meeting invalidMeeting = new Meeting()
			.setStartDate(LocalDate.of(2018, 1, 1))
			.setEndDate(LocalDate.of(2018, 2, 1))
			.setStartTime(LocalTime.of(0, 0))
			.setEndTime(LocalTime.of(0, 10))
			.setMeetingRoom("회의실");
		
		assertFalse(new MeetingValidator().isValid(invalidMeeting, null));
	}
}
