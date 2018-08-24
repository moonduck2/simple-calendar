package moonduck.calendar.simple.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import moonduck.calendar.simple.dao.MeetingDao;
import moonduck.calendar.simple.dao.RecurrenceDao;
import moonduck.calendar.simple.dao.RoomDao;
import moonduck.calendar.simple.dto.MeetingDto;
import moonduck.calendar.simple.dto.RoomDto;
import moonduck.calendar.simple.entity.Meeting;
import moonduck.calendar.simple.entity.Room;
import moonduck.calendar.simple.exception.MeetingDuplicationException;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MeetingServiceTest {
	@InjectMocks
	private MeetingService service;

	@Mock
	private MeetingDao meetingDao;
	
	@Mock
	private RoomDao roomDao;

	@Mock
	private RecurrenceDao recurDao;

	@Mock
	private RecurrenceService mockRecurChecker;

	@Mock
	private CalendarUtilService util;

	@Test
	public void 선점에_성공했을_경우_addOrUpdate테스트() {
		int expectedId = 1;
		Meeting savedEntity = prepareSavedEntity(expectedId); //id가 1인 회의 선점 예정
		
		Optional<Meeting> occupiedMeeting = prepareFirstReserveMeeting(expectedId);
		when(mockRecurChecker.getFirstInDuplicatedMeeting(eq(savedEntity), any()))
			.thenReturn(occupiedMeeting);
		
		MeetingDto dto = prepareArbitraryMeetingDto();
		when(dto.toEntity(any(Boolean.class))).thenReturn(savedEntity);
		
		assertEquals(expectedId, service.addMeeting(dto));
	}

	@Test(expected = MeetingDuplicationException.class)
	public void 선점에_실패했을_경우_addOrUpdate_테스트() {
		Meeting mockResultEntity = prepareSavedEntity(1); //id가 1인 회의 선점시도 예정

		Optional<Meeting> occupiedMeeting = prepareFirstReserveMeeting(2);
		when(mockRecurChecker.getFirstInDuplicatedMeeting(eq(mockResultEntity), any()))
			.thenReturn(occupiedMeeting); //save한 entity가 아닌 다른 회의(id=2)가 처음 예약한 상황
		
		MeetingDto dto = prepareArbitraryMeetingDto();
		when(dto.toEntity(any(Boolean.class))).thenReturn(mockResultEntity);
		
		service.addMeeting(dto);
	}
	
	private Meeting prepareSavedEntity(Integer id) {
		Meeting mockResultEntity = mock(Meeting.class); //meetingDao의 save결과 mock
		when(mockResultEntity.getId()).thenReturn(id);
		when(meetingDao.save(any())).thenReturn(mockResultEntity);
		return mockResultEntity;
	}
	private MeetingDto prepareArbitraryMeetingDto() {
		MeetingDto dto = mock(MeetingDto.class);
		when(dto.getStartDate()).thenReturn(LocalDate.MAX);
		when(dto.getEndDate()).thenReturn(LocalDate.MIN);
		when(dto.getStartTime()).thenReturn(LocalTime.MAX);
		when(dto.getEndTime()).thenReturn(LocalTime.MIN);
		when(dto.getMeetingRoom()).thenReturn(mock(RoomDto.class));
		return dto;
	}
	
	private Optional<Meeting> prepareFirstReserveMeeting(Integer id) {
		Meeting mockMeeting = mock(Meeting.class);
		when(mockMeeting.getId()).thenReturn(id);
		return Optional.of(mockMeeting);
	}

	//meetingDao의 쿼리 수행 결과를 그대로 리턴하는지 테스트
	@Test
	public void 기준일의_모든_회의_가져오기() {
		
		RoomDto mockRoom = mock(RoomDto.class);
		Room mockRoomEntity = mock(Room.class);
		when(mockRoomEntity.toDto()).thenReturn(mockRoom);
		
		List<Room> mockMeetings = Arrays.asList(mockRoomEntity);
		when(roomDao.findMeetingsEachRooms(any(LocalDate.class), any(Integer.class))).thenReturn(mockMeetings);

		//LocalDate.now는 의미 없는 값임, 회의실을 지정하지 않을 경우 모든 회의실의 일정을 조회함
		assertEquals(Arrays.asList(mockRoom), 
				service.findMeetingByDate(LocalDate.now(), Collections.emptyList()));
	}
	
	@Test
	public void 회의삭제테스트() {
		service.deleteMeeting(12);
		verify(meetingDao).deleteById(eq(12));
	}
}
