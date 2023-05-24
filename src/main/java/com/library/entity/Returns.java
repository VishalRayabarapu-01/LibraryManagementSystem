package com.library.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Returns {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int returnId;

	private String bookTitle;
	
	private String issueDate;

	@ManyToOne
	Student returnStudent;

	private String returnDate;

	private int fines;

	public Returns( Student returnStudent, String returnDate, int fines) {
		super();
		this.returnStudent = returnStudent;
		this.returnDate = returnDate;
		this.fines = fines;
	}

	public Returns(String bookTitle, Student returnStudent,String issueDate, String returnDate, int fines) {
		super();
		this.bookTitle = bookTitle;
		this.returnStudent = returnStudent;
		this.returnDate = returnDate;
		this.fines = fines;
		this.issueDate=issueDate;
	}

	public Returns() {
		super();
	}

	public int getReturnId() {
		return returnId;
	}

	public void setReturnId(int returnId) {
		this.returnId = returnId;
	}

	public Student getReturnStudent() {
		return returnStudent;
	}

	public void setReturnStudent(Student returnStudent) {
		this.returnStudent = returnStudent;
	}

	public String getReturnDate() {
		return returnDate;
	}
	
	public String getBookTitle() {
		return bookTitle;
	}

	
	public String getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}

	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}

	public void setReturnDate(String returnDate) {
		this.returnDate = returnDate;
	}

	public int getFines() {
		return fines;
	}

	public void setFines(int fines) {
		this.fines = fines;
	}

	@Override
	public String toString() {
		return "Returns [returnId=" + returnId + ", bookTitle=" + bookTitle + ", issueDate=" + issueDate
				+ ", returnDate=" + returnDate + ", fines=" + fines + "]";
	}

}
