package moonduck.calendar.simple.dao;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import moonduck.calendar.simple.entity.Room;

public interface RoomDao extends CrudRepository<Room, Integer> {
	/**
	 * 조건에 만족하는 회의실과 회의들을 조인해서 가져온다. *존재하는 모든 회의실을 가져온다(회의가 없더라도)*.
	 * @param date 기준일자
	 * @param dayOfWeek 기준일자의 요일
	 * @return 모든 회의실
	 */
	@Query("select r from Room r left join r.meetings m left join m.recurrence rec where m.id is null or"
			+ " (m.enabled = true and (rec is null or rec.dayOfWeek = :dayOfWeek)"
			+ " and m.startDate <= :baseDate and m.endDate >= :baseDate)")
	List<Room> findMeetingsEachRooms(@Param("baseDate") LocalDate date, @Param("dayOfWeek") int dayOfWeek);
	
	/**
	 * 위 메소드와 동일하나 일부 회의실만 가져올 수 있음
	 * @param date
	 * @param dayOfWeek
	 * @param roomIds
	 * @return 지정된 회의실에 한해 모든 회의를 가져온다.
	 */
	@Query("select r from Room r left join r.meetings m left join m.recurrence rec where r.id in :roomIds or"
			+ " (r.id in :roomIds and m.enabled = true and (rec is null or rec.dayOfWeek = :dayOfWeek)"
			+ " and m.startDate <= :baseDate and m.endDate >= :baseDate)")
	List<Room> findMeetingsEachRooms(@Param("baseDate") LocalDate date, @Param("dayOfWeek") int dayOfWeek, 
			@Param("roomIds") Collection<Integer> roomIds);
}
