package moonduck.calendar.simple.service;

import org.springframework.stereotype.Service;

import moonduck.calendar.simple.entity.Meeting.MeetingDto;

/**
 * 회의실 예약에 관한 트랜잭션 처리를 총괄한다. 
 */
@Service
public class MeetingService {
	public int addOrUpdateMeeting(MeetingDto meeting) {
		return 0;
	}
}
