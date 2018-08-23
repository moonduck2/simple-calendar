package moonduck.calendar.simple.dao;

import org.springframework.data.repository.CrudRepository;

import moonduck.calendar.simple.entity.Room;

public interface RoomDao extends CrudRepository<Room, Integer> {

}
