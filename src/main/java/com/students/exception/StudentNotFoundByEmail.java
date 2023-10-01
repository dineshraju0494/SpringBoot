package com.students.exception;

public class StudentNotFoundByEmail extends RuntimeException {

	private String message;

	public StudentNotFoundByEmail(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
