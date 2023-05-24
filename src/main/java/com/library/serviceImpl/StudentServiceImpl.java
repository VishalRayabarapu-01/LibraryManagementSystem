package com.library.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.library.entity.Student;
import com.library.repository.StudentRepository;
import com.library.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	StudentRepository repository;

	@Override
	public boolean addStudent(Student student) {
		boolean flag=validateStudent(student.getHtno());
		if(!flag) {
			Student save = repository.save(student);
			if(save.toString().equals(student.toString())) {
				return  true;
			}
		}
		return false;
	}
	
	public boolean validateStudent(String id) {
		Optional<Student> stud = repository.findById(id);
		if(stud.isPresent()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean deleteStudent(String htno) {
		Optional<Student> findById = repository.findById(htno);
		Student student = findById.get();
		repository.delete(student);
		return true;
	}

	@Override
	public boolean updatStudent(Student student) {
	
		return false;
	}

	@Override
	public List<Student> getStudents() {
		return repository.findAll();
	}

	@Override
	public Student getStudent(String htno) {
		Optional<Student> stud = repository.findById(htno);
		if(stud.isPresent()) {
			return stud.get();
		}
		return null;
	}

	@Override
	public Page<Student> getStudents(Pageable pageable) {
		return repository.findAll(pageable);
	}

}
