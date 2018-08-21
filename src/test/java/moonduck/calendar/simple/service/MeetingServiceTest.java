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
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import moonduck.calendar.simple.dao.MeetingDao;
import moonduck.calendar.simple.entity.Meeting;
import moonduck.calendar.simple.exception.MeetingDuplicationException;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MeetingServiceTest {
	@InjectMocks
	private MeetingService service;
	
	@Spy
	private MeetingDao mockDao;
	
	@Test
	public void 겹치지_않을_경우_addOrUpdate테스트() {
		fail();
	}
	
	@Test(expected = MeetingDuplicationException.class)
	public void 겹칠_경우_addOrUpdate_테스트() {
		List mockDuplicates = mock(List.class);
		when(mockDuplicates.isEmpty()).thenReturn(false);
		
		when(mockDao.findAllPossibleDuplicate(any(String.class), any(LocalDate.class), any(LocalDate.class),
				any(LocalTime.class), any(LocalTime.class))).thenReturn(mock(List.class));
		Meeting mockMeeting = mock(Meeting.class);
		when(mockMeeting.getMeetingRoom()).thenReturn("회의실");
		when(mockMeeting.getStart()).thenReturn(LocalDate.now());
		when(mockMeeting.getEnd()).thenReturn(LocalDate.now());
		when(mockMeeting.getStartTime()).thenReturn(LocalTime.now());
		when(mockMeeting.getEndTime()).thenReturn(LocalTime.now());
		
		service.addOrUpdateMeeting(mockMeeting);
	}
}
