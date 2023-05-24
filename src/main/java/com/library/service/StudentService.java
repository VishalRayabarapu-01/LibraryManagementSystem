package com.library.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.library.entity.Student;

@Service
public interface StudentService {

	boolean addStudent(Student student);

	boolean deleteStudent(String htno);

	boolean updatStudent(Student student);

	List<Student> getStudents();
	
	Page<Student> getStudents(Pageable pageable);

	Student getStudent(String htno);
	
}
