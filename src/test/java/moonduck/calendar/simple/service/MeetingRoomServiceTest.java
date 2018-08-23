package moonduck.calendar.simple.service;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

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
	
	@Test
	public void 모든_회의실_가져오는_메소드_테스트() {
		Room room1 = mock(Room.class);
		RoomDto room1Dto = mock(RoomDto.class);
		when(room1.toDto()).thenReturn(room1Dto);
		
		Room room2 = mock(Room.class);
		RoomDto room2Dto = mock(RoomDto.class);
		when(room2.toDto()).thenReturn(room2Dto);
		
		List<Room> mockRooms = Arrays.asList(room1, room2);
		when(roomDao.findAll()).thenReturn(mockRooms);
		
		List<RoomDto> result = service.allRooms();
		assertThat(result, hasItems(room1Dto, room2Dto));
		assertEquals(2, result.size());
	}
}
