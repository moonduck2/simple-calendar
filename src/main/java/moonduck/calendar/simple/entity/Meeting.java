package moonduck.calendar.simple.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import moonduck.calendar.simple.type.HasDateRange;
import moonduck.calendar.simple.validator.annotation.ValidDateRange;

@Table(name = "meeting")
@Entity
@ValidDateRange
public class Meeting implements HasDateRange {
	@Id
	@GeneratedValue
	private Integer id;
	
	@Column(name = "start_date", nullable = false)
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate startDate;
	
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
	@Column(name = "meeting_room", length = 20)
	private String meetingRoom;
	
	@OneToMany(mappedBy = "meeting")
	private List<Recurrence> recurrence;

	public Integer getId() {
		return id;
	}

	@Override
	public LocalDate getStartDate() {
		return startDate;
	}

	@Override
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

	public String getMeetingRoom() {
		return meetingRoom;
	}

	public List<Recurrence> getRecurrence() {
		return recurrence;
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

	public Meeting setMeetingRoom(String meetingRoom) {
		this.meetingRoom = meetingRoom;
		return this;
	}

	public Meeting setRecurrence(List<Recurrence> recurrence) {
		this.recurrence = recurrence;
		return this;
	}
}
