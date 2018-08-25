package moonduck.calendar.simple.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import moonduck.calendar.simple.dto.MeetingDto;

@Table(name = "meeting", indexes = {
	@Index(columnList = "start_date"), @Index(columnList = "end_date"),
	@Index(columnList = "start_time"), @Index(columnList = "end_time"),
	@Index(columnList = "room_id"), @Index(columnList = "modified_time"),
	@Index(columnList = "enabled")
})
@Entity
public class Meeting {
	@Id
	@GeneratedValue
	private Integer id;
	
	@NotNull
	@Column(name = "start_date", nullable = false)
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate startDate;
	
	@NotNull
	@Column(name = "end_date", nullable = false)
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate endDate;
	
	@NotNull
	@Column(name = "start_time", nullable = false)
	@DateTimeFormat(iso = ISO.TIME)
	private LocalTime startTime;
	
	@NotNull
	@Column(name = "end_time", nullable = false)
	@DateTimeFormat(iso = ISO.TIME)
	private LocalTime endTime;
	
	@Column
	private String title;
	
	@Column(length = 1000)
	private String content;
	
	@NotNull
	@Column(name = "enabled")
	private boolean enabled;
	
	@NotNull
	@Column(name = "modified_time")
	private long modifiedTime;
	
	@NotNull
	@Column(name = "user_name", length = 10)
	private String userName;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "recur_id")
	private Recurrence recurrence;
	
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "room_id")
	private Room meetingRoom;

	public Integer getId() {
		return id;
	}

	public Room getMeetingRoom() {
		return meetingRoom;
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

	public Recurrence getRecurrence() {
		return recurrence;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public Meeting setEnabled(boolean enabled) {
		this.enabled = enabled;
		return this;
	}

	public Meeting setStartDate(LocalDate start) {
		this.startDate = start;
		return this;
	}

	public Meeting setEndDate(LocalDate end) {
		this.endDate = end;
		return this;
	}

	public Meeting setStartTime(LocalTime startTime) {
		this.startTime = startTime;
		return this;
	}

	public Meeting setEndTime(LocalTime endTime) {
		this.endTime = endTime;
		return this;
	}

	public Meeting setTitle(String title) {
		this.title = title;
		return this;
	}

	public Meeting setContent(String content) {
		this.content = content;
		return this;
	}

	public Meeting setMeetingRoom(Room meetingRoom) {
		this.meetingRoom = meetingRoom;
		return this;
	}

	public Meeting setRecurrence(Recurrence recurrence) {
		this.recurrence = recurrence;
		return this;
	}
	
	public long getModifiedTime() {
		return modifiedTime;
	}

	public Meeting setModifiedTimeToServerTime() {
		this.modifiedTime = System.nanoTime();
		return this;
	}
	
	public Meeting setModifedTime(long timestamp) {
		this.modifiedTime = timestamp;
		return this;
	}

	public String getUserName() {
		return userName;
	}

	public Meeting setUserName(String userName) {
		this.userName = userName;
		return this;
	}

	public MeetingDto toDto() {
		MeetingDto dto = new MeetingDto()
				.setId(id)
				.setStartDate(startDate)
				.setEndDate(endDate)
				.setStartTime(startTime)
				.setEndTime(endTime)
				.setTitle(title)
				.setContent(content)
				.setUserName(userName)
				.setMeetingRoom(meetingRoom.getId());
		if (this.recurrence != null) {
			dto.setRecurrence(recurrence.toDto());
		}
		return dto;
	}
}
