package moonduck.calendar.simple.dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import moonduck.calendar.simple.entity.Meeting;

public interface MeetingDao extends CrudRepository<Meeting, Integer> {
	@Query("select m from Meeting m where m.meetingRoom = :room"
			+ " and ((m.startDate between :startDate and :endDate or m.endDate between :startDate and :endDate)"
			+ " and (m.startTime between :startTime and :endTime or endTime between :startTime and :endTime))")
	List<Meeting> findAllPossibleDuplicate(@Param("room") String room, 
			@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, 
			@Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);
}
