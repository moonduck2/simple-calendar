package moonduck.calendar.simple.dto;

import java.util.Collections;
import java.util.List;

import javax.validation.constraints.NotNull;

import moonduck.calendar.simple.entity.Room;

/**
 * 회의실과 관련된 데이터 교환 객체
 */
public class RoomDto {
	private Integer id;
	@NotNull private String name;
	private List<MeetingDto> meetings = Collections.emptyList();
	
	public Integer getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public RoomDto setId(Integer id) {
		this.id = id;
		return this;
	}
	public RoomDto setName(String name) {
		this.name = name;
		return this;
	}
	public List<MeetingDto> getMeetings() {
		return meetings;
	}
	public RoomDto setMeetings(List<MeetingDto> meetings) {
		this.meetings = meetings;
		return this;
	}
	public Room toEntity() {
		return new Room().setId(id).setName(name);
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
		RoomDto other = (RoomDto) obj;
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
