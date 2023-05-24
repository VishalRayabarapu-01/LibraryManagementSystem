package com.library.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class Issues {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long issueId;

	private String issueDate;
	
	private String returnDate;
	
	@ManyToOne
	Student issueStudent;
	
	@OneToOne
	BookNumbers bookNumbers;
	
	private String emailSent;
	
	public Issues(long issueId, String issueDate, Student issueStudent, BookNumbers bookNumbers) {
		super();
		this.issueId = issueId;
		this.issueDate = issueDate;
		this.issueStudent = issueStudent;
		this.bookNumbers = bookNumbers;
	}
	
	public Issues(String issueDate, Student issueStudent, BookNumbers bookNumbers,String returnDate,String emailSent) {
		super();
		this.issueDate = issueDate;
		this.issueStudent = issueStudent;
		this.bookNumbers = bookNumbers;
		this.returnDate=returnDate;
		this.emailSent=emailSent;
	}

	public Issues() {
		super();
	}
	

	public long getIssueId() {
		return issueId;
	}

	public void setIssueId(long issueId) {
		this.issueId = issueId;
	}

	public String getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}

	public Student getIssueStudent() {
		return issueStudent;
	}

	public void setIssueStudent(Student issueStudent) {
		this.issueStudent = issueStudent;
	}
	
	public String getEmailSent() {
		return emailSent;
	}

	public void setEmailSent(String emailSent) {
		this.emailSent = emailSent;
	}

	public String getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(String returnDate) {
		this.returnDate = returnDate;
	}

	public BookNumbers getBookNumbers() {
		return bookNumbers;
	}

	public void setBookNumbers(BookNumbers bookNumbers) {
		this.bookNumbers = bookNumbers;
	}


}
