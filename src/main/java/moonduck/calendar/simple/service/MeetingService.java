package moonduck.calendar.simple.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import moonduck.calendar.simple.dao.MeetingDao;
import moonduck.calendar.simple.entity.Meeting;
import moonduck.calendar.simple.exception.MeetingDuplicationException;

/**
 * 회의실 예약에 관한 트랜잭션 처리를 총괄한다. 
 */
@Service
public class MeetingService {
	@Autowired
	private MeetingDao meetingDao;
	
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
}
