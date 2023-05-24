package com.library.service;

import org.springframework.stereotype.Service;

import com.library.entity.Returns;

@Service
public interface ReturnsService {

	boolean addReturnDetails(Returns returns);
}
