package moonduck.calendar.simple.config;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import moonduck.calendar.simple.dto.MeetingDto;
import moonduck.calendar.simple.dto.RecurrenceDto;
import moonduck.calendar.simple.dto.RoomDto;
import moonduck.calendar.simple.enumeration.RecurrenceType;
import moonduck.calendar.simple.service.MeetingRoomService;
import moonduck.calendar.simple.service.MeetingService;

@Profile("test")
@Configuration
public class InitDataConfiguration {
	@Autowired
	private MeetingService meetingService;
	
	@Autowired
	private MeetingRoomService roomService;
	
	@PostConstruct
	public void init() {
		Map<String, Integer> meetingRooms = generateRooms();
		generateMeetings(meetingRooms);
	}
	
	private Map<String, Integer> generateRooms() {
		Map<String, Integer> idMap = new HashMap<>();
		idMap.put("회의실A", roomService.createOrUpdateRoom(new RoomDto().setName("회의실A")));
		idMap.put("회의실B", roomService.createOrUpdateRoom(new RoomDto().setName("회의실B")));
		idMap.put("회의실C", roomService.createOrUpdateRoom(new RoomDto().setName("회의실C")));
		idMap.put("회의실D", roomService.createOrUpdateRoom(new RoomDto().setName("회의실D")));
		idMap.put("회의실E", roomService.createOrUpdateRoom(new RoomDto().setName("회의실E")));
		idMap.put("회의실F", roomService.createOrUpdateRoom(new RoomDto().setName("회의실F")));
		idMap.put("회의실G", roomService.createOrUpdateRoom(new RoomDto().setName("회의실G")));
		return idMap;
	}
	
	private void generateMeetings(Map<String, Integer> roomMap) {
		meetingService.addMeeting(new MeetingDto()
				.setStartDate(LocalDate.of(2018, 1, 1))
				.setEndDate(LocalDate.of(2018, 12, 31))
				.setStartTime(LocalTime.of(10, 0))
				.setEndTime(LocalTime.of(11, 0))
				.setUserName("김개발")
				.setTitle("개발1팀 주간회의")
				.setContent("매주하는 회의")
				.setMeetingRoom(roomMap.get("회의실A"))
				.setRecurrence(new RecurrenceDto().setType(RecurrenceType.ONCE_A_WEEK).setDayOfWeek(DayOfWeek.FRIDAY.getValue())));
		
		meetingService.addMeeting(new MeetingDto()
				.setStartDate(LocalDate.of(2018, 1, 1))
				.setEndDate(LocalDate.of(2018, 12, 31))
				.setStartTime(LocalTime.of(13, 0))
				.setEndTime(LocalTime.of(14, 30))
				.setTitle("기획3팀 아이디어 회의")
				.setUserName("김기획")
				.setContent("자세한 내용은 메일 참고")
				.setMeetingRoom(roomMap.get("회의실A"))
				.setRecurrence(new RecurrenceDto().setType(RecurrenceType.ONCE_A_WEEK).setDayOfWeek(DayOfWeek.FRIDAY.getValue())));
		
		meetingService.addMeeting(new MeetingDto()
				.setStartDate(LocalDate.of(2018, 8, 6))
				.setEndDate(LocalDate.of(2018, 8, 17))
				.setStartTime(LocalTime.of(10, 0))
				.setEndTime(LocalTime.of(18, 0))
				.setTitle("회계감사")
				.setUserName("이회계")
				.setContent("외부 감사")
				.setMeetingRoom(roomMap.get("회의실B")));
		
		meetingService.addMeeting(new MeetingDto()
				.setStartDate(LocalDate.of(2018, 8, 31))
				.setEndDate(LocalDate.of(2018, 8, 31))
				.setStartTime(LocalTime.of(10, 0))
				.setEndTime(LocalTime.of(18, 0))
				.setTitle("개발세미나")
				.setUserName("최개발")
				.setContent("Spring 세미나")
				.setMeetingRoom(roomMap.get("회의실C")));
		
		meetingService.addMeeting(new MeetingDto()
				.setStartDate(LocalDate.of(2018, 8, 13))
				.setStartTime(LocalTime.of(15, 0))
				.setEndTime(LocalTime.of(16, 30))
				.setTitle("신규서비스TF 회의")
				.setUserName("오신규")
				.setContent("사내 전 팀장 참석")
				.setMeetingRoom(roomMap.get("회의실F"))
				.setRecurrence(new RecurrenceDto().setType(RecurrenceType.ONCE_A_WEEK)
						.setDayOfWeek(DayOfWeek.FRIDAY.getValue())
						.setCount(4)));
	}
}
