package com.students.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.students.entity.Student;

public interface StudentRepo extends JpaRepository<Student, Integer> {

	public Student findByStudentEmail(String email);

//	@Query("select s.studentEmail from Student s where s.stud.entGrade=?1")
//	public List<String> getAllEmailsByGrade(String grade);
}
