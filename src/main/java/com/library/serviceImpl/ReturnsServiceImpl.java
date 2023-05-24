package com.library.serviceImpl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.entity.Returns;
import com.library.repository.ReturnsRepository;
import com.library.service.ReturnsService;

@Service
public class ReturnsServiceImpl implements ReturnsService {

	@Autowired
	ReturnsRepository repository;

	@Override
	public boolean addReturnDetails(Returns returns) {
		repository.save(returns);
		return true;
	}


}
