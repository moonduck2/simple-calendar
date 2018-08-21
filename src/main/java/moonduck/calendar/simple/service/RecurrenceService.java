package moonduck.calendar.simple.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import moonduck.calendar.simple.entity.Meeting;
import moonduck.calendar.simple.entity.Recurrence;

@Service
public class RecurrenceService {
	public boolean isOccur(LocalDate date, Meeting meeting, Recurrence recurrence) {
		return false;
	}
}
