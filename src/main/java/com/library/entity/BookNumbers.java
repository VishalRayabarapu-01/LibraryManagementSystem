package com.library.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class BookNumbers {

	@Id
	private int  bookId;
	
	private String status;
	
	@ManyToOne
	@JsonBackReference
	Books books;
	
	public BookNumbers(int bookId, String status, Books books, Issues issues) {
		super();
		this.bookId = bookId;
		this.status = status;
		this.books = books;
		this.issues = issues;
	}

	@OneToOne
	Issues issues;

	public BookNumbers(int bookId, String status, Books books) {
		super();
		this.bookId = bookId;
		this.status = status;
		this.books = books;
	}

	public BookNumbers() {
		super();
	}

	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Books getBooks() {
		return books;
	}

	public Issues getIssues() {
		return issues;
	}

	public void setIssues(Issues issues) {
		this.issues = issues;
	}

	public void setBooks(Books books) {
		this.books = books;
	}
	

	// i kept in comments because tostring is called infinetely so stackoverflow exception is 
	// occured while adding book and bookNumbers to db.
	
//	@Override
//	public String toString() {
//		return "BookNumbers [bookId=" + bookId + ", status=" + status + ", books=" + books + "]";
//	}
	
	
}
