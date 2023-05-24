package com.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.library.entity.Fines;

public interface FInesRepository extends JpaRepository<Fines,Integer> {

}
