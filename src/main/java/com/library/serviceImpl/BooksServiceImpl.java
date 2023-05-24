package com.library.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.library.entity.Books;
import com.library.repository.BookRepository;
import com.library.service.BooksService;

@Service
public class BooksServiceImpl implements BooksService {

	@Autowired
	BookRepository repository;

	@Override
	public boolean addBook(Books books) {
		boolean flag = validateBook(books);
		if (!flag) {
			Books bookSaved = repository.save(books);
			if (bookSaved.toString().equals(books.toString())) {
				return true;
			}
		}
		return false;
	}

	private boolean validateBook(Books books) {
		Optional<Books> findById = repository.findById(books.getIsbn());
		if (findById.isPresent()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean deleteBooks(int isbn) {
		Optional<Books> findById = repository.findById(isbn);
		if (findById.isPresent()) {
			Books deleteBook = findById.get();
			repository.delete(deleteBook);
			return true;
		}
		return false;
	}

	@Override
	public boolean updatBooks(Books books) {
//		boolean flag=validateBook(books);
//		if(flag) {
//			boolean deleteBooks = deleteBooks(books.getIsbn());
//			if(deleteBooks) {
//				repository.save(books);
//			}
//		}
//		return false;
//
//		boolean flag = validateBook(books);
//		if (flag) {
//			Books book = getBook(books.getIsbn());
//			repository.save(books);
//			return true;
//		}
		return false;

	}

	@Override
	public Page<Books> getBooks(Pageable pageable) {
		return repository.findAll(pageable);
	}

	@Override
	public Books getBook(int isbn) {
		Optional<Books> findById = repository.findById(isbn);
		if (findById.isPresent()) {
			Books viewBook = findById.get();
			return viewBook;
		}
		return null;
	}

	@Override
	public List<Books> getBooks() {
		return repository.findAll();
	}

}
