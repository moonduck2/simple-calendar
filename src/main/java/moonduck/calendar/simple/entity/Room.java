package moonduck.calendar.simple.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import moonduck.calendar.simple.dto.RoomDto;

@Table(name = "room")
@Entity
public class Room {
	@Id @GeneratedValue
	private Integer id;
	
	@Column
	private String name;
	
	@OneToMany(mappedBy = "meetingRoom")
	private List<Meeting> meetings;

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Room setId(Integer id) {
		this.id = id;
		return this;
	}

	public Room setName(String name) {
		this.name = name;
		return this;
	}

	public List<Meeting> getMeetings() {
		return meetings;
	}

	public Room setMeetings(List<Meeting> meetings) {
		this.meetings = meetings;
		return this;
	}
	
	public RoomDto toDto() {
		return new RoomDto().setId(id).setName(name);
	}
}
