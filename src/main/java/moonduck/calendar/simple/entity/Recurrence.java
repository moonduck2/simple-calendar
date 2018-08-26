package moonduck.calendar.simple.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import moonduck.calendar.simple.dto.RecurrenceDto;
import moonduck.calendar.simple.enumeration.RecurrenceType;

@Table(name = "recurrence", indexes = {
	@Index(columnList = "type")
})
@Entity
public class Recurrence {
	@Id
	@GeneratedValue
	private Integer id;
	
	@NotNull
	@Column(name = "type", nullable = false)
	@Enumerated(EnumType.STRING)
	private RecurrenceType type;
	
	@Column(name = "count")
	private Integer count;
	
	@Column(name = "day_of_week")
	private Integer dayOfWeek;
	
	@OneToOne(mappedBy = "recurrence")
	private Meeting meeting;

	public Integer getId() {
		return id;
	}

	public RecurrenceType getType() {
		return type;
	}

	public Integer getCount() {
		return count;
	}

	public Integer getDayOfWeek() {
		return dayOfWeek;
	}

	public Meeting getMeeting() {
		return meeting;
	}

	public Recurrence setCount(Integer count) {
		this.count = count;
		return this;
	}

	public Recurrence setDayOfWeek(Integer dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
		return this;
	}

	public Recurrence setId(Integer id) {
		this.id = id;
		return this;
	}

	public Recurrence setType(RecurrenceType type) {
		this.type = type;
		return this;
	}

	public Recurrence setMeeting(Meeting meeting) {
		this.meeting = meeting;
		return this;
	}
	
	public RecurrenceDto toDto() {
		return new RecurrenceDto()
				.setType(type)
				.setCount(count)
				.setDayOfWeek(dayOfWeek);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((count == null) ? 0 : count.hashCode());
		result = prime * result + ((dayOfWeek == null) ? 0 : dayOfWeek.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((meeting == null) ? 0 : meeting.hashCode());
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
		Recurrence other = (Recurrence) obj;
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
		if (meeting == null) {
			if (other.meeting != null)
				return false;
		} else if (!meeting.equals(other.meeting))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
}
