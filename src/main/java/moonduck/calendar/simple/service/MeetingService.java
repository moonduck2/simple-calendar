package moonduck.calendar.simple.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
import moonduck.calendar.simple.util.RecurrenceCalculator;

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
	
	@Transactional
	public int addMeeting(MeetingDto meeting) {
		setEndDateIfNotExist(meeting);
		setRecurrenceIfNotExist(meeting);
		
		List<Meeting> possibleDuplicate = Collections.emptyList();
		
		if (meeting.getRecurrence() != null) {
		  possibleDuplicate = meetingDao.findAllPossibleDuplicate(meeting.getMeetingRoom(),
				meeting.getStartDate(), meeting.getEndDate(), meeting.getStartTime(), meeting.getEndTime(),
				meeting.getRecurrence().getDayOfWeek());
		} else {
			possibleDuplicate = meetingDao.findAllPossibleDuplicate(meeting.getMeetingRoom(),
					meeting.getStartDate(), meeting.getEndDate(), meeting.getStartTime(), meeting.getEndTime(),
					meeting.getRecurrence().getDayOfWeek());
		}
		if (!possibleDuplicate.isEmpty()) {
			throw new MeetingDuplicationException("일정이 겹칩니다.");
		}
		return saveMeeting(meeting, false).getId();
	}
	
	private Meeting saveMeeting(MeetingDto meeting, boolean isUpdate) {
		RecurrenceDto recur = meeting.getRecurrence();
		Meeting meetingEntity = meeting.toEntity(isUpdate);
		if (recur != null) {
			Recurrence recurEntity = recurDao.save(recur.toEntity(isUpdate));
			meetingEntity.setRecurrence(recurEntity);
		}
		
		return meetingDao.save(meetingEntity);
	}
	
	@Transactional
	public int modifyMeeting(MeetingDto meeting) {
		setEndDateIfNotExist(meeting);
		setRecurrenceIfNotExist(meeting);
		
		List<Meeting> possibleDuplicate = meetingDao.findAllPossibleDuplicateExceptId(
				meeting.getId(), meeting.getMeetingRoom(),
				meeting.getStartDate(), meeting.getEndDate(), meeting.getStartTime(), meeting.getEndTime());
		if (!possibleDuplicate.isEmpty()) {
			throw new MeetingDuplicationException("수정하려는 일정이 겹칩니다.");
		} 
		return saveMeeting(meeting, true).getId();
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
	
	private MeetingDto setRecurrenceIfNotExist(MeetingDto meeting) {
		RecurrenceDto recur = meeting.getRecurrence();
		if (recur == null) {
			//문제의 단순화를 위해 반복이 없는 경우는 1회 반복으로 처리한다.
			recur = new RecurrenceDto().setType(RecurrenceType.ONCE_A_WEEK)
				.setCount(1)
				//반복이 없는 경우는 startDate와 endDate가 같으므로 임의로 startDate의 요일을 선택
				.setDayOfWeek(meeting.getStartDate().getDayOfWeek().getValue());
			meeting.setRecurrence(recur);
		}
		return meeting;
	}
	private MeetingDto setEndDateIfNotExist(MeetingDto meeting) {
		RecurrenceDto recur = meeting.getRecurrence();
		if (recur == null || meeting.getEndDate() != null) {
			return meeting;
		}
		meeting.setEndDate(RecurrenceCalculator.calcLastDate(
				meeting.getStartDate(), DayOfWeek.of(recur.getDayOfWeek()), recur.getCount()));
		return meeting;
	}
}
