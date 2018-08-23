package moonduck.calendar.simple.service.recurrence;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;

import moonduck.calendar.simple.entity.Meeting;
import moonduck.calendar.simple.entity.Recurrence;
import moonduck.calendar.simple.entity.Room;
import moonduck.calendar.simple.enumeration.RecurrenceType;

@RunWith(SpringRunner.class)
public class DayOfWeekRecurrenceHandlerTest {
	@InjectMocks
	private DayOfWeekRecurrenceHandler checker;
	
	@Test
	public void 시작_종료일자를_벗어난_경우_테스트() {
		Meeting mockMeeting = mock(Meeting.class);
		when(mockMeeting.getStartDate()).thenReturn(LocalDate.of(2018, 8, 1));
		when(mockMeeting.getEndDate()).thenReturn(LocalDate.of(2018, 8, 31));
		when(mockMeeting.getStartTime()).thenReturn(LocalTime.of(10, 0));
		when(mockMeeting.getEndTime()).thenReturn(LocalTime.of(11, 30));
		when(mockMeeting.getMeetingRoom()).thenReturn(mock(Room.class));
		
		Recurrence mockRecur = mock(Recurrence.class);
		
		assertFalse(checker.isOccur(LocalDate.of(2018, 7, 1), mockMeeting, mockRecur));
		assertFalse(checker.isOccur(LocalDate.of(2018, 9, 1), mockMeeting, mockRecur));
	}
	
	@Test
	public void 주1회_특정_요일_반복_일정이_있을때_테스트() {
		Meeting mockMeeting = mock(Meeting.class);
		when(mockMeeting.getStartDate()).thenReturn(LocalDate.of(2018, 8, 1));
		when(mockMeeting.getEndDate()).thenReturn(LocalDate.of(2018, 8, 31));
		when(mockMeeting.getStartTime()).thenReturn(LocalTime.of(10, 0));
		when(mockMeeting.getEndTime()).thenReturn(LocalTime.of(11, 30));
		when(mockMeeting.getMeetingRoom()).thenReturn(mock(Room.class));
		
		Recurrence mockRecur = mock(Recurrence.class);
		when(mockRecur.getDayOfWeek()).thenReturn(DayOfWeek.WEDNESDAY.getValue());
		when(mockRecur.getType()).thenReturn(RecurrenceType.ONCE_A_WEEK);
		
		//8월 1일~31일까지 수요일 모두 테스트
		assertTrue(checker.isOccur(LocalDate.of(2018, 8, 1), mockMeeting, mockRecur));
		assertTrue(checker.isOccur(LocalDate.of(2018, 8, 8), mockMeeting, mockRecur));
		assertTrue(checker.isOccur(LocalDate.of(2018, 8, 15), mockMeeting, mockRecur));
		assertTrue(checker.isOccur(LocalDate.of(2018, 8, 22), mockMeeting, mockRecur));
		assertTrue(checker.isOccur(LocalDate.of(2018, 8, 29), mockMeeting, mockRecur));
		
		//8월 1일~31일 이내 수요일이 아닌 요일은 false
		assertFalse(checker.isOccur(LocalDate.of(2018, 8, 3), mockMeeting, mockRecur));
	}
}
