package com.library.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import com.library.entity.BookNumbers;
import com.library.entity.Books;
import com.library.entity.Issues;
import com.library.entity.Student;
import com.library.entity.ViewIssuedDetails;
import com.library.repository.StudentRepository;
import com.library.service.BookNumbersService;
import com.library.service.BooksService;
import com.library.service.IssuesService;
import com.library.service.StudentService;

@RestController
@RequestMapping("/student")
public class StudentController {

	@Autowired
	StudentService studentService;

	@Autowired
	StudentRepository repository;

	@Autowired
	BooksService booksService;

	@Autowired
	BookNumbersService numbersService;

	@Autowired
	IssuesService issuesService;

	@GetMapping("/index")
	public ModelAndView home(Model model, Principal principal) {
		Student student = getStudent(principal);
		model.addAttribute("name", student.getName());
		model.addAttribute("indexName", "Student Home");
		return new ModelAndView("student/Home");
	}

	Student getStudent(Principal principal) {
		String name = principal.getName();
		Student byEmail = repository.getByEmail(name);
		return byEmail;
	}

	@GetMapping("/viewBooks/{pageNo}")
	public ModelAndView viewbooksInLibrary(@PathVariable int pageNo, Model model, Principal principal) {
		Student student = getStudent(principal);
		model.addAttribute("name", student.getName());
		Pageable pageable = PageRequest.of(pageNo, 10);
		Page<Books> pages = booksService.getBooks(pageable);
		List<Books> content = pages.getContent();
		for (Books b : content) {
			b.setQuantity(bookQuantity(b));
		}
		if (pages.getNumberOfElements() == 0) {
			model.addAttribute("noDataFound", "true");
		} else {
			model.addAttribute("noDataFound", "false");
		}

		if (pages.getTotalPages() < 1) {
			model.addAttribute("noPagination", "true");
		} else {
			model.addAttribute("noPagination", "false");
		}
		model.addAttribute("totalPages", pages.getTotalPages());
		model.addAttribute("listOfBooks", content);
		model.addAttribute("indexName", "View Books -Student");
		return new ModelAndView("student/viewBooks");
	}

	private int bookQuantity(Books books) {
		int count = 0;
		List<BookNumbers> bookUsingIsbn = numbersService.getBookUsingIsbn(books.getIsbn());
		for (BookNumbers b : bookUsingIsbn) {
			if (b.getStatus().equals("library")) {
				count++;
			}
		}
		return count;
	}

	@PostMapping("/search")
	public ModelAndView view(@RequestParam(name = "bookid", defaultValue = "0") String bookid, Model model,
			Principal principal) {

		if (bookid.equals("")) {
			return new ModelAndView("redirect:/student/viewBooks/0");
		} else {
			model.addAttribute("name", getStudent(principal).getName());
			Books book = booksService.getBook(Integer.parseInt(bookid));
			if (book != null) {
				book.setQuantity(bookQuantity(book));
				model.addAttribute("obj", book);
				model.addAttribute("imgurl", book.getImageName());
			} else {
				model.addAttribute("noDataFound", "true");
				model.addAttribute("indexName", "View Books -Student");
				return new ModelAndView("student/viewBooks");
			}
		}
		model.addAttribute("indexName", "View Books -Student");
		return new ModelAndView("student/viewBook");
	}

	@GetMapping("/returnedBooks/{number}")
	public ModelAndView returnedBooks(Model model, Principal principal) {
		Student student = getStudent(principal);
		model.addAttribute("name", student.getName());
		model.addAttribute("indexName", "Returned Books -Student");
		return new ModelAndView("student/viewBooks");
	}

