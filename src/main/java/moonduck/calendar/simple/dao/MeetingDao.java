package moonduck.calendar.simple.dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import moonduck.calendar.simple.entity.Meeting;

public interface MeetingDao extends CrudRepository<Meeting, Integer> {
	
	@Query("select m from Meeting m where m.meetingRoom = :room"
			+ " and m.recurrence.dayOfWeek = :dayOfWeek"
			+ " and (m.startDate between :startDate and :endDate or m.endDate between :startDate and :endDate)"
			+ " and ((m.startTime > :startTime and m.startTime < :endTime) or (endTime > :startTime and endTime < :endTime))")
	List<Meeting> findAllPossibleDuplicate(@Param("room") String room, 
			@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, 
			@Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime,
			@Param("dayOfWeek") int dayOfWeek);
	
	@Query("select m from Meeting m where m.meetingRoom = :room"
			+ " and m.recurrence.dayOfWeek = :dayOfWeek"
			+ " and m.endDate >= :startDate")
	List<Meeting> findAllMeetingInDate(@Param("room") int roomId, 
			@Param("startDate") LocalDate startDate, @Param("dayOfWeek") int dayOfWeek);
	
	//TODO: unit test
	@Query("select m from Meeting m where m.recurrence.dayOfWeek = :dayOfWeek and m.enabled = 1"
			+ " and m.startDate <= :baseDate and m.endDate >= :baseDate")
	List<Meeting> findMeetingByDate(@Param("baseDate") LocalDate baseDate, @Param("dayOfWeek") int dayOfWeek);

	//TODO: unit test
	@Query("select m from Meeting m where m.recurrence.dayOfWeek = :dayOfWeek and enabled = 1"
			+ " and m.startDate <= :baseDate and m.endDate >= :baseDate"
			+ " and m.meetingRoom IN :rooms")
	List<Meeting> findMeetingByDate(@Param("baseDate") LocalDate baseDate,
			@Param("dayOfWeek") int dayOfWeek, @Param("rooms") Collection<String> rooms);
	
	//TODO: unit test
	@Query("select m from Meeting m where m.id != :id and m.meetingRoom = :room"
			+ " and (m.startDate between :startDate and :endDate or m.endDate between :startDate and :endDate)"
			+ " and ((m.startTime > :startTime and m.startTime < :endTime) or (endTime > :startTime and endTime < :endTime))")
	List<Meeting> findAllPossibleDuplicateExceptId(
			@Param("id") int id, @Param("room") String room, 
			@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, 
			@Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);
}
