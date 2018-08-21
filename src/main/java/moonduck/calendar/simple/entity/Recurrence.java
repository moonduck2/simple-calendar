package moonduck.calendar.simple.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import moonduck.calendar.simple.enumeration.RecurrenceType;

@Table(name = "recurrence")
@Entity(name = "recurrence")
public class Recurrence {
	@Column(name = "type")
	private RecurrenceType type;
	
	@Column(name = "count")
	private int count;
	
	@Column(name = "day_of_week")
	private int dayOfWeek;
	
	@ManyToOne
	private Meeting meeting;
}
