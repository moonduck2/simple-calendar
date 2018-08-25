package moonduck.calendar.simple.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import moonduck.calendar.simple.dao.MeetingDao;
import moonduck.calendar.simple.dao.RecurrenceDao;
import moonduck.calendar.simple.dao.RoomDao;
import moonduck.calendar.simple.dto.MeetingDto;
import moonduck.calendar.simple.dto.RecurrenceDto;
import moonduck.calendar.simple.dto.RoomDto;
import moonduck.calendar.simple.entity.Meeting;
import moonduck.calendar.simple.entity.Recurrence;
import moonduck.calendar.simple.entity.Room;
import moonduck.calendar.simple.exception.MeetingDuplicationException;
import moonduck.calendar.simple.exception.MeetingNotFoundException;

/**
 * 회의실 예약에 관한 트랜잭션 처리를 총괄한다. 
 */
@Service
public class MeetingService {
	@Autowired
	private MeetingDao meetingDao;
	
	@Autowired
	private RecurrenceDao recurDao;
	
	@Autowired
	private RoomDao roomDao;
	
	@Autowired
	private CalendarUtilService util;
	
	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	private RecurrenceService recurChecker;
	
	//동시에 회의실을 잡을 수 있기때문에 가장 먼저 잡은 것만 빼고 나머지는 지운다.
	//회의 요청이 insert되었다가 delete되면 예외를 던져 회의실 선점 실패를 알린다.
	@Transactional
	public int addMeeting(MeetingDto meeting) {
		return addMeeting(meeting, meetingEntity ->  {
			meetingEntity.setEnabled(true);
			return meetingEntity.getId();
		});
	}
	
	private int addMeeting(MeetingDto meeting, Function<Meeting, Integer> successFunc) {
		util.normalizeMeeting(meeting);
		
		Meeting meetingEntity = saveMeeting(meeting, false);

		List<Meeting> possibleDuplicate = meetingDao.findAllMeetingInDateAndTime(
				meeting.getMeetingRoom().getId(), 
				meeting.getStartDate(), meeting.getEndDate(),
				meeting.getStartTime(), meeting.getEndTime());
		
		//meetingEntity와 시간이 겹치는 회의 중 가장 먼저 예약된 회의를 찾는다.
		Optional<Meeting> duplicatedMeetings = recurChecker.getFirstInDuplicatedMeeting(
				meetingEntity, possibleDuplicate);
		
		//겹치는 시간 중 요청된 값이 가장 오래된 값이면 선점 성공, 그렇지 않으면 선점 실패로 예외 throw
		if (!duplicatedMeetings.isPresent() || meetingEntity.getId().equals(duplicatedMeetings.get().getId())) {
			return successFunc.apply(meetingEntity);
		}
		throw new MeetingDuplicationException("이미 회의실이 선점되었습니다.");
	}
	
	private Meeting saveMeeting(MeetingDto meeting, boolean isUpdate) {
		RecurrenceDto recur = meeting.getRecurrence();
		Meeting meetingEntity = meeting.toEntity(isUpdate);
		if (recur != null) {
			Recurrence recurEntity = recurDao.save(recur.toEntity(isUpdate));
			meetingEntity.setRecurrence(recurEntity);
		}
		
		return meetingDao.save(meetingEntity.setModifiedTimeToServerTime());
	}
	
	/**
	 * 메소드명은 modify이지만 실제로는 새로 회의를 만들고 기존 것을 삭제하는 과정을 거친다.
	 * 먼저 변경되는 시간으로 새로 만들어보고 성공할 경우 기존 것을 삭제, 실패할 경우 예외를 던진다.
	 * @param meeting 변경될 회의 날짜/시간 정보를 담은 객체
	 * @return 주어진 시간으로 새로 생성된 회의의 ID
	 */
	@Transactional
	public int modifyMeeting(MeetingDto meeting) {
		util.normalizeMeeting(meeting);
		Meeting oldMeetingEntity = meetingDao.findById(meeting.getId()).orElseThrow(() -> new MeetingNotFoundException(meeting.getId()));
		
		return addMeeting(meeting, newMeetingEntity -> {
			EntityTransaction tx = entityManager.getTransaction();
			newMeetingEntity.setEnabled(true);
			meetingDao.delete(oldMeetingEntity);
			tx.commit();
			return newMeetingEntity.getId();
		});
	}
	
	/**
	 * 특정날짜의 회의를 조회한다.
	 * @param date 기준일자
	 * @return 모든 회의실의 일정 리스트
	 */
	public List<RoomDto> findAllMeetingOfAllRoomsAtDate(LocalDate date) {
		List<Room> allMeetings = roomDao.findAllPossibleMeetingsAtDateOfAllRooms(date);
		return allMeetings.stream()
				//모든 룸을 가져오되
				.map(roomEntity -> roomEntity.toDto().setMeetings(
						Optional.ofNullable(roomEntity.getMeetings()).orElseGet(() -> Collections.emptyList())
						//date에 날짜가 있을 법한 모든 회의 중에서 실제로 있는 회의만 filter를 통과한다
						.stream()
							.filter(meetingEntity -> recurChecker.isOccur(date, meetingEntity))
							.map(meetingEntity -> meetingEntity.toDto())
							.collect(Collectors.toList())))
				.collect(Collectors.toList());
	}
	
	@Transactional
	public void deleteMeeting(int meetingId) {
		meetingDao.deleteById(meetingId);
	}
}
