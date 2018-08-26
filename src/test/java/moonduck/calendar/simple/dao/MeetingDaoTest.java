package moonduck.calendar.simple.dao;

import static org.junit.Assert.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import moonduck.calendar.simple.entity.Meeting;
import moonduck.calendar.simple.entity.Recurrence;
import moonduck.calendar.simple.entity.Room;
import moonduck.calendar.simple.enumeration.RecurrenceType;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MeetingDaoTest {
	@Autowired
	private MeetingDao meetingDao;

	@Autowired
	private RoomDao roomDao;

	@Autowired
	private RecurrenceDao recurDao;

	private Room room;
	@Before
	public void init() {
		room = roomDao.save(new Room().setName("회의실1"));
		//07-18(수) ~ 07-25(수), 09:00 ~ 13:00, 수요일 반복
		Meeting meeting1 = prepareMeeting(LocalDate.of(2018, 7, 18), LocalDate.of(2018, 7, 25), 
				LocalTime.of(9, 0), LocalTime.of(13, 0), room);
		Recurrence recurEntity = prepareRecurrence(RecurrenceType.ONCE_A_WEEK, DayOfWeek.WEDNESDAY, null);
		meeting1.setRecurrence(recurEntity);

		//07-23(월) ~ 07-30(월), 10:00 ~ 15:00, 반복 없음
		prepareMeeting(LocalDate.of(2018, 7, 23), LocalDate.of(2018, 7, 30), 
				LocalTime.of(10, 0), LocalTime.of(15, 0), room);

		//07-12(목) ~ 07-26(목), 11:00 ~ 12:00, 목요일 반복 
		prepareMeeting(LocalDate.of(2018, 7, 12), LocalDate.of(2018, 7, 26), 
				LocalTime.of(11, 0), LocalTime.of(12, 0), room);
	}
	
	@Test
	public void 회의가_있을_수_있는_구간_있는_시간을_조회함() {
		List<Meeting> meetings = meetingDao.findAllMeetingInDateAndTime(
				room.getId(), LocalDate.of(2018, 7, 15), LocalDate.of(2018, 7, 25),
				LocalTime.of(11, 0), LocalTime.of(13, 0));
		
		assertEquals(3, meetings.size());
	}
	@Test
	public void 회의가_없는_구간_조회함() {
		List<Meeting> meetings = meetingDao.findAllMeetingInDateAndTime(
				room.getId(), LocalDate.of(2018, 7, 7), LocalDate.of(2018, 7, 11),
				LocalTime.of(11, 0), LocalTime.of(13, 0));
		
		assertTrue(meetings.isEmpty());
	}
	@Test
	public void 회의가_있는_구간이지만_회의가_없는_시간대를_조회함() {
		List<Meeting> meetings = meetingDao.findAllMeetingInDateAndTime(
				room.getId(), LocalDate.of(2018, 7, 15), LocalDate.of(2018, 7, 25),
				LocalTime.of(8, 0), LocalTime.of(9, 0));

		assertTrue(meetings.isEmpty());
	}

	private Meeting prepareMeeting(LocalDate startDate, LocalDate endDate,
			LocalTime startTime, LocalTime endTime, Room room) {
		return meetingDao.save(new Meeting()
				.setStartDate(startDate)
				.setEndDate(endDate)
				.setStartTime(startTime)
				.setEndTime(endTime)
				.setUserName("예약자")
				.setEnabled(true)
				.setModifiedTimeToServerTime()
				.setMeetingRoom(room));
	}

	private Recurrence prepareRecurrence(RecurrenceType type, DayOfWeek dayOfWeek, Integer count) {
		return recurDao.save(new Recurrence()
				.setType(type)
				.setDayOfWeek(dayOfWeek.getValue())
				.setCount(count));
	}
}
