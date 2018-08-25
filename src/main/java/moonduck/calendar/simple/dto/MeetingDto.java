package moonduck.calendar.simple.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import moonduck.calendar.simple.entity.Meeting;
import moonduck.calendar.simple.entity.Room;
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
	private int meetingRoom;
	private String userName;
	
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

	public int getMeetingRoom() {
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

	public MeetingDto setMeetingRoom(int meetingRoom) {
		this.meetingRoom = meetingRoom;
		return this;
	}

	public MeetingDto setRecurrence(RecurrenceDto recurrence) {
		this.recurrence = recurrence;
		return this;
	}
	
	public String getUserName() {
		return userName;
	}

	public MeetingDto setUserName(String userName) {
		this.userName = userName;
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
		meeting.setMeetingRoom(new Room().setId(meetingRoom));
		meeting.setTitle(this.title);
		meeting.setContent(this.content);
		if (this.recurrence != null) {
			meeting.setRecurrence(this.recurrence.toEntity(includeId));
		}
		meeting.setUserName(userName);
		return meeting;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + meetingRoom;
		result = prime * result + ((recurrence == null) ? 0 : recurrence.hashCode());
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
		result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MeetingDto other = (MeetingDto) obj;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (endTime == null) {
			if (other.endTime != null)
				return false;
		} else if (!endTime.equals(other.endTime))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (meetingRoom != other.meetingRoom)
			return false;
		if (recurrence == null) {
			if (other.recurrence != null)
				return false;
		} else if (!recurrence.equals(other.recurrence))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}
}
