package moonduck.calendar.simple.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import moonduck.calendar.simple.entity.Meeting;

public interface MeetingDao extends CrudRepository<Meeting, Integer> {
	
	/**
	 * 회의실 ID와 기준요일/일자에 해당하는 회의 조회
	 * @param roomId
	 * @param baseDate
	 * @param dayOfWeek
	 * @return 조건에 만족하는 회의 모두 조회
	 */
	@Query("select m from Meeting m where m.meetingRoom.id = :room"
			+ " and m.recurrence.dayOfWeek = :dayOfWeek"
			+ " and m.endDate >= :baseDate")
	List<Meeting> findAllMeetingInDate(@Param("room") int roomId, 
			@Param("baseDate") LocalDate baseDate, @Param("dayOfWeek") int dayOfWeek);
}
