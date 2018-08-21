package moonduck.calendar.simple.service;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import moonduck.calendar.simple.entity.Meeting;
import moonduck.calendar.simple.entity.Recurrence;
import moonduck.calendar.simple.enumeration.RecurrenceType;
import moonduck.calendar.simple.exception.NoSuitableRecurrenceCheckerException;
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
	
	public boolean isOccur(LocalDate date, Meeting meeting, Recurrence recurrence) {
		RecurrenceHandler service = recurrenceServiceMap.get(recurrence.getType());
		if (service == null) {
			throw new NoSuitableRecurrenceCheckerException();
		}
		return service.isOccur(date, meeting, recurrence);
	}
}
