package moonduck.calendar.simple.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "선점실패")
public class MeetingDuplicationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3482081519991207145L;

	public MeetingDuplicationException(String msg) {
		super(msg);
	}
}
