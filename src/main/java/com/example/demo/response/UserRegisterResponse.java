package com.example.demo.response;

public class UserRegisterResponse<T> {

	private boolean status;
	private String message;
	private T data;
	
	public UserRegisterResponse(boolean status, String message, T data) {
		super();
		this.status = status;
		this.message = message;
		this.data = data;
	}
	
	public boolean isSuccess() {
		return status;
	}
	
	public String getMessage() {
		return message;
	}
	
	public T getData() {
		return data;
	}
	
}
