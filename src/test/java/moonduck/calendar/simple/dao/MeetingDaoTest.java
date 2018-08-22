package moonduck.calendar.simple.dao;

import static org.junit.Assert.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import moonduck.calendar.simple.entity.Meeting;
import moonduck.calendar.simple.entity.Recurrence;
import moonduck.calendar.simple.enumeration.RecurrenceType;

//TODO: 필요없는 테스트 제거(findDuplicatedPossible이라던지...)
@RunWith(SpringRunner.class)
@DataJpaTest
public class MeetingDaoTest {
	@Autowired
	private MeetingDao dao;
	
	@Autowired
	private RecurrenceDao recurDao;

	@Before
	public void init() {
		//07-01 ~ 08-01, 09:00 ~ 11:00, 수요일 반복
		Recurrence recurEntity = recurDao.save(new Recurrence()
				.setType(RecurrenceType.ONCE_A_WEEK)
				.setDayOfWeek(DayOfWeek.WEDNESDAY.getValue()));
				
		dao.save(new Meeting()
				.setMeetingRoom("회의실1")
				.setStartDate(LocalDate.of(2018, 7, 1))
				.setEndDate(LocalDate.of(2018, 9, 1))
				.setStartTime(LocalTime.of(9, 0))
				.setEndTime(LocalTime.of(11, 0))
				.setRecurrence(recurEntity));
	}

	@Test
	public void 날짜도_시간도_겹치지_않음() {
		//01-01 ~ 05-01, 14:00 ~ 15:00
		List<Meeting> possibleDuplicate = dao.findAllPossibleDuplicate("회의실1",
				LocalDate.of(2018, 1, 1), LocalDate.of(2018, 5, 1), LocalTime.of(14, 0), LocalTime.of(15, 0),
				DayOfWeek.WEDNESDAY.getValue());

		assertEquals(0, possibleDuplicate.size());
	}

	@Test
	public void 날짜만_겹침() {
		//07-31 ~ 09-01, 14:00 ~ 15:00
		List<Meeting> possibleDuplicate = dao.findAllPossibleDuplicate("회의실1",
				LocalDate.of(2018, 7, 31), LocalDate.of(2018, 9, 1), LocalTime.of(14, 0), LocalTime.of(15, 0),
				DayOfWeek.WEDNESDAY.getValue());

		assertEquals(0, possibleDuplicate.size());
	}
	
	@Test
	public void 시간만_겹침() {
		//01-01 ~ 05-01, 09:00 ~ 10:00
		List<Meeting> possibleDuplicate = dao.findAllPossibleDuplicate("회의실1",
				LocalDate.of(2018, 1, 1), LocalDate.of(2018, 5, 1), LocalTime.of(9, 0), LocalTime.of(10, 0),
				DayOfWeek.WEDNESDAY.getValue());
		assertEquals(0, possibleDuplicate.size());
	}
	
	@Test
	public void 날짜_시간_모두_겹침() {
		List<Meeting> possibleDuplicate = dao.findAllPossibleDuplicate("회의실1",
				LocalDate.of(2018, 7, 31), LocalDate.of(2018, 9, 1), LocalTime.of(9, 0), LocalTime.of(10, 0),
				DayOfWeek.WEDNESDAY.getValue());
		assertEquals(1, possibleDuplicate.size());
	}
	
	@Test
	public void 기준_날짜에_해당하는_회의_조회() {
		List<Meeting> meetings = dao.findMeetingByDate(LocalDate.of(2018, 8, 1));
		assertEquals(1, meetings.size());
		
		meetings = dao.findMeetingByDate(LocalDate.of(2018, 1, 1));
		assertEquals(0, meetings.size());
		
		meetings = dao.findMeetingByDate(LocalDate.of(2019, 1, 1));
		assertEquals(0, meetings.size());
	}
}
