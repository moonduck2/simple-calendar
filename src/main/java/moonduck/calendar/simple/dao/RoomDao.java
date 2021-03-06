package moonduck.calendar.simple.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import moonduck.calendar.simple.entity.Room;

public interface RoomDao extends CrudRepository<Room, Integer> {
	/**
	 * 조건에 만족하는 회의실과 회의들을 조인해서 가져온다. *존재하는 모든 회의실을 가져온다(회의가 없더라도)*.
	 * @param date 기준일자
	 * @return 모든 회의실
	 */
	@EntityGraph(attributePaths = {"meetings", "meetings.recurrence"})
	@Query("select distinct r from Room r left join r.meetings m"
			+ " on m.enabled = true and m.startDate <= :baseDate and m.endDate >=:baseDate"
			+ " order by r.id")
	List<Room> findAllPossibleMeetingsAtDateOfAllRooms(@Param("baseDate") LocalDate date);
}
