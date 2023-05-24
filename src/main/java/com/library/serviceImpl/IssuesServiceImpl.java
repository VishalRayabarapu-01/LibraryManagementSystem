package com.library.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.library.entity.Issues;
import com.library.entity.Student;
import com.library.repository.IssuesRepository;
import com.library.service.IssuesService;

@Service
public class IssuesServiceImpl implements IssuesService {

	@Autowired
	IssuesRepository repository;
	
	@Override
	public Page<Issues> getIssues(Pageable pageable) {
		return repository.findAll(pageable);
	}

	@Override
	public boolean saveIssueDetails(Issues issues) {
		Issues save = repository.save(issues);
		if(save.toString().equals(issues.toString())) {
			return true;
		}
		return false;
	}

	@Override
	public void deleteIssue(Issues issues) {
		repository.delete(issues);
	}

	@Override
	public List<Issues> getIssues() {
		return repository.findAll();
	}

	@Override
	public Page<Issues> getAllIssuesByStudent(Student student,Pageable pageable) {
		return  repository.getIssuesByissueStudent(student, pageable);
	}
	
	
}
