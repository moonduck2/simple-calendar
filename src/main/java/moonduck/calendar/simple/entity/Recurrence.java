package moonduck.calendar.simple.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

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

	public int getCount() {
		return count;
	}

	public int getDayOfWeek() {
		return dayOfWeek;
	}

	public Meeting getMeeting() {
		return meeting;
	}

	public Recurrence setType(RecurrenceType type) {
		this.type = type;
		return this;
	}

	public Recurrence setCount(int count) {
		this.count = count;
		return this;
	}

	public Recurrence setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
		return this;
	}

	public Recurrence setMeeting(Meeting meeting) {
		this.meeting = meeting;
		return this;
	}
}
