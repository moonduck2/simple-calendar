package moonduck.calendar.simple.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import moonduck.calendar.simple.dao.MeetingDao;
import moonduck.calendar.simple.entity.Meeting;
import moonduck.calendar.simple.entity.Recurrence;
import moonduck.calendar.simple.exception.MeetingDuplicationException;
import moonduck.calendar.simple.util.RecurrenceCalculator;

/**
 * 회의실 예약에 관한 트랜잭션 처리를 총괄한다. 
 */
@Service
public class MeetingService {
	@Autowired
	private MeetingDao meetingDao;
	
	@Autowired
	private RecurrenceService recurrenceService;
	
	@Transactional
	public int addMeeting(Meeting meeting) {
		calcEndDate(meeting);
		
		List<Meeting> possibleDuplicate = meetingDao.findAllPossibleDuplicate(meeting.getMeetingRoom(),
				meeting.getStartDate(), meeting.getEndDate(), meeting.getStartTime(), meeting.getEndTime());
		if (!possibleDuplicate.isEmpty()) {
			throw new MeetingDuplicationException("일정이 겹칩니다.");
		}
		
		Meeting meetingEntity = meetingDao.save(meeting);
		return meetingEntity.getId();
	}
	
	@Transactional
	public int modifyMeeting(Meeting meeting) {
		calcEndDate(meeting);
		
		List<Meeting> possibleDuplicate = meetingDao.findAllPossibleDuplicateExceptId(
				meeting.getId(), meeting.getMeetingRoom(),
				meeting.getStartDate(), meeting.getEndDate(), meeting.getStartTime(), meeting.getEndTime());
		if (possibleDuplicate.isEmpty()) {
			meeting = meetingDao.save(meeting);
		} else {
			throw new MeetingDuplicationException("수정하려는 일정이 겹칩니다.");
		} 
		
		return meeting.getId();
	}
	
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
	
	private Meeting calcEndDate(Meeting meeting) {
		Recurrence recur = meeting.getRecurrence();
		if (recur == null || meeting.getEndDate() != null) {
			return meeting;
		}
		meeting.setEndDate(RecurrenceCalculator.calcLastDate(
				meeting.getStartDate(), DayOfWeek.of(recur.getDayOfWeek()), recur.getCount()));
		return meeting;
	}
}
