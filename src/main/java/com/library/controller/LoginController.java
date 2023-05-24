package com.library.controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.library.entity.User;
import com.library.repository.LoginUserRepository;

@RestController
public class LoginController {

	@Autowired
	LoginUserRepository repository;
	
	// If we get invalid credentials the above end point executes.
//	@GetMapping("/login/{id}")
//	public ModelAndView logins(@PathVariable String id, Model model) {
//		// System.out.println(request.getParameter("submitButton"));
//		if (Integer.parseInt(id) != 0) {
//			model.addAttribute("error", "invaldid");
//		}
//		return new ModelAndView("Login");
//	}
	
	@GetMapping("/signin")
	public ModelAndView loginmethod(Model model) {
		model.addAttribute("indexName","Logins");
		return new ModelAndView("Login");
	}

	@GetMapping("/validateLoginUser")
	public ModelAndView vaidate(Principal principal,Model model) {
		User user=null;
		Optional<User> optionalUser = repository.findById(principal.getName());
		if(optionalUser.isPresent()) {
			user=optionalUser.get();
		}
		if(user.getRole().equals("ROLE_ADMIN")) {
			return new ModelAndView("redirect:/admin/index");
		}
		else {
			return new ModelAndView("redirect:/student/index");
		}
	}
}
