package com.library.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.library.entity.Issues;
import com.library.entity.Student;

public interface IssuesRepository extends JpaRepository<Issues,Long>{

	Page<Issues> getIssuesByissueStudent(Student student,Pageable pageable);
}
