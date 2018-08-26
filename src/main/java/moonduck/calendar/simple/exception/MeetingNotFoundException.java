package moonduck.calendar.simple.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "존재하지 않는 회의임")
public class MeetingNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -29652548439608292L;

	public MeetingNotFoundException(int id) {
		super(id + "는 없는 회의 ID임");
	}
}
