package com.library.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.library.entity.Books;

@Service
public interface BooksService {

	boolean addBook(Books books);

	boolean deleteBooks(int isbn);

	boolean updatBooks(Books books);

	Page<Books> getBooks(Pageable pageable);

	List<Books> getBooks();

	Books getBook(int isbn);

}
