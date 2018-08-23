package moonduck.calendar.simple.dao;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import moonduck.calendar.simple.entity.Room;

public interface RoomDao extends CrudRepository<Room, Integer> {
	@Query("select r from Room r left join r.meetings m left join m.recurrence rec where m.id is null or"
			+ " (m.enabled = true and rec.dayOfWeek = :dayOfWeek"
			+ " and m.startDate <= :baseDate and m.endDate >= :baseDate)")
	List<Room> findMeetingsEachRooms(@Param("baseDate") LocalDate date, @Param("dayOfWeek") int dayOfWeek);
	
	@Query("select r from Room r left join r.meetings m left join m.recurrence rec where m.id is null or"
			+ " (r.id in :roomIds and m.enabled = true and rec.dayOfWeek = :dayOfWeek"
			+ " and m.startDate <= :baseDate and m.endDate >= :baseDate)")
	List<Room> findMeetingsEachRooms(@Param("baseDate") LocalDate date, @Param("dayOfWeek") int dayOfWeek, 
			@Param("roomIds") Collection<Integer> roomIds);
}
