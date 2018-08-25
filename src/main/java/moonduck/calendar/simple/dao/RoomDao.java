package moonduck.calendar.simple.dao;

import java.time.LocalDate;
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
	@Query("select r from Room r left join r.meetings m left join m.recurrence rec where r.id is not null"
			+ " or (m.enabled = true and m.startDate <= :baseDate and m.endDate >= :baseDate)")
	List<Room> findAllPossibleMeetingsAtDateOfAllRooms(@Param("baseDate") LocalDate date);
}
