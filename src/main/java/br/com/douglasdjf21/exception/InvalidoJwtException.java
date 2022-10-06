package br.com.douglasdjf21.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class InvalidoJwtException extends AuthenticationException {
	
	private static final long serialVersionUID = 1L;
	

	public InvalidoJwtException(String ex) {
		super(ex);
	}
	

}
