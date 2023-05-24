package com.library.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.library.entity.User;
import com.library.repository.LoginUserRepository;

public class CustomUserDetailsService implements UserDetailsService{
	
	@Autowired
	LoginUserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user;
		
		Optional<User> optionalUser = userRepository.findById(username);
		if(optionalUser.isEmpty()) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}else {
			user=optionalUser.get();
		}
		
		CustomUserDetails customUserDetails=new CustomUserDetails(user);
		return  customUserDetails;
	}

}
