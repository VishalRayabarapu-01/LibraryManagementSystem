package com.library.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/Library")
public class HomeController {

	@GetMapping("/home")
	public ModelAndView home(Model model) {
		model.addAttribute("indexName","Home");
		return new ModelAndView("index");
	}
	
	@GetMapping("/contact")
	public ModelAndView contacts(Model model) {
		model.addAttribute("indexName","Contact Us");
		return new ModelAndView("contacts");
	}

	
}
