package com.library.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.entity.BookNumbers;
import com.library.repository.BookNumberRepository;
import com.library.service.BookNumbersService;

@Service
public class BookNumbersServiceImpl implements BookNumbersService {

	@Autowired
	BookNumberRepository repository;

	@Override
	public BookNumbers getBookDetails(int bookId) {
		Optional<BookNumbers> findById = repository.findById(bookId);
		if(findById.isPresent()) {
			return findById.get();
		}
		return null;
	}

	@Override
	public List<BookNumbers> getBookUsingIsbn(int isbn) {
		List<BookNumbers> findAll = repository.findAll();
		List<BookNumbers> books=new ArrayList<>();
		for(BookNumbers b: findAll) {
			if(isbn== b.getBooks().getIsbn()) {
				books.add(b);
			}
		}
		return books;
	}
}
