package br.com.douglasdjf21.exception;

import java.time.LocalDateTime;

public class ErroMessage {
	
	private LocalDateTime timestamp;
	private String error;
	private String message;
	private Integer status;
	
	public ErroMessage() {
		super();
	}
	public ErroMessage(LocalDateTime timestamp, String error, String message, Integer status) {
		super();
		this.timestamp = timestamp;
		this.error = error;
		this.message = message;
		this.status = status;
	}
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	

}
