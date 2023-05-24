package com.library.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.library.entity.BookNumbers;

@Service
public interface BookNumbersService {

	public BookNumbers getBookDetails(int bookId);
	
	List<BookNumbers> getBookUsingIsbn(int isbn);
}
