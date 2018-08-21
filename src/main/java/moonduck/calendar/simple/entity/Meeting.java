package moonduck.calendar.simple.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Table(name = "meeting")
@Entity(name = "meeting")
public class Meeting {
	@Column
	private LocalDate start;
	
	@Column
	private LocalDate end;
	
	@Column(name = "start_time")
	private LocalTime startTime;
	
	@Column(name = "end_time")
	private LocalTime endTime;
	
	@Column
	private String title;
	
	@Column
	private String content;
	
	@Column(name = "meeting_room")
	private String meetingRoom;
	
	@OneToMany(mappedBy = "meeting")
	private List<Recurrence> recurrence;
}
