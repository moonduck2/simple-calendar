package moonduck.calendar.simple.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;

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

	@Mock
	private CalendarUtilService util;

	@Test
	public void 선점에_성공했을_경우_addOrUpdate테스트() {
		int expectedId = 1;
		Meeting mockResultEntity = mock(Meeting.class); //meetingDao의 save결과 mock
		when(mockResultEntity.getId()).thenReturn(expectedId); //addMeeting의 결과는 mockResultEntity의 ID인 1이 나와야 한다
		when(mockDao.save(any())).thenReturn(mockResultEntity);

		//선점 성공을 의미하는 mock 설정(findDuplicatedPeriod은 중복된 시간대에 저장된 시간 순서의 자료구조를 리턴하므로)
		SortedSet<Meeting> mockSortedResult = mock(SortedSet.class);
		when(mockSortedResult.first()).thenReturn(mockResultEntity);
		when(util.findDuplicatedPeriod(any(), any(), any())).thenReturn(mockSortedResult);

		//mockMeeting, mockRecur의 getter의 값들은 의미없는 값. 로직 내부에서 호출하므로 예외가 발생하지 않도록 임의의 값을 설정
		RecurrenceDto mockRecur = mock(RecurrenceDto.class);
		when(mockRecur.getDayOfWeek()).thenReturn(5);

		MeetingDto mockMeeting = mock(MeetingDto.class);
		when(mockMeeting.getStartDate()).thenReturn(LocalDate.now());
		when(mockMeeting.getRecurrence()).thenReturn(mockRecur);
		when(mockMeeting.toEntity(any(Boolean.class))).thenReturn(mock(Meeting.class));

		assertEquals(expectedId, service.addMeeting(mockMeeting));
	}

	@Test(expected = MeetingDuplicationException.class)
	public void 선점에_실패했을_경우_addOrUpdate_테스트() {
		Meeting mockResultEntity = mock(Meeting.class); //meetingDao의 save결과 mock
		when(mockResultEntity.getId()).thenReturn(1);
		when(mockDao.save(any())).thenReturn(mockResultEntity);

		//id가 1인 회의가 선점에 실패하여 id가 3인 회의가 선점한 경우
		Meeting firstReservedMeeting = mock(Meeting.class);
		when(firstReservedMeeting.getId()).thenReturn(3);
		SortedSet<Meeting> mockSortedResult = mock(SortedSet.class);
		when(mockSortedResult.first()).thenReturn(firstReservedMeeting);
		when(util.findDuplicatedPeriod(any(), any(), any())).thenReturn(mockSortedResult);

		//mockMeeting, mockRecur의 getter의 값들은 의미없는 값. 로직 내부에서 호출하므로 예외가 발생하지 않도록 임의의 값을 설정
		RecurrenceDto mockRecur = mock(RecurrenceDto.class);
		when(mockRecur.getDayOfWeek()).thenReturn(5);

		MeetingDto mockMeeting = mock(MeetingDto.class);
		when(mockMeeting.getStartDate()).thenReturn(LocalDate.now());
		when(mockMeeting.getRecurrence()).thenReturn(mockRecur);
		when(mockMeeting.toEntity(any(Boolean.class))).thenReturn(mock(Meeting.class));
		
		try {
			service.addMeeting(mockMeeting);
			verify(mockDao).delete(eq(mockResultEntity));
		} catch (MeetingDuplicationException ex) {
			throw ex;
		}
	}

	//mockDao의 쿼리 수행 결과를 그대로 리턴하는지 테스트
	@Test
	public void 기준일의_모든_회의_가져오기() {
		List<Meeting> mockMeetings = mock(List.class);
		when(mockDao.findMeetingByDate(any(LocalDate.class), any(Integer.class))).thenReturn(mockMeetings);

		//LocalDate.now는 의미 없는 값임, 회의실을 지정하지 않을 경우 모든 회의실의 일정을 조회함
		assertEquals(mockMeetings, 
				service.findMeetingByDate(LocalDate.now(), Collections.emptyList()));
	}
}
