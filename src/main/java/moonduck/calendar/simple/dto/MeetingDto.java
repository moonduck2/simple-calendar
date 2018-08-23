package moonduck.calendar.simple.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import moonduck.calendar.simple.entity.Meeting;
import moonduck.calendar.simple.validator.annotation.ValidMeeting;

@ValidMeeting
public class MeetingDto {
	private Integer id;
	private LocalDate startDate;
	private LocalDate endDate;
	private LocalTime startTime;
	private LocalTime endTime;
	private String title;
	private String content;
	private RoomDto meetingRoom;
	
	private RecurrenceDto recurrence;

	public Integer getId() {
		return id;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

	public RoomDto getMeetingRoom() {
		return meetingRoom;
	}

	public RecurrenceDto getRecurrence() {
		return recurrence;
	}

	public MeetingDto setStartDate(LocalDate start) {
		this.startDate = start;
		return this;
	}

	public MeetingDto setEndDate(LocalDate end) {
		this.endDate = end;
		return this;
	}

	public MeetingDto setStartTime(LocalTime startTime) {
		this.startTime = startTime;
		return this;
	}

	public MeetingDto setEndTime(LocalTime endTime) {
		this.endTime = endTime;
		return this;
	}

	public MeetingDto setTitle(String title) {
		this.title = title;
		return this;
	}

	public MeetingDto setContent(String content) {
		this.content = content;
		return this;
	}

	public MeetingDto setMeetingRoom(RoomDto meetingRoom) {
		this.meetingRoom = meetingRoom;
		return this;
	}

	public MeetingDto setRecurrence(RecurrenceDto recurrence) {
		this.recurrence = recurrence;
		return this;
	}
	
	public Meeting toEntity(boolean includeId) {
		Meeting meeting = new Meeting();
		if (includeId) {
			meeting.setId(this.id);
		}
		meeting.setStartDate(this.startDate);
		meeting.setEndDate(this.endDate);
		meeting.setStartTime(this.startTime);
		meeting.setEndTime(this.endTime);
		meeting.setMeetingRoom(this.meetingRoom.toEntity());
		meeting.setTitle(this.title);
		meeting.setContent(this.content);
		meeting.setRecurrence(this.recurrence.toEntity(includeId));
		return meeting;
	}
}
