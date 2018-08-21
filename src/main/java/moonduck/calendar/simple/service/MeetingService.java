package moonduck.calendar.simple.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import moonduck.calendar.simple.dao.MeetingDao;
import moonduck.calendar.simple.entity.Meeting;
import moonduck.calendar.simple.entity.Recurrence;
import moonduck.calendar.simple.exception.MeetingDuplicationException;

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
	public int addOrUpdateMeeting(Meeting meeting) {
		List<Meeting> possibleDuplicate = meetingDao.findAllPossibleDuplicate(meeting.getMeetingRoom(),
				meeting.getStart(), meeting.getEnd(), meeting.getStartTime(), meeting.getEndTime());
		if (!possibleDuplicate.isEmpty()) {
			throw new MeetingDuplicationException();
		}
		
		Meeting meetingEntity = meetingDao.save(meeting);
		return meetingEntity.getId();
	}
	
	@Transactional
	public List<Meeting> findMeetingByDate(LocalDate date) {
		List<Meeting> allMeetings = meetingDao.findMeetingByDate(date);
		return allMeetings.stream().filter(meeting -> {
			for (Recurrence recur : meeting.getRecurrence()) {
				if (recurrenceService.isOccur(date, meeting, recur)) {
					return true;
				}
			}
			return false;
		}).collect(Collectors.toList());
	}
}
