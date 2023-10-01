package com.students.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.students.util.ErrorStructure;

@RestControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<ObjectError> allErrors = ex.getAllErrors();
		Map<String, String> errors = new HashMap<String, String>();
		
		for(ObjectError error: allErrors) {

			FieldError fieldError = (FieldError)error;
			String message=fieldError.getDefaultMessage();
			String field=fieldError.getField();
			errors.put(field, message);
		}
	
		return new ResponseEntity<Object>(errors,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure> studentNotFoundById(StudentNotFoundByIdException ex){
		ErrorStructure structure=new ErrorStructure();
		structure.setStatus(HttpStatus.NOT_FOUND.value());
		structure.setMessage(ex.getMessage());
		structure.setRootCause("Student is not present with the Requested ID");
		return new ResponseEntity<ErrorStructure>(structure, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure> studentNotFoundByEmail(StudentNotFoundByEmail ex){
		ErrorStructure structure=new ErrorStructure();
		structure.setStatus(HttpStatus.NOT_FOUND.value());
		structure.setMessage(ex.getMessage());
		structure.setRootCause("Student is not present with the Requested Email");
		return new ResponseEntity<ErrorStructure>(structure, HttpStatus.NOT_FOUND);
	}
	
}
