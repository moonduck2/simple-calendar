package moonduck.calendar.simple.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import moonduck.calendar.simple.dao.MeetingDao;
import moonduck.calendar.simple.dao.RecurrenceDao;
import moonduck.calendar.simple.dto.MeetingDto;
import moonduck.calendar.simple.dto.RecurrenceDto;
import moonduck.calendar.simple.entity.Meeting;
import moonduck.calendar.simple.entity.Recurrence;
import moonduck.calendar.simple.exception.MeetingDuplicationException;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MeetingServiceTest {
	@InjectMocks
	private MeetingService service;

	@Mock
	private MeetingDao mockDao;
	
	@Mock
	private RecurrenceDao recurDao;

	@Mock
	private RecurrenceService mockRecurChecker;

	@Test
	public void 겹치지_않을_경우_addOrUpdate테스트() {
		List mockDuplicates = mock(List.class);
		when(mockDuplicates.isEmpty()).thenReturn(true);

		when(mockDao.findAllPossibleDuplicate(any(String.class), any(LocalDate.class), any(LocalDate.class),
				any(LocalTime.class), any(LocalTime.class), any(Integer.class))).thenReturn(mockDuplicates);

		Meeting mockResultEntity = mock(Meeting.class);
		when(mockResultEntity.getId()).thenReturn(1);

		when(mockDao.save(any(Meeting.class))).thenReturn(mockResultEntity);

		when(recurDao.save(any())).thenReturn(mock(Recurrence.class));
		//mockMeeting, mockRecur의 getter의 값들은 의미없는 값임
		RecurrenceDto mockRecur = mock(RecurrenceDto.class);
		when(mockRecur.getDayOfWeek()).thenReturn(1);

		MeetingDto mockMeeting = mock(MeetingDto.class);
		when(mockMeeting.getMeetingRoom()).thenReturn("회의실");
		when(mockMeeting.getStartDate()).thenReturn(LocalDate.now());
		when(mockMeeting.getEndDate()).thenReturn(LocalDate.now());
		when(mockMeeting.getStartTime()).thenReturn(LocalTime.now());
		when(mockMeeting.getEndTime()).thenReturn(LocalTime.now());
		when(mockMeeting.getRecurrence()).thenReturn(mockRecur);
		when(mockMeeting.toEntity(any(Boolean.class))).thenReturn(mock(Meeting.class));

		assertEquals(1, service.addMeeting(mockMeeting));
	}

	@Test(expected = MeetingDuplicationException.class)
	public void 겹칠_경우_addOrUpdate_테스트() {
		List mockDuplicates = mock(List.class);
		when(mockDuplicates.isEmpty()).thenReturn(false);

		when(mockDao.findAllPossibleDuplicate(any(String.class), any(LocalDate.class), any(LocalDate.class),
				any(LocalTime.class), any(LocalTime.class), any(Integer.class))).thenReturn(mockDuplicates);
		
		//mockMeeting, mockRecur의 getter의 값들은 의미없는 값임
		RecurrenceDto mockRecur = mock(RecurrenceDto.class);
		when(mockRecur.getDayOfWeek()).thenReturn(1);

		MeetingDto mockMeeting = mock(MeetingDto.class);
		when(mockMeeting.getMeetingRoom()).thenReturn("회의실");
		when(mockMeeting.getStartDate()).thenReturn(LocalDate.now());
		when(mockMeeting.getEndDate()).thenReturn(LocalDate.now());
		when(mockMeeting.getStartTime()).thenReturn(LocalTime.now());
		when(mockMeeting.getEndTime()).thenReturn(LocalTime.now());
		when(mockMeeting.getRecurrence()).thenReturn(mockRecur);
		when(mockMeeting.toEntity(any(Boolean.class))).thenReturn(mock(Meeting.class));

		service.addMeeting(mockMeeting);
	}

	@Test
	public void 기준일의_모든_회의_가져오기() {
		Meeting abandonedMeeting = mock(Meeting.class);
		when(abandonedMeeting.getRecurrence()).thenReturn(mock(Recurrence.class));
		when(mockRecurChecker.isOccur(any(LocalDate.class), eq(abandonedMeeting), any(Recurrence.class)))
		.thenReturn(false);

		Meeting availableMeeting = mock(Meeting.class);
		when(availableMeeting.getRecurrence()).thenReturn(null);
		when(mockRecurChecker.isOccur(any(LocalDate.class), eq(availableMeeting), any(Recurrence.class)))
		.thenReturn(true);

		List<Meeting> mockMeetings = Arrays.asList(abandonedMeeting, availableMeeting);
		when(mockDao.findMeetingByDate(any(LocalDate.class))).thenReturn(mockMeetings);

		//LocalDate.now는 의미 없는 값임, 회의실을 지정하지 않을 경우 모든 회의실의 일정을 조회함
		assertEquals(Arrays.asList(availableMeeting), 
				service.findMeetingByDate(LocalDate.now(), Collections.emptyList()));
	}
}
