package telran.ashkelon2018fl.forum.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserConflictExeption extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
