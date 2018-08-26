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
	
	/**
	 * 요청한 회의실을 생성/수정한다
	 * @param room
	 * @return 생성/수정된 회의실 ID
	 */
	public int createOrUpdateRoom(RoomDto room) {
		return roomDao.save(room.toEntity()).getId();
	}
	
	/**
	 * 회의실 삭제
	 * @param roomId
	 */
	public void deleteRoom(int roomId) {
		roomDao.deleteById(roomId);
	}
	
	/**
	 * @return 모든 회의실 가져옴
	 */
	public List<RoomDto> allRooms() {
		List<RoomDto> allRooms = new ArrayList<>();
		roomDao.findAll().forEach(roomEntity -> allRooms.add(roomEntity.toDto()));
		return allRooms;
	}
}