	@GetMapping("/issuedBooks/{number}")
	public ModelAndView issuedBooks(@PathVariable("number") int Number, Model model, Principal principal) {
		Student student = getStudent(principal);
		Pageable pageable = PageRequest.of(Number, 7);
		Page<Issues> pages = issuesService.getAllIssuesByStudent(student, pageable);
		if (pages.getNumberOfElements() == 0) {
			model.addAttribute("noDataFound", "true");
		} else {
			model.addAttribute("noDataFound", "false");
		}
		if (pages.getTotalPages() < 1) {
			model.addAttribute("noPagination", "true");
		} else {
			model.addAttribute("noPagination", "false");
		}
		List<ViewIssuedDetails> issueDetails = new ArrayList<>();
		List<Issues> content = pages.getContent();
		for (Issues e : content) {
			issueDetails.add(new ViewIssuedDetails(e.getIssueId(), e.getIssueDate(), e.getReturnDate(),
					e.getBookNumbers().getBookId(), e.getBookNumbers().getBooks().getTitle(),
					e.getIssueStudent().getHtno(), e.getIssueStudent().getName()));
		}
		model.addAttribute("totalPages", pages.getTotalPages());
		model.addAttribute("listOfIssues", issueDetails);
		model.addAttribute("name", student.getName());
		model.addAttribute("indexName", "Issued Books -Student");
		return new ModelAndView("student/issuedBooks");
	}

	@PostMapping("/searchIssueDetails")
	public ModelAndView viewissuedBook(@RequestParam("bookid") String bookId ,Model model,Principal principal) {
		Student student = getStudent(principal);
		BookNumbers bookNumbers=null;
		if(bookId.equals("")) {
			return new ModelAndView("redirect:/student/issuedBooks/0");
		}
		else {
			List<Issues> issues = student.getIssues();
			for( Issues i:issues) {
				if(i.getBookNumbers().getBookId()==Integer.parseInt(bookId)) {
					bookNumbers=i.getBookNumbers();
				}
			}
		}
		if(bookNumbers !=null) {
			model.addAttribute("viewFields","true");
			model.addAttribute("imageurl",bookNumbers.getBooks().getImageName());
			Issues e=bookNumbers.getIssues();
			ViewIssuedDetails viewIssuedDetails = new ViewIssuedDetails(e.getIssueId(), e.getIssueDate(), e.getReturnDate(),
					e.getBookNumbers().getBookId(), e.getBookNumbers().getBooks().getTitle(),
					e.getIssueStudent().getHtno(), e.getIssueStudent().getName());
			model.addAttribute("obj",viewIssuedDetails);
		}
		else {
			model.addAttribute("viewFields","false");
		}
		model.addAttribute("name", student.getName());
		model.addAttribute("indexName", "Viewing Issued Book Home");
		return new ModelAndView("student/issuedBook");
	}
	
//	@GetMapping("/viewStudentDetails")
//	public ModelAndView studentDetails(Model model, Principal principal) {
//		Student student = getStudent(principal);
//		model.addAttribute("name", student.getName());
//		model.addAttribute("indexName", "Student Details");
//		return new ModelAndView("student/viewBooks");
//	}

	@GetMapping("/viewStudentDetails")
	public ModelAndView searchStud(Model model, Principal principal) {
		Student student = getStudent(principal);
		model.addAttribute("obj", student);
		model.addAttribute("imgurl", student.getStudImage());
		model.addAttribute("name", student.getName());
		model.addAttribute("showEye", "true");
		model.addAttribute("indexName", "View Student  -Student");
		return new ModelAndView("student/viewStudentDetails");
	}

	@GetMapping("/searchStudent/viewPassord")
	public ModelAndView showPassword(Principal principal, Model model) {
		Student student = getStudent(principal);
		model.addAttribute("name", student.getName());
		student.setPassword(student.getRawPassword());
		model.addAttribute("imgurl", student.getStudImage());
		model.addAttribute("obj", student);
		model.addAttribute("showEye", "false");
		model.addAttribute("indexName", "View Student  -Admin");
		return new ModelAndView("student/viewStudentDetails");
	}

}
