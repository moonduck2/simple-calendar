package moonduck.calendar.simple.exception;

public class MeetingNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -29652548439608292L;

	public MeetingNotFoundException(int id) {
		super(id + "는 없는 회의 ID임");
	}
}
