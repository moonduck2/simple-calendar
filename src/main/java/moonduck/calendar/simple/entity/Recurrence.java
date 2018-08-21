package moonduck.calendar.simple.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import moonduck.calendar.simple.enumeration.RecurrenceType;

@Table(name = "recurrence")
@Entity
public class Recurrence {
	@Id
	@Column
	private int id;
	
	@Column(name = "type")
	private RecurrenceType type;
	
	@Column(name = "count")
	private int count;
	
	@Column(name = "day_of_week")
	private int dayOfWeek;
	
	@ManyToOne
	private Meeting meeting;

	public int getId() {
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
