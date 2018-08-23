package moonduck.calendar.simple.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import moonduck.calendar.simple.dto.RoomDto;
import moonduck.calendar.simple.service.MeetingRoomService;

@RestController
@RequestMapping("/api/room")
public class RoomController {
	@Autowired
	private MeetingRoomService service;
	
	@PostMapping
	public int newRoom(@RequestBody RoomDto room) {
		return service.createOrUpdateRoom(room);
	}
}
