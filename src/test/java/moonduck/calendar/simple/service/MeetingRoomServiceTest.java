package moonduck.calendar.simple.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import moonduck.calendar.simple.dao.RoomDao;
import moonduck.calendar.simple.dto.RoomDto;
import moonduck.calendar.simple.entity.Room;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MeetingRoomServiceTest {
	@InjectMocks
	private MeetingRoomService service;
	
	@Mock
	private RoomDao roomDao;
	
	@Test
	public void 회의실_수정_생성_메소드_테스트() {
		RoomDto room = mock(RoomDto.class);
		Room roomEntity = mock(Room.class);
		when(room.toEntity()).thenReturn(roomEntity);
		when(roomEntity.getId()).thenReturn(0);
		when(roomDao.save(roomEntity)).thenReturn(roomEntity);

		service.createOrUpdateRoom(room);
		verify(roomDao).save(eq(roomEntity));
	}
	
	@Test
	public void 회의실_삭제_메소드_테스트() {
		service.deleteRoom(1);
		verify(roomDao).deleteById(eq(1));
	}
}
