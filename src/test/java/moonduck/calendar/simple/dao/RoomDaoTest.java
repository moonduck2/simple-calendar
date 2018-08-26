package moonduck.calendar.simple.dao;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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
public class RoomDaoTest {
	@Autowired RoomDao roomDao;
	@Autowired MeetingDao meetingDao;
	@Autowired RecurrenceDao recurDao;

	@Test
	public void 모든_회의실의_회의_가져오기() {
		Room room1 = new Room().setName("회의있음");
		room1 = roomDao.save(room1);

		meetingDao.save(new Meeting()
				.setMeetingRoom(room1)
				.setStartDate(LocalDate.of(2018, 1, 1))
				.setEndDate(LocalDate.of(2018, 12, 12))
				.setStartTime(LocalTime.now())
				.setEndTime(LocalTime.now())
				.setEnabled(true)
				.setUserName("예약자")
				.setRecurrence(
						recurDao.save(new Recurrence()
								.setType(RecurrenceType.ONCE_A_WEEK)
								.setCount(1)
								.setDayOfWeek(DayOfWeek.WEDNESDAY.getValue()))));

		Room room2 = new Room().setName("회의없음");
		room2 = roomDao.save(room2);

		List<Room> rooms = roomDao.findAllPossibleMeetingsAtDateOfAllRooms(LocalDate.of(2018, 7, 4));

		assertThat(rooms, hasItems(room1, room2));
		assertEquals(2, rooms.size());
	}
}
