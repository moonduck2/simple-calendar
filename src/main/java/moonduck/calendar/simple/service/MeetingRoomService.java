package moonduck.calendar.simple.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import moonduck.calendar.simple.dao.RoomDao;
import moonduck.calendar.simple.dto.RoomDto;

@Service
public class MeetingRoomService {
	@Autowired
	private RoomDao roomDao;
	
	public int createOrUpdateRoom(RoomDto room) {
		return roomDao.save(room.toEntity()).getId();
	}
	
	public void deleteRoom(int roomId) {
		roomDao.deleteById(roomId);
	}
	
	public List<RoomDto> allRooms() {
		List<RoomDto> allRooms = new ArrayList<>();
		roomDao.findAll().forEach(roomEntity -> allRooms.add(roomEntity.toDto()));
		return allRooms;
	}
}
