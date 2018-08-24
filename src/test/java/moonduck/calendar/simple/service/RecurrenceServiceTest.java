package moonduck.calendar.simple.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import moonduck.calendar.simple.entity.Meeting;
import moonduck.calendar.simple.entity.Recurrence;
import moonduck.calendar.simple.enumeration.RecurrenceType;
import moonduck.calendar.simple.service.recurrence.DayOfWeekRecurrenceHandler;

@RunWith(SpringRunner.class)
public class RecurrenceServiceTest {
	private RecurrenceService recurService;

	@Mock
	private DayOfWeekRecurrenceHandler onceAweekRecurHandler;


	@Before
	public void init() {
		when(onceAweekRecurHandler.getAvailableType()).thenReturn(RecurrenceType.ONCE_A_WEEK);
		recurService = new RecurrenceService(Arrays.asList(onceAweekRecurHandler));
	}
	@Test
	public void 주어진_리스트_중에_겹치는_회의_중_제일_먼저_예약된_회의_찾기_테스트() {
		//firstMeeting, secondMeeting과 baseMeeting은 8/15 회의가 중복됨
		//firstMeeting이 먼저 예약된 상황(baseMeeting제외)
		Meeting firstMeeting = prepareMock(RecurrenceType.ONCE_A_WEEK,
				LocalDate.of(2018, 8, 1), LocalDate.of(2018, 8, 29),
				LocalTime.of(10, 0), LocalTime.of(14, 0), 1);

		Meeting secondMeeting = prepareMock(RecurrenceType.ONCE_A_WEEK,
				LocalDate.of(2018, 8, 15), LocalDate.of(2018, 9, 5),
				LocalTime.of(12, 0), LocalTime.of(16, 0), 2);

		Meeting baseMeeting = prepareMock(RecurrenceType.ONCE_A_WEEK,
				LocalDate.of(2018, 8, 15), LocalDate.of(2018, 8, 15),
				LocalTime.of(11, 0), LocalTime.of(13, 0), 0);

		//8월 15일에 위 세 회의 모두 회의가 있으므로
		when(onceAweekRecurHandler.isOccur(eq(LocalDate.of(2018, 8, 15)), any(Meeting.class), any(Recurrence.class)))
			.thenReturn(true);
		//다음 회의 발생일을 더이상 찾지 않기 위해 임의로 MAX 값을 넣음 + NPE방지
		when(onceAweekRecurHandler.nextOccur(any(LocalDate.class), any())).thenReturn(LocalDate.MAX);

		List<Meeting> comparedMeetings = Arrays.asList(firstMeeting, secondMeeting);

		Optional<Meeting> duplicatedMeetings = recurService.getFirstInDuplicatedMeeting(
				baseMeeting, comparedMeetings);

		assertTrue(duplicatedMeetings.isPresent());
		assertEquals(firstMeeting, duplicatedMeetings.get());
	}

	private Meeting prepareMock(RecurrenceType type, LocalDate startDate, LocalDate endDate,
			LocalTime startTime, LocalTime endTime, long modifiedTime) {
		Meeting mockMeeting = mock(Meeting.class);
		when(mockMeeting.getStartDate()).thenReturn(startDate);
		when(mockMeeting.getEndDate()).thenReturn(endDate);
		when(mockMeeting.getStartTime()).thenReturn(startTime);
		when(mockMeeting.getEndTime()).thenReturn(endTime);
		Recurrence mockRecur = mock(Recurrence.class);
		when(mockRecur.getType()).thenReturn(type);
		when(mockMeeting.getRecurrence()).thenReturn(mockRecur);
		return mockMeeting;
	}
}
