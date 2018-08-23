package moonduck.calendar.simple.dto;

import moonduck.calendar.simple.entity.Room;

public class RoomDto {
	private int id;
	private String name;
	
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public RoomDto setId(int id) {
		this.id = id;
		return this;
	}
	public RoomDto setName(String name) {
		this.name = name;
		return this;
	}
	
	public Room toEntity() {
		return new Room().setId(id).setName(name);
	}
}
