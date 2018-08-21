package moonduck.calendar.simple.controller;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

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

import moonduck.calendar.simple.entity.Meeting;
import moonduck.calendar.simple.service.MeetingService;

@RestController
@RequestMapping("/meeting")
public class ApiController {
	@Autowired
	private MeetingService meetingService;
	
	@GetMapping
	public List<Meeting> getMeetings(@RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate date, 
			@RequestParam(required = false) Collection<String> rooms) {
		return meetingService.findMeetingByDate(date);
	}
	
	@PostMapping
	public int newMeeting(@RequestBody Meeting meeting) {
		throw new UnsupportedOperationException("not implemented");
	}
	
	@PutMapping
	public int modifyMeeting(@RequestBody Meeting meetinig) {
		throw new UnsupportedOperationException("not implemented");
	}
	
	@DeleteMapping("{meetingId}")
	public void deleteMeeting(@PathVariable int meetingId) {
		throw new UnsupportedOperationException("not implemented");
	}
}
