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

	public Integer getCount() {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((count == null) ? 0 : count.hashCode());
		result = prime * result + ((dayOfWeek == null) ? 0 : dayOfWeek.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RecurrenceDto other = (RecurrenceDto) obj;
		if (count == null) {
			if (other.count != null)
				return false;
		} else if (!count.equals(other.count))
			return false;
		if (dayOfWeek == null) {
			if (other.dayOfWeek != null)
				return false;
		} else if (!dayOfWeek.equals(other.dayOfWeek))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
}
