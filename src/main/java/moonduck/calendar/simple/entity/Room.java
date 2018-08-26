package moonduck.calendar.simple.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
	
	@OneToMany(mappedBy = "meetingRoom", fetch = FetchType.EAGER)
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((meetings == null) ? 0 : meetings.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Room other = (Room) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (meetings == null) {
			if (other.meetings != null)
				return false;
		} else if (!meetings.equals(other.meetings))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
