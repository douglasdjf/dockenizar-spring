package br.com.douglasdjf21.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionRestHandler {
	
	
	@ExceptionHandler(RequiredObjectIsNullException.class)
	public ResponseEntity<ErroMessage> handlerObjectIsNull(RequiredObjectIsNullException ex, HttpServletRequest request){
		ErroMessage error = new ErroMessage(LocalDateTime.now(), "NOT NULL", ex.getMessage(), HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(InvalidoJwtException.class)
	public ResponseEntity<ErroMessage> handlerInvalidJwtException(InvalidoJwtException ex, HttpServletRequest request){
		ErroMessage error = new ErroMessage(LocalDateTime.now(), "FORBIDDEN", ex.getMessage(), HttpStatus.FORBIDDEN.value());
		return new ResponseEntity<>(error,HttpStatus.FORBIDDEN);
		
	}
	
	@ExceptionHandler(TokenExpiredException.class)
	public ResponseEntity<ErroMessage> handelrTokenExpirad(TokenExpiredException ex, HttpServletRequest request){
		ErroMessage error = new ErroMessage(LocalDateTime.now(), "FORBIDDEN", ex.getMessage(), HttpStatus.FORBIDDEN.value());
		return new ResponseEntity<>(error,HttpStatus.FORBIDDEN);
		
	}
	
	@ExceptionHandler(JWTVerificationException.class)
	public ResponseEntity<ErroMessage> handelrTokenVerify(JWTVerificationException ex, HttpServletRequest request){
		ErroMessage error = new ErroMessage(LocalDateTime.now(), "FORBIDDEN", ex.getMessage(), HttpStatus.FORBIDDEN.value());
		return new ResponseEntity<>(error,HttpStatus.FORBIDDEN);
		
	}
	
	@ExceptionHandler(JwtExpiredException.class)
	public ResponseEntity<ErroMessage> handelrTokenExpirad(JwtExpiredException ex, HttpServletRequest request){
		ErroMessage error = new ErroMessage(LocalDateTime.now(), "FORBIDDEN", ex.getMessage(), HttpStatus.FORBIDDEN.value());
		return new ResponseEntity<>(error,HttpStatus.FORBIDDEN);
		
	}
	
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErroMessage> handlerMessageNotSuported(HttpMessageNotReadableException ex, HttpServletRequest request){
		ErroMessage error = new ErroMessage(LocalDateTime.now(), "BAD REQUEST", ex.getMessage(), HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
		
	}
	

	
}
