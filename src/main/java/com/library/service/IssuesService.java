package com.library.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.library.entity.Issues;
import com.library.entity.Student;

@Service
public interface IssuesService {

	Page<Issues> getIssues(Pageable pageable);
	
	boolean saveIssueDetails(Issues issues);
	
	void deleteIssue(Issues issues);
	
	List<Issues> getIssues();
	
	 Page<Issues> getAllIssuesByStudent(Student student,Pageable pageable);
}
