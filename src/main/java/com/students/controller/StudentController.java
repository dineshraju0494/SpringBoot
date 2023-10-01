package com.students.controller;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.students.dto.MessageData;
import com.students.dto.StudentRequest;
import com.students.dto.StudentResponse;
import com.students.entity.Student;
import com.students.service.StudentService;
import com.students.util.ResponseStructure;

@RestController
@RequestMapping("/students")
public class StudentController {

	@Autowired
	private StudentService service;
	
	@PostMapping
	public ResponseEntity<ResponseStructure<StudentResponse>> saveStudent(@RequestBody @Valid StudentRequest request){
		
		return service.saveStudent(request);
	}
	
	@PutMapping("/{studentId}")
	public ResponseEntity<ResponseStructure<StudentResponse>> updateStudent(@RequestBody  StudentRequest studentRequest, @PathVariable int studentId)
	{
		
		return service.updateStudent(studentRequest, studentId);
	}
	
	@DeleteMapping("/{studentId}")
	public ResponseEntity<ResponseStructure<Student>> deleteStudent(@PathVariable int studentId){
		return service.deleteStudent(studentId);
	}
	
	@GetMapping("/{email}")
	public ResponseEntity<ResponseStructure<StudentResponse>> findByEmail(@PathVariable String email){
		return service.findByEmail(email);
	}
	

//	@GetMapping("/grade/{grade}")
//	public ResponseEntity<ResponseStructure<List<String>>> getAllEmailsByGrade(@PathVariable String grade){
//		return service.getAllEmailsByGrade(grade);
//	}
	
	@PostMapping("/extract")
	public ResponseEntity<String> ExtractDataFromExcel(@RequestParam MultipartFile file) throws IOException{
	return  service.extractDataFromExcel(file);
		
	}
	
	@PostMapping("/write/excel")
	public ResponseEntity<String> writeToExcel(@RequestParam String filePath) throws IOException{
		return service.writeToExcel(filePath);
	}
	
	@PostMapping("/mail")
	public ResponseEntity<String> sendMail(@RequestBody MessageData messageData){
		return service.sendMail(messageData);
	}

	
	@PostMapping("/mime-messsage")
	public ResponseEntity<String> sendMyMail(@RequestBody MessageData messageData) throws MessagingException{
		return service.sendMimeMessage(messageData);
	}
}
