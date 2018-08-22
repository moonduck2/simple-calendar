package moonduck.calendar.simple.dao;

import org.springframework.data.repository.CrudRepository;

import moonduck.calendar.simple.entity.Recurrence;

public interface RecurrenceDao extends CrudRepository<Recurrence, Integer> {

}
