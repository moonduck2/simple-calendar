package moonduck.calendar.simple.service;

import static org.junit.Assert.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.SortedSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;

import moonduck.calendar.simple.dto.MeetingDto;
import moonduck.calendar.simple.dto.RecurrenceDto;
import moonduck.calendar.simple.entity.Meeting;

@RunWith(SpringRunner.class)
public class CalendarUtilServiceTest {
	@InjectMocks
	private CalendarUtilService service;
	
	@Test
	public void 해당_시간대_중복된_회의_찾기_테스트() {
		Meeting first = new Meeting().setStartTime(LocalTime.of(8, 0)).setEndTime(LocalTime.of(10, 0))
				.setModifedTime(1L);
		Meeting second = new Meeting().setStartTime(LocalTime.of(10, 0)).setEndTime(LocalTime.of(14, 0))
				.setModifedTime(2L);
		Meeting third = new Meeting().setStartTime(LocalTime.of(10, 30)).setEndTime(LocalTime.of(19, 0))
				.setModifedTime(3L);
		
		//11:00 ~ 15:00회의를 가져올 경우 second와 third가 대상이 되는데 second가 modifiedTime이 작으므로 SortedSet.first의 결과는 second가 되어야 함
		SortedSet<Meeting> result = service.findDuplicatedPeriod(Arrays.asList(first, second, third), 
				LocalTime.of(11, 0), LocalTime.of(15, 0));
		
		assertEquals(second, result.first());
		assertEquals(2, result.size());
	}
	
	@Test
	public void MeetingDto_startDate값_정상화_테스트() {
		MeetingDto dto = new MeetingDto();
		dto.setStartDate(LocalDate.of(2018, 8, 1)); //수요일
		dto.setRecurrence(new RecurrenceDto().setDayOfWeek(DayOfWeek.THURSDAY.getValue()).setCount(1));
		
		//시작일이 수요일이고 반복엔 목요일로 되어 있으면 시작일이 목요일로 수정되어야 함
		assertEquals(LocalDate.of(2018, 8, 2), service.normalizeMeeting(dto).getStartDate());
	}
	
	@Test
	public void MeetingDto_endDate값_정상화_테스트() {
		MeetingDto dto = new MeetingDto();
		dto.setStartDate(LocalDate.of(2018, 8, 2));
		dto.setRecurrence(new RecurrenceDto().setDayOfWeek(DayOfWeek.THURSDAY.getValue()).setCount(1));
		
		//endDate가 없어 계산해야 함. 8월 2일 목요일에 시작하여 목요일마다 1회 반복이면 8월 2일이 마지막 날짜가 되어야 함
		assertEquals(LocalDate.of(2018, 8, 2), service.normalizeMeeting(dto).getEndDate());
		
		//endDate가 있지만 반복횟수와 맞지 않는 값이라서 반복횟수에 맞게 endDate를 수정함
		dto.setEndDate(LocalDate.of(2018, 10, 10));
		assertEquals(LocalDate.of(2018, 8, 2), service.normalizeMeeting(dto).getEndDate());
	}
}
