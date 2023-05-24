package com.library.entity;

public class ViewIssuedDetails {

	private long issueId;

	private String issueDate;

	private String returnDate;
	
	private int  bookId;
	
	private String title;
	
	private String studId;

	private String name;

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

	public String getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(String returnDate) {
		this.returnDate = returnDate;
	}

	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStudId() {
		return studId;
	}

	public void setStudId(String studId) {
		this.studId = studId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ViewIssuedDetails() {
		super();
	}

	public ViewIssuedDetails(long issueId, String issueDate, String returnDate, int bookId, String title, String studId,
			String name) {
		super();
		this.issueId = issueId;
		this.issueDate = issueDate;
		this.returnDate = returnDate;
		this.bookId = bookId;
		this.title = title;
		this.studId = studId;
		this.name = name;
	}
	
}
