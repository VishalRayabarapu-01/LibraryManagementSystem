package com.library.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Books {

	@Id
	private int isbn;

	private String author;

	private String title;

	private String publisher;

	private int edition;

	private String category;

	private int cost;
	
	private String imageName;
	
	private int quantity;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "books")
	@JsonManagedReference
	private List<BookNumbers> bookLists = new ArrayList<>();

	public Books() {
		super();
	}

//
//	public Books(int isbn) {
//		super();
//		this.isbn=isbn;
//	} 
//	
	public Books(int isbn, String author,String imageName, String title, String publisher, int edition, String category, int cost,
			int quantity) {
		super();
		this.isbn = isbn;
		this.author = author;
		this.title = title;
		this.publisher = publisher;
		this.edition = edition;
		this.category = category;
		this.cost = cost;
		this.quantity = quantity;
		this.imageName=imageName;
	}

	public Books(int isbn, String author, String title, String publisher, int edition, String category, int cost,
			int quantity, List<BookNumbers> bookLists,String imageName) {
		super();
		this.isbn = isbn;
		this.author = author;
		this.title = title;
		this.publisher = publisher;
		this.edition = edition;
		this.category = category;
		this.cost = cost;
		this.quantity = quantity;
		this.bookLists = bookLists;
		this.imageName=imageName;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}


	@Override
	public String toString() {
		return "Books [isbn=" + isbn + ", author=" + author + ", title=" + title + ", publisher=" + publisher
				+ ", edition=" + edition + ", category=" + category + ", cost=" + cost + ", imageName=" + imageName
				+ ", quantity=" + quantity + "]";
	}

	public List<BookNumbers> getBookLists() {
		return bookLists;
	}

	public void setBookLists(List<BookNumbers> bookLists) {
		this.bookLists = bookLists;
	}

	public int getIsbn() {
		return isbn;
	}

	public void setIsbn(int isbn) {
		this.isbn = isbn;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public int getEdition() {
		return edition;
	}

	public void setEdition(int edition) {
		this.edition = edition;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public boolean isEmpty() {
		
		boolean isbn=(this.isbn==0);
		
		boolean author=(this.author.equals(""));

		boolean title=(this.title.equals(""));

		boolean publisher=(this.publisher.equals(""));

		boolean edition=(this.edition==0);

		boolean category=(this.category.equals(""));

		boolean cost=(this.cost==0);

		boolean quantity=(this.quantity==0);
		
		if(isbn || author || title || publisher || edition || category || cost || quantity) {
			return true;
		}

		return false;
	}
}
