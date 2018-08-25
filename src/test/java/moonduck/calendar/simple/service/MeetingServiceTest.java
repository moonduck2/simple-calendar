package moonduck.calendar.simple.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
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
import moonduck.calendar.simple.entity.Recurrence;
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
		when(dto.getMeetingRoom()).thenReturn(1);
		return dto;
	}
	
	private Optional<Meeting> prepareFirstReserveMeeting(Integer id) {
		Meeting mockMeeting = mock(Meeting.class);
		when(mockMeeting.getId()).thenReturn(id);
		return Optional.of(mockMeeting);
	}

	//meetingDao의 쿼리 수행 결과 중 기준 날짜에 회의가 있는 회의만 뽑아오는지 테스트
	@Test
	public void 기준일에_실제로_회의가_있는_회의만_뽑아와야_함() {
		LocalDate baseDate = LocalDate.of(2018, 7, 17);
		Room room1 = new Room()
				.setId(1)
				.setName("회의실1");

		//기준일이 startDate와 endDate 사이에 있으나 기준일은 회의가 없는 날
		Meeting notOccured = new Meeting()
				.setStartDate(LocalDate.of(2018, 7, 1))
				.setEndDate(LocalDate.of(2018, 7, 31))
				.setRecurrence(new Recurrence().setDayOfWeek(DayOfWeek.SUNDAY.getValue()))
				.setMeetingRoom(room1);
		when(mockRecurChecker.isOccur(eq(baseDate), eq(notOccured))).thenReturn(false);
		
		//기준일이 startDate와 endDate 사이에 있음
		Meeting occured = new Meeting()
				.setStartDate(LocalDate.of(2018, 7, 15))
				.setEndDate(LocalDate.of(2018, 7, 20))
				.setMeetingRoom(room1);
		when(mockRecurChecker.isOccur(eq(baseDate), eq(occured))).thenReturn(true);

		room1.setMeetings(Arrays.asList(notOccured, occured));
		
		when(roomDao.findAllPossibleMeetingsAtDateOfAllRooms(any(LocalDate.class))).thenReturn(Arrays.asList(room1));

		RoomDto expected = new RoomDto()
				.setId(1)
				.setName("회의실1");
		expected.setMeetings(Arrays.asList(occured.toDto()));
		
		assertEquals(Arrays.asList(expected), 
				service.findAllMeetingOfAllRoomsAtDate(baseDate));
	}
	
	@Test
	public void 회의가_없는_회의실도_결과에_포함되어야_함() {
		LocalDate baseDate = LocalDate.of(2018, 7, 17);
		Room room1 = new Room()
				.setId(1)
				.setName("회의실1");
		
		RoomDto expected = room1.toDto();
		
		when(roomDao.findAllPossibleMeetingsAtDateOfAllRooms(any(LocalDate.class)))
			.thenReturn(Arrays.asList(room1));

		assertEquals(Arrays.asList(expected), 
				service.findAllMeetingOfAllRoomsAtDate(baseDate));
	}
	
	@Test
	public void 회의삭제테스트() {
		service.deleteMeeting(12);
		verify(meetingDao).deleteById(eq(12));
	}
}
