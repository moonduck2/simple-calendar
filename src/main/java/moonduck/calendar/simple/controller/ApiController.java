package moonduck.calendar.simple.controller;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import moonduck.calendar.simple.dto.MeetingDto;
import moonduck.calendar.simple.dto.RoomDto;
import moonduck.calendar.simple.service.MeetingService;

@RestController
@RequestMapping("/api/meeting")
public class ApiController {
	@Autowired
	private MeetingService meetingService;
	
	@GetMapping
	public List<RoomDto> getMeetings(@RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate date) {
		return meetingService.findAllMeetingOfAllRoomsAtDate(date);
	}
	
	@PostMapping
	public int newMeeting(@RequestBody @Valid MeetingDto meeting) {
		return meetingService.addMeeting(meeting);
	}
	
	@PutMapping
	public int modifyMeeting(@RequestBody @Valid MeetingDto meeting) {
		return meetingService.modifyMeeting(meeting);
	}
	
	@DeleteMapping("{meetingId}")
	public void deleteMeeting(@PathVariable int meetingId) {
		meetingService.deleteMeeting(meetingId);
	}
	
	@GetMapping(path = "{meetingId}")
	public MeetingDto getMeeting(@PathVariable int meetingId) {
		return meetingService.getMeetingById(meetingId);
	}
}
