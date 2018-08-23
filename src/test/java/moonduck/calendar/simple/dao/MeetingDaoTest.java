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

	@Test
	public void 특정_기준일에_회의가_있을_경우() {
		//07-01 ~ 08-01, 09:00 ~ 11:00, 수요일 반복
		Recurrence recurEntity = recurDao.save(new Recurrence()
				.setType(RecurrenceType.ONCE_A_WEEK)
				.setDayOfWeek(DayOfWeek.WEDNESDAY.getValue()));

		Room room = roomDao.save(new Room().setName("회의실1"));
		Meeting meeting = meetingDao.save(new Meeting()
				.setMeetingRoom(room)
				.setStartDate(LocalDate.of(2018, 7, 1))
				.setEndDate(LocalDate.of(2018, 9, 1))
				.setStartTime(LocalTime.of(9, 0))
				.setEndTime(LocalTime.of(11, 0))
				.setRecurrence(recurEntity)
				.setEnabled(true));

		List<Meeting> meetings = meetingDao.findAllMeetingInDate(
				room.getId(), LocalDate.of(2018, 8, 22), DayOfWeek.WEDNESDAY.getValue());

		assertEquals(Arrays.asList(meeting), meetings);
	}
}
