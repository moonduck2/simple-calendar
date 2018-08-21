package moonduck.calendar.simple.dao;

import static org.junit.Assert.*;

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

@RunWith(SpringRunner.class)
@DataJpaTest
public class MeetingDaoTest {
	@Autowired
	private MeetingDao dao;

	@Before
	public void init() {
		//07-01 ~ 08-01, 09:00 ~ 11:00
		dao.save(new Meeting()
				.setMeetingRoom("회의실1")
				.setStart(LocalDate.of(2018, 7, 1))
				.setEnd(LocalDate.of(2018, 9, 1))
				.setStartTime(LocalTime.of(9, 0))
				.setEndTime(LocalTime.of(11, 0)));
	}

	@Test
	public void 날짜도_시간도_겹치지_않음() {
		//01-01 ~ 05-01, 14:00 ~ 15:00
		List<Meeting> possibleDuplicate = dao.findAllPossibleDuplicate("회의실1",
				LocalDate.of(2018, 1, 1), LocalDate.of(2018, 5, 1), LocalTime.of(14, 0), LocalTime.of(15, 0));

		assertEquals(0, possibleDuplicate.size());
	}

	@Test
	public void 날짜만_겹침() {

	}
}
