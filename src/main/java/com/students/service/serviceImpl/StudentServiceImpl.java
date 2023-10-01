package com.students.service.serviceImpl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.students.dto.MessageData;
import com.students.dto.StudentRequest;
import com.students.dto.StudentResponse;
import com.students.entity.Student;
import com.students.exception.StudentNotFoundByEmail;
import com.students.exception.StudentNotFoundByIdException;
import com.students.repo.StudentRepo;
import com.students.service.StudentService;
import com.students.util.ResponseStructure;

@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	private StudentRepo repo;
	
	
	@Autowired
	JavaMailSender sender;

	@Override
	public ResponseEntity<ResponseStructure<StudentResponse>> saveStudent(StudentRequest studentRequest) {
		Student student = new Student();
		student.setStudentName(studentRequest.getStudentName());
		student.setStudentEmail(studentRequest.getStudentEmail());
		student.setStudentGrade(studentRequest.getStudentGrade());
		student.setStudentPhNo(studentRequest.getStudentPhNo());
		student.setStudentPassword(studentRequest.getStudentPassword());

		Student std1 = repo.save(student);

		StudentResponse response = new StudentResponse();
		response.setStudentId(std1.getStudentId());
		response.setStudentName(std1.getStudentName());
		response.setStudentGrade(std1.getStudentGrade());

		ResponseStructure<StudentResponse> structure = new ResponseStructure<>();
		structure.setStatus(HttpStatus.CREATED.value());
		structure.setMessage("Student data saved successfully!!!");
		structure.setData(response);

		return new ResponseEntity<ResponseStructure<StudentResponse>>(structure, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ResponseStructure<StudentResponse>> updateStudent(StudentRequest studentRequest,
			Integer studentId) {

		Optional<Student> findById = repo.findById(studentId);
		if (findById.isPresent()) {

			// creating a new student entity object
			// and setting the data request to entity object
			Student student1 = new Student();
			student1.setStudentName(studentRequest.getStudentName());
			student1.setStudentEmail(studentRequest.getStudentEmail());
			student1.setStudentGrade(studentRequest.getStudentGrade());
			student1.setStudentPhNo(studentRequest.getStudentPhNo());
			student1.setStudentPassword(studentRequest.getStudentPassword());
			student1.setStudentId(studentId);
			// saving the entity object

			Student std = repo.save(student1);

			// the returned student object must be converted to a response object
			// and then should be returned to the client

			StudentResponse response = new StudentResponse();
			response.setStudentId(std.getStudentId());
			response.setStudentName(std.getStudentName());
			response.setStudentGrade(std.getStudentGrade());

			// creating the response structure
			ResponseStructure<StudentResponse> structure = new ResponseStructure<>();
			structure.setStatus(HttpStatus.CREATED.value());
			structure.setMessage("Student Data has been updatedted successfully!!!");
			structure.setData(response);
			return new ResponseEntity<ResponseStructure<StudentResponse>>(structure, HttpStatus.OK);
		} else {
			return null;
		}

	}

	@Override
	public ResponseEntity<ResponseStructure<Student>> deleteStudent(int studentId) {
		Optional<Student> optional = repo.findById(studentId);
		if (optional.isPresent()) {
			repo.deleteById(studentId);
			ResponseStructure<Student> structure = new ResponseStructure<>();
			structure.setStatus(HttpStatus.CREATED.value());
			structure.setMessage("Student Data has been deleted successfully!!!");
			structure.setData(optional.get());
			return new ResponseEntity<ResponseStructure<Student>>(structure, HttpStatus.OK);
		}
		throw new StudentNotFoundByIdException("Failed to Delete the Student");

	}

	@Override
	public ResponseEntity<ResponseStructure<StudentResponse>> findByEmail(String studentEmail) {
		Student student = repo.findByStudentEmail(studentEmail);
		if (student != null) {
			StudentResponse response = new StudentResponse();
			response.setStudentId(student.getStudentId());
			response.setStudentName(student.getStudentName());
			response.setStudentGrade(student.getStudentGrade());

			ResponseStructure<StudentResponse> structure = new ResponseStructure<StudentResponse>();
			structure.setStatus(HttpStatus.FOUND.value());
			structure.setMessage("Student Found based on Email!!!");
			structure.setData(response);

			return new ResponseEntity<ResponseStructure<StudentResponse>>(structure, HttpStatus.FOUND);
		}
		throw new StudentNotFoundByEmail("Failed to find Student with the Requested Email!!!");

	}

//	@Override
//	public ResponseEntity<ResponseStructure<List<String>>> getAllEmailsByGrade(String grade) {
//		List<String> emails = repo.getAllEmailsByGrade(grade);
//		if(emails!= null) {
//			
//			ResponseStructure<List<String>> structure = new ResponseStructure<List<String>>();
//			structure.setStatus(HttpStatus.FOUND.value());
//			structure.setMessage("Stduent Details Found Based on Grade!!!!");
//			structure.setData(emails);
//			
//			return new ResponseEntity<ResponseStructure<List<String>>> (structure, HttpStatus.FOUND);
//		}
//		return null;
//	}

	@Override
	public ResponseEntity<String> extractDataFromExcel(MultipartFile file) throws IOException {
		
		XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
		
		for(Sheet sheet : workbook) {
			for(Row row : sheet) {
				if(row.getRowNum()>0) {
					if(row!=null) {
					String name = row.getCell(0).getStringCellValue();
					String email = row.getCell(1).getStringCellValue();
					long phoneNumber =(long)row.getCell(2).getNumericCellValue();
					String grade = row.getCell(3).getStringCellValue();
					String password = row.getCell(4).getStringCellValue();
					System.out.println("The Details are " + name + " " + email + " " + phoneNumber + " " + grade + " " + password);
					Student student = new Student();
					student.setStudentName(name);
					student.setStudentEmail(email);
					student.setStudentGrade(grade);
					student.setStudentPassword(password);
					student.setStudentPhNo(phoneNumber);
					
					repo.save(student);
					
					 }
					}	
			}
		}
		workbook.close();
		return null;
	}

	@Override
	public ResponseEntity<String> writeToExcel(String filePath) throws IOException {
		
		List<Student> students = repo.findAll();
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet();
		
		Row header = sheet.createRow(0);
		header.createCell(0).setCellValue("studentId");
		header.createCell(1).setCellValue("studentName");
		header.createCell(2).setCellValue("studenEmail");
		header.createCell(3).setCellValue("studentPhNo");
		header.createCell(4).setCellValue("studentGrade");
		header.createCell(5).setCellValue("studentPassword");
		
		int rowNum=1;
		for(Student std : students) {
			
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(std.getStudentId());
			row.createCell(1).setCellValue(std.getStudentName());
			row.createCell(2).setCellValue(std.getStudentEmail());
			row.createCell(3).setCellValue(std.getStudentPhNo());
			row.createCell(4).setCellValue(std.getStudentGrade());
			row.createCell(5).setCellValue(std.getStudentPassword());
		}
		FileOutputStream fileOutputStream= new FileOutputStream(filePath);
		workbook.write(fileOutputStream);
		
		workbook.close();
		return new ResponseEntity<String>("Data transfered to Ecel Sheet!!!!!!!!!!", HttpStatus.OK);
	
	}

	@Override
	public ResponseEntity<String> sendMail(MessageData messageData) {
		
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(messageData.getTo());
		message.setSubject(messageData.getSubject());
		message.setText(messageData.getText()
				+"\n\nThanks & Regards"
				+"\n"+messageData.getSenderName()
				+"\n"+ messageData.getSenderAddress()
				);
		message.setSentDate(new java.util.Date());
		
		sender.send(message);
	
		return new ResponseEntity<String>("Mail has Been Sent Successfully!!!!!", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> sendMimeMessage(MessageData messageData) throws MessagingException {
		MimeMessage mime = sender.createMimeMessage();
		MimeMessageHelper message = new MimeMessageHelper(mime, true);
		
		message.setTo(messageData.getTo());
		message.setSubject(messageData.getSubject());
		
		String emailBody = messageData.getText()
				+ "<br><br><h4> Thanks & Regards<br>"
				 + messageData.getSenderName() + "<br>"
		+ messageData.getSenderAddress() + "</h4>"
		+ "<img src=\"https://shorturl.at/qBO08\" width=\"100px\" height=\"100px\">";
		
		message.setText(emailBody,true);
		message.setSentDate(new java.util.Date());
		
		sender.send(mime);
		return new ResponseEntity<String>("Mail Has been sent!!!!!!", HttpStatus.OK);
	}
	
	
}
