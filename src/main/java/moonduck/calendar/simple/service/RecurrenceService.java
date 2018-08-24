package moonduck.calendar.simple.service;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import moonduck.calendar.simple.entity.Meeting;
import moonduck.calendar.simple.entity.Recurrence;
import moonduck.calendar.simple.enumeration.RecurrenceType;
import moonduck.calendar.simple.service.recurrence.RecurrenceHandler;

@Service
public class RecurrenceService {
	private Map<RecurrenceType, RecurrenceHandler> recurrenceServiceMap;
	
	public RecurrenceService(@Autowired List<RecurrenceHandler> recurServices) {
		Map<RecurrenceType, RecurrenceHandler> serviceMap = new EnumMap<>(RecurrenceType.class);
		for (RecurrenceHandler service : recurServices) {
			serviceMap.put(service.getAvailableType(), service);
		}
		this.recurrenceServiceMap = serviceMap;
	}
	
	/**
	 * 리스트 안에서 baseMeeting과 회의가 겹치는 것 중 가장 먼저 예약된 1개를 리턴한다
	 * @param baseMeeting 기준회의
	 * @param meetings 중복이 예상되는 회의리스트
	 * @return baseMeeting과 중복된 회의 중 먼저 예약된 회의
	 */
	public Optional<Meeting> getFirstInDuplicatedMeeting(Meeting baseMeeting, List<Meeting> meetings) {
		Recurrence baseMeetingRecurrence = baseMeeting.getRecurrence();
		RecurrenceHandler baseMeetingRecurrenceChecker = recurrenceServiceMap.get(
				baseMeetingRecurrence == null ? RecurrenceType.EVERYDAY_IN_RANGE : baseMeetingRecurrence.getType());
		
		return meetings.stream().filter(meeting -> {
			LocalDate baseMeetingStartDate = baseMeeting.getStartDate();
			LocalDate baseMeetingEndDate = baseMeeting.getEndDate();
			LocalDate meetingStartDate = meeting.getStartDate();
			LocalDate meetingEndDate = meeting.getEndDate();
			
			//겹칠만한 범위내에서만 겹치는지 확인
			LocalDate intersectionStart = baseMeetingStartDate.isAfter(meetingStartDate) ? baseMeetingStartDate : meetingStartDate;
			LocalDate intersectionEnd = baseMeetingEndDate.isBefore(meetingEndDate) ? baseMeetingEndDate : meetingEndDate;
			
			LocalDate nextOccurDate = intersectionStart;

			Recurrence meetingRecurrence = meeting.getRecurrence();
			RecurrenceHandler meetingRecurrenceChecker = recurrenceServiceMap.get(
					meetingRecurrence == null ? RecurrenceType.EVERYDAY_IN_RANGE : meetingRecurrence.getType());
			//겹칠만한 범위내에서 회의가 발생하는지 체크한다
			while (!nextOccurDate.isAfter(intersectionEnd)) {
				if (baseMeetingRecurrenceChecker.isOccur(nextOccurDate, baseMeeting, baseMeeting.getRecurrence())
						&& meetingRecurrenceChecker.isOccur(nextOccurDate, meeting, meeting.getRecurrence())) {
					return true;
				}
				LocalDate nextOccurCandidate1 = baseMeetingRecurrenceChecker.nextOccur(nextOccurDate, baseMeetingRecurrence);
				LocalDate nextOccurCandidate2 = meetingRecurrenceChecker.nextOccur(nextOccurDate, meetingRecurrence);
				//더 빨리 찾기 위해 큰 날짜를 선택
				nextOccurDate = nextOccurCandidate1.isAfter(nextOccurCandidate2) ? nextOccurCandidate1 : nextOccurCandidate2;
			}
			return false;
			//modifiedTime이 오래된 순으로 정렬
		}).sorted((meeting1, meeting2) -> Long.compare(meeting1.getModifiedTime(), meeting2.getModifiedTime()))
			.findFirst();
	}
}
