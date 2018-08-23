package moonduck.calendar.simple.validator;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.Test;

import moonduck.calendar.simple.dto.MeetingDto;
import moonduck.calendar.simple.dto.RoomDto;

public class MeetingValidatorTest {

	@Test
	public void start가_null이면_invalid() {
		MeetingDto invalidMeeting = new MeetingDto()
			.setEndDate(LocalDate.now())
			.setStartTime(LocalTime.of(0, 0))
			.setEndTime(LocalTime.of(1, 0))
			.setMeetingRoom(new RoomDto().setName("회의실"));
		
		assertFalse(new MeetingValidator().isValid(invalidMeeting, null));
	}
	
	@Test
	public void 반복없이_end가_null이면_invalid() {
		MeetingDto invalidMeeting = new MeetingDto()
			.setStartDate(LocalDate.now())
			.setStartTime(LocalTime.of(0, 0))
			.setEndTime(LocalTime.of(1, 0))
			.setMeetingRoom(new RoomDto().setName("회의실"));
		
		assertFalse(new MeetingValidator().isValid(invalidMeeting, null));
	}
	
	@Test
	public void startTime이_null이면_invalid() {
		MeetingDto invalidMeeting = new MeetingDto()
			.setStartDate(LocalDate.of(2018, 1, 1))
			.setEndDate(LocalDate.of(2018, 2, 1))
			.setEndTime(LocalTime.of(1, 0))
			.setMeetingRoom(new RoomDto().setName("회의실"));
		
		assertFalse(new MeetingValidator().isValid(invalidMeeting, null));
	}
	
	@Test
	public void endTime이_null이면_invalid() {
		MeetingDto invalidMeeting = new MeetingDto()
			.setStartDate(LocalDate.of(2018, 1, 1))
			.setEndDate(LocalDate.of(2018, 2, 1))
			.setStartTime(LocalTime.of(1, 0))
			.setMeetingRoom(new RoomDto().setName("회의실"));
		
		assertFalse(new MeetingValidator().isValid(invalidMeeting, null));
	}
	
	@Test
	public void 회의실이_null이면_invalid() {
		MeetingDto invalidMeeting = new MeetingDto()
			.setStartDate(LocalDate.of(2018, 1, 1))
			.setEndDate(LocalDate.of(2018, 2, 1))
			.setStartTime(LocalTime.of(0, 0))
			.setEndTime(LocalTime.of(1, 0));
		
		assertFalse(new MeetingValidator().isValid(invalidMeeting, null));
	}
	
	@Test
	public void startDate가_endDate보다_미래면_invalid() {
		MeetingDto invalidMeeting = new MeetingDto()
			.setStartDate(LocalDate.of(2018, 2, 1))
			.setEndDate(LocalDate.of(2018, 1, 1))
			.setStartTime(LocalTime.of(0, 0))
			.setEndTime(LocalTime.of(1, 0))
			.setMeetingRoom(new RoomDto().setName("회의실"));
		
		assertFalse(new MeetingValidator().isValid(invalidMeeting, null));
	}
	
	@Test
	public void startTime이_endTime보다_미래면_invalid() {
		MeetingDto invalidMeeting = new MeetingDto()
			.setStartDate(LocalDate.of(2018, 1, 1))
			.setEndDate(LocalDate.of(2018, 2, 1))
			.setStartTime(LocalTime.of(1, 0))
			.setEndTime(LocalTime.of(0, 0))
			.setMeetingRoom(new RoomDto().setName("회의실"));
		
		assertFalse(new MeetingValidator().isValid(invalidMeeting, null));
	}
	
	@Test
	public void startTime과_endTime이_같으면_invalid() {
		MeetingDto invalidMeeting = new MeetingDto()
			.setStartDate(LocalDate.of(2018, 1, 1))
			.setEndDate(LocalDate.of(2018, 2, 1))
			.setStartTime(LocalTime.of(1, 0))
			.setEndTime(LocalTime.of(1, 0))
			.setMeetingRoom(new RoomDto().setName("회의실"));
		
		assertFalse(new MeetingValidator().isValid(invalidMeeting, null));
	}
	
	@Test
	public void startTime이_30분_단위가_아니면_invalid() {
		MeetingDto invalidMeeting = new MeetingDto()
			.setStartDate(LocalDate.of(2018, 1, 1))
			.setEndDate(LocalDate.of(2018, 2, 1))
			.setStartTime(LocalTime.of(0, 10))
			.setEndTime(LocalTime.of(0, 30))
			.setMeetingRoom(new RoomDto().setName("회의실"));
		
		assertFalse(new MeetingValidator().isValid(invalidMeeting, null));
	}
	
	@Test
	public void endTime이_30분_단위가_아니면_invalid() {
		MeetingDto invalidMeeting = new MeetingDto()
			.setStartDate(LocalDate.of(2018, 1, 1))
			.setEndDate(LocalDate.of(2018, 2, 1))
			.setStartTime(LocalTime.of(0, 0))
			.setEndTime(LocalTime.of(0, 10))
			.setMeetingRoom(new RoomDto().setName("회의실"));
		
		assertFalse(new MeetingValidator().isValid(invalidMeeting, null));
	}
}
