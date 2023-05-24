package com.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.library.entity.BookNumbers;

public interface BookNumberRepository extends JpaRepository<BookNumbers,Integer>{

}
