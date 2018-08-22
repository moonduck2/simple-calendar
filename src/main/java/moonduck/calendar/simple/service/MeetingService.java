package moonduck.calendar.simple.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import moonduck.calendar.simple.dao.MeetingDao;
import moonduck.calendar.simple.dao.RecurrenceDao;
import moonduck.calendar.simple.dto.MeetingDto;
import moonduck.calendar.simple.dto.RecurrenceDto;
import moonduck.calendar.simple.entity.Meeting;
import moonduck.calendar.simple.entity.Recurrence;
import moonduck.calendar.simple.enumeration.RecurrenceType;
import moonduck.calendar.simple.exception.MeetingDuplicationException;
import moonduck.calendar.simple.util.TemporalCalculator;

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
	private RecurrenceService recurrenceService;
	
	@Autowired
	private CalendarUtilService util;
	
	//동시에 회의실을 잡을 수 있기때문에 가장 먼저 잡은 것만 빼고 나머지는 지운다.
	//회의 요청이 insert되었다가 delete되면 예외를 던져 회의실 선점 실패를 알린다.
	public int addMeeting(MeetingDto meeting) {
		util.normalizeMeeting(meeting);
		
		Meeting meetingEntity = saveMeeting(meeting, false);
		//TODO : 처음부터 겹치는 시간대만 가져오자
		List<Meeting> possibleDuplicate = meetingDao.findAllMeetingInDate(meeting.getMeetingRoom(), 
				meeting.getStartDate(), meeting.getRecurrence().getDayOfWeek());
		
		//시간이 겹치는 구간을 찾는다.
		SortedSet<Meeting> duplicatedMeetings = util.findDuplicatedPeriod(
				possibleDuplicate, meeting.getStartTime(), meeting.getEndTime());
		
		//겹치는 시간 중 요청된 값이 가장 오래된 값이면 선점 성공, 그렇지 않으면 선점 실패로 예외 throw
		Meeting firstMeeting = duplicatedMeetings.first();
		if (firstMeeting.getId().equals(meetingEntity.getId())) {
			return meetingEntity.getId();
		}
		meetingDao.delete(meetingEntity);
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
	
	@Transactional
	public int modifyMeeting(MeetingDto meeting) {
		util.normalizeMeeting(meeting);
		
		List<Meeting> possibleDuplicate = meetingDao.findAllPossibleDuplicateExceptId(
				meeting.getId(), meeting.getMeetingRoom(),
				meeting.getStartDate(), meeting.getEndDate(), meeting.getStartTime(), meeting.getEndTime());
		if (!possibleDuplicate.isEmpty()) {
			throw new MeetingDuplicationException("수정하려는 일정이 겹칩니다.");
		} 
		return saveMeeting(meeting, true).getId();
	}
	
	//TODO : 임시로 넣은 회의도 같이 나올 수 있으므로 중복된 회의는 선점한 회의만 빼고 제거하자
	@Transactional
	public List<Meeting> findMeetingByDate(LocalDate date, Collection<String> rooms) {
		List<Meeting> allMeetings = CollectionUtils.isEmpty(rooms) 
				? meetingDao.findMeetingByDate(date)
				: meetingDao.findMeetingByDate(date, rooms);
				
		return allMeetings.stream().filter(meeting -> {
			Recurrence recur = meeting.getRecurrence();
			if (recur == null || recurrenceService.isOccur(date, meeting, recur)) {
				return true;
			}
			return false;
		}).collect(Collectors.toList());
	}
	
	@Transactional
	public void deleteMeeting(int meetingId) {
		meetingDao.deleteById(meetingId);
	}
}
