package moonduck.calendar.simple.dto;

import moonduck.calendar.simple.entity.Recurrence;
import moonduck.calendar.simple.enumeration.RecurrenceType;

public class RecurrenceDto {
	private Integer id;
	private RecurrenceType type;
	private Integer count;
	private Integer dayOfWeek;

	public Integer getId() {
		return id;
	}

	public RecurrenceType getType() {
		return type;
	}

	public int getCount() {
		return count;
	}

	public int getDayOfWeek() {
		return dayOfWeek;
	}
	
	public RecurrenceDto setCount(Integer count) {
		this.count = count;
		return this;
	}
	public RecurrenceDto setType(RecurrenceType type) {
		this.type = type;
		return this;
	}

	public RecurrenceDto setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
		return this;
	}

	public Recurrence toEntity(boolean includeId) {
		Recurrence recur = new Recurrence();
		if (includeId) {
			recur.setId(this.id);
		}
		recur.setCount(this.count);
		recur.setDayOfWeek(this.dayOfWeek);
		recur.setType(this.type);
		return recur;
	}
}
