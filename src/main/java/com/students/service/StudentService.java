package com.students.service;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;

import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.students.dto.MessageData;
import com.students.dto.StudentRequest;
import com.students.dto.StudentResponse;
import com.students.entity.Student;
import com.students.util.ResponseStructure;

public interface StudentService {

	public ResponseEntity<ResponseStructure<StudentResponse>> saveStudent(StudentRequest request);
	
	public ResponseEntity<ResponseStructure<StudentResponse>> updateStudent(StudentRequest studentRequest, Integer studentId);
	
	public ResponseEntity<ResponseStructure<Student>> deleteStudent(int studentId);
	

	public ResponseEntity<ResponseStructure<StudentResponse>> findByEmail(String studentEmail);
	

//	public ResponseEntity<ResponseStructure<List<String>>> getAllEmailsByGrade(String grade);
	
	public ResponseEntity<String> extractDataFromExcel(MultipartFile file) throws IOException;
	
	public ResponseEntity<String> writeToExcel(String filePath) throws IOException;
	
	public ResponseEntity<String> sendMail(MessageData messageData);

	public ResponseEntity<String> sendMimeMessage(MessageData messageData) throws MessagingException;
	
	
}
