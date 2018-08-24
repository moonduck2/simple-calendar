package moonduck.calendar.simple.dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import moonduck.calendar.simple.entity.Meeting;

public interface MeetingDao extends CrudRepository<Meeting, Integer> {
	
	/**
	 * 기준일자와 겹치는 회의 모두 조회
	 * @param roomId
	 * @param baseDate
	 * @return 조건에 만족하는 회의 모두 조회
	 */
	@Query("select m from Meeting m where m.meetingRoom.id = :room"
			+ " and ((m.startDate <= :startDate and m.endDate >= :startDate) or (m.startDate <= :endDate and m.endDate >= :endDate))"
			+ " and ((m.startTime <= :startTime and m.endTime > :startTime) or (m.startTime < :endTime and m.endTime >= :endTime))")
	List<Meeting> findAllMeetingInDate(@Param("room") int roomId, 
			@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, 
			@Param("startTime") LocalTime baseTime, @Param("endTime") LocalTime endTime);
}
