package moonduck.calendar.simple.dao;

import org.springframework.data.repository.CrudRepository;

import moonduck.calendar.simple.entity.Meeting;

public interface MeetingDao extends CrudRepository<Meeting, Integer> {

}
