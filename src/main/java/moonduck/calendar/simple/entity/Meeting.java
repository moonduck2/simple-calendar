package moonduck.calendar.simple.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Table(name = "meeting")
@Entity
public class Meeting {
	@Id
	@GeneratedValue
	private int id;
	
	@Column(name = "start_date", nullable = false)
	private LocalDate startDate;
	
	@Column(name = "end_date", nullable = false)
	private LocalDate endDate;
	
	@Column(name = "start_time", nullable = false)
	private LocalTime startTime;
	
	@Column(name = "end_time", nullable = false)
	private LocalTime endTime;
	
	@Column
	private String title;
	
	@Column(length = 1000)
	private String content;
	
	@Column(name = "meeting_room", length = 20)
	private String meetingRoom;
	
	@OneToMany(mappedBy = "meeting")
	private List<Recurrence> recurrence;

	public int getId() {
		return id;
	}

	public LocalDate getStart() {
		return startDate;
	}

	public LocalDate getEnd() {
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

	public Meeting setStart(LocalDate start) {
		this.startDate = start;
		return this;
	}

	public Meeting setEnd(LocalDate end) {
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
