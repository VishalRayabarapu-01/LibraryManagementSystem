package com.library.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.library.entity.Books;

public interface BookRepository extends JpaRepository<Books,Integer> {

	public Page<Books> findAll(Pageable pageable);
}
