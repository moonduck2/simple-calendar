package moonduck.calendar.simple.service;

import static org.junit.Assert.*;

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
	
	@Test
	public void 겹칠_경우_addOrUpdate_테스트() {
		fail();
	}
}
