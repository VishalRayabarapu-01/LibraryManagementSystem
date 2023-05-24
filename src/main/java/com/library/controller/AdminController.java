package com.library.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import com.library.entity.BookNumbers;
import com.library.entity.Books;
import com.library.entity.Fines;
import com.library.entity.Issues;
import com.library.entity.Returns;
import com.library.entity.Student;
import com.library.entity.User;
import com.library.entity.ViewIssuedDetails;
import com.library.repository.BookNumberRepository;
import com.library.repository.FInesRepository;
import com.library.repository.LoginUserRepository;
import com.library.repository.StudentRepository;
import com.library.service.BookNumbersService;
import com.library.service.BooksService;
import com.library.service.EmailSenderService;
import com.library.service.IssuesService;
import com.library.service.ReturnsService;
import com.library.service.StudentService;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	BooksService service;

	@Autowired
	StudentService studentService;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	LoginUserRepository loginUserRepository;

	@Autowired
	BookNumbersService bookNumbersService;

	@Autowired
	IssuesService issuesService;

	@Autowired
	StudentRepository repository;

	@Autowired
	BookNumberRepository numberRepository;

	@Autowired
	ReturnsService returnsService;

	@Autowired
	FInesRepository finesRepository;

	@Autowired
	EmailSenderService emailSenderService;

	@GetMapping("/index")
	public ModelAndView admin(Model model, HttpSession session) {
		String attribute = (String) session.getAttribute("email");
		if (attribute == null) {
			model.addAttribute("emailMessage", "Send");
		} else if (attribute.equals("sent")) {
			model.addAttribute("emailMessage", "Sent");
		}
		else if(attribute.equals("No students with deadline")) {
			model.addAttribute("emailMessage", "No students with deadline..");
		}
		else {
			model.addAttribute("emailMessage", "Error occured try again!!");
		}
		long sum = 0;
		List<Fines> findAll = finesRepository.findAll();
		for (Fines f : findAll) {
			sum += f.getFines();
		}
		model.addAttribute("students", studentService.getStudents().size());
		model.addAttribute("fines", sum);
		model.addAttribute("books", service.getBooks().size());
		model.addAttribute("issuedBooks", issuesService.getIssues().size());
		model.addAttribute("indexName", "Admin Home");
		model.addAttribute("currentPage", "Dashboard");
		return new ModelAndView("admin/Home");
	}

	@GetMapping("/sendMails/{number}")
	public ModelAndView sendmailApi(HttpSession httpSession) {

		String sendEmail = "";

		List<Issues> issues = issuesService.getIssues();
		for (Issues i : issues) {

			if (!i.getEmailSent().equals("No")) {
				continue;
			}
			String issueDateStr = i.getIssueDate();
			String returnDateStr = i.getReturnDate();

			String[] issueSplit = issueDateStr.split("-");
			String[] returnSplit = returnDateStr.split("-");

			int issueYear = Integer.parseInt(issueSplit[0]);
			int issueMonth = Integer.parseInt(issueSplit[1]);
			int issueDate = Integer.parseInt(issueSplit[2]);

			int returnYear = Integer.parseInt(returnSplit[0]);
			int returnMonth = Integer.parseInt(returnSplit[1]);
			int returnDate = Integer.parseInt(returnSplit[2]);

			LocalDate date1 = LocalDate.of(issueYear, issueMonth, issueDate);
			LocalDate date2 = LocalDate.of(returnYear, returnMonth, returnDate);

			long daysDifference = ChronoUnit.DAYS.between(date1, date2);

			if (daysDifference == 2) {
				String meaasge = "HurryUp ! Submit the book named " + i.getBookNumbers().getBooks().getTitle()
						+ " in your library with in 2 days or else you will be fined ₹"
						+ finesRepository.findById(1).get().getFineAmount() + " and increases per month..";
				sendEmail = emailSenderService.sendEmail(i.getIssueStudent().getEmail(), "Deadline!!!!", meaasge);
			} else if (daysDifference == 1) {
				String meaasge = "HurryUp ! Submit the book named " + i.getBookNumbers().getBooks().getTitle()
						+ " in your library with in a days or else you will be fined ₹"
						+ finesRepository.findById(1).get().getFineAmount() + " and increases per month..";
				sendEmail = emailSenderService.sendEmail(i.getIssueStudent().getEmail(), "Deadline!!!!", meaasge);
			} else if (daysDifference == 0) {
				String meaasge = "HurryUp ! Submit the book named " + i.getBookNumbers().getBooks().getTitle()
						+ " in your library today or else you will be fined ₹"
						+ finesRepository.findById(1).get().getFineAmount() + " and increases per month..";
				sendEmail = emailSenderService.sendEmail(i.getIssueStudent().getEmail(), "Deadline!!!!", meaasge);
			}
			if (sendEmail.equals("success")) {
				httpSession.setAttribute("email", "sent");
			}
			else if(sendEmail.equals("")) {
				httpSession.setAttribute("email", "No students with deadline");
			}
			else {
				httpSession.setAttribute("email", "Error occured..");
			}
		}
		return new ModelAndView("redirect:/admin/index");
	}

	@GetMapping("/addBook")
	public ModelAndView addbook(Model model) {
		model.addAttribute("indexName", "Add Book  -Admin");
		model.addAttribute("currentPage", "Add Book");
		model.addAttribute("obj", new Books());
		return new ModelAndView("admin/addBooks");
	}

	@PostMapping("/addBookDetails")
	public ModelAndView addBooktoDb(@ModelAttribute Books books, Model model, MultipartFile file) throws IOException {

		boolean allowToaddBook = true;

		if (file.isEmpty()) {
			books.setImageName("defaultBook.jpg");
		} else if ((file.getContentType().equals("image/jpg") || file.getContentType().equals("image/jpeg")
				|| file.getContentType().equals("image/png")) && (file.getSize() < 3145728)) {

			String fileName = giveDateAndTime() + file.getOriginalFilename();

			File saveFile = new ClassPathResource("/static/images").getFile();
			Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + fileName);

			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

			books.setImageName(fileName);
		} else {
			allowToaddBook = false;
			model.addAttribute("message", "File should be an image and less than 3 MB.");
		}

		if (allowToaddBook) {

			if (validateBookRecieved(books)) {
				int quantityOfBooks = books.getQuantity();
				for (int i = 0; i < quantityOfBooks; i++) {
					Random r = new Random();
					int bookid = r.nextInt(8489855);
					books.getBookLists().add(new BookNumbers(bookid, "library", books, null));
				}
				if (service.addBook(books)) {
					model.addAttribute("submitOk", "true");
				} else {
					model.addAttribute("message", "Error occured while adding details !");
				}
			} else {
				model.addAttribute("message", "Please enter all the fields !");
			}
		}
		model.addAttribute("obj", new Books());
		model.addAttribute("indexName", "Add Book  -Admin");
		model.addAttribute("currentPage", "Add Book");
		return new ModelAndView("admin/addBooks");
	}

	public String giveDateAndTime() {

		StringBuilder imageName = new StringBuilder();

		LocalTime time = LocalTime.now();
		LocalDate date = LocalDate.now();

		String[] dateArray = (date + "").split("-");
		for (String d : dateArray) {
			imageName.append(d);
		}
		imageName.append("_");

		String timeArray[] = (time + "").split(":");
		for (int i = 0; i < timeArray.length - 1; i++) {
			imageName.append(timeArray[i]);
		}
		imageName.append(timeArray[2].substring(0, 2));
		imageName.append("_");

		return new String(imageName);
	}

	// validation of add books . whether admin added all details or not.
	public boolean validateBookRecieved(Books books) {
		if (books.isEmpty()) {
			return false;
		}
		return true;
	}

	@GetMapping("/viewBooks/{pageNo}")
	public ModelAndView viewBooks(@PathVariable int pageNo, Model model) {
		Pageable pageable = PageRequest.of(pageNo, 8);
		Page<Books> pages = service.getBooks(pageable);
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
		model.addAttribute("listOfBooks", pages);
		model.addAttribute("indexName", "View Books  -Admin");
		model.addAttribute("currentPage", "View Books");
		return new ModelAndView("admin/viewBooks");
	}

	@GetMapping("/deleteBook/{delId}")
	public ModelAndView deleteBook(@PathVariable int delId, Model model) throws IOException {
		Books book = service.getBook(delId);
		String imageName = book.getImageName();
		if (!imageName.equals("defaultBook.jpg")) {

			// deleting the image.
			File deletingFile = new ClassPathResource("/static/images/" + imageName).getFile();
			deletingFile.delete();
		}
		service.deleteBooks(delId);
		return new ModelAndView("redirect:/admin/viewBooks/0");
	}

	@GetMapping("/searchBook/{number}")
	public ModelAndView view(@RequestParam(name = "bookid", defaultValue = "0") String bookid, Model model,
			@PathVariable("number") String Number) {

		if (!Number.equals("0")) {
			bookid = Number;
		}

		if (bookid.equals("")) {
			return new ModelAndView("redirect:/admin/viewBooks/0");
		} else {
			Books book = service.getBook(Integer.parseInt(bookid));
			if (book != null) {
				model.addAttribute("obj", book);
				model.addAttribute("imgurl", book.getImageName());
			} else {
				model.addAttribute("noDataFound", "true");
				model.addAttribute("indexName", "View Books  -Admin");
				model.addAttribute("currentPage", "View Books");
				return new ModelAndView("admin/viewBooks");
			}
		}
		model.addAttribute("indexName", "View Book  -Admin");
		model.addAttribute("currentPage", "Viewing Book");
		return new ModelAndView("admin/viewBook");
	}

	// student controllers .

	@GetMapping("/addstudent")
	public ModelAndView addStudent(Model model) {
		model.addAttribute("indexName", "Add Student  -Admin");
		model.addAttribute("currentPage", "Add Student");
		model.addAttribute("obj", new Student());
		return new ModelAndView("admin/addStudent");
	}

	@PostMapping("/addStudentDetails")
	public ModelAndView adddetails(@ModelAttribute Student student, Model model, MultipartFile file)
			throws IOException {

		boolean allowToaddStudent = true;

		if (file.isEmpty()) {
			student.setStudImage("defaultStudent.jpg");
		} else if ((file.getContentType().equals("image/jpg") || file.getContentType().equals("image/jpeg")
				|| file.getContentType().equals("image/png")) && (file.getSize() < 3145728)) {

			String fileName = student.getHtno() + "_" + file.getOriginalFilename();

			File saveFile = new ClassPathResource("/static/Student-Images").getFile();
			Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + fileName);

			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

			student.setStudImage(fileName);
		} else {
			allowToaddStudent = false;
			model.addAttribute("message", "File should be an image and less than 3 MB.");
		}
		if (allowToaddStudent) {
			// addding student to db.

			if (validateRecievedStudent(student)) {
				student.setRawPassword(student.getPassword());
				String password = encoder.encode(student.getPassword());
				student.setPassword(password);
				if (studentService.addStudent(student)) {
					loginUserRepository.save(new User(student.getEmail(), password, "ROLE_STUDENT"));
					model.addAttribute("submitOk", "Student details added...!");
				} else {
					model.addAttribute("message", "Error occured while adding student details !");
				}
			} else {
				model.addAttribute("message", "Please enter all the fields !");
			}
		}
		model.addAttribute("indexName", "Add Student  -Admin");
		model.addAttribute("currentPage", "Add Student");
		model.addAttribute("obj", new Student());
		return new ModelAndView("admin/addStudent");
	}

	public boolean validateRecievedStudent(Student student) {
		if (student.isEmpty()) {
			return false;
		}
		return true;
	}

	boolean showMessage = false;

	@GetMapping("/viewStudents/{pageNo}")
	public ModelAndView viewStudents(Model model, @PathVariable("pageNo") int pageNo) {
		Pageable pageable = PageRequest.of(pageNo, 4);
		Page<Student> pages = studentService.getStudents(pageable);
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

		if (showMessage) {
			// this indicates the student had books . we are trying to delete student.
			model.addAttribute("messageError", "Cannot delete student.there are books to submit.");
			showMessage = false;
		}
		model.addAttribute("currentPageNumber", pageNo);
		model.addAttribute("totalPages", pages.getTotalPages());
		model.addAttribute("listOfStudents", pages);
		model.addAttribute("indexName", "View Students  -Admin");
		model.addAttribute("currentPage", "View Students");
		return new ModelAndView("admin/viewStudents");
	}

	@GetMapping("/deleteStudent/{delId}/{pageNo}")
	public ModelAndView deleteStudent(@PathVariable String delId, Model model, @PathVariable("pageNo") int pageNo)
			throws IOException {
		Student student = studentService.getStudent(delId);
//		System.out.println(pageNo);
		if (student.getIssues().size() == 0) {
			String imageName = student.getStudImage();
			if (!imageName.equals("defaultStudent.jpg")) {
				// deleting the image.
				File deletingFile = new ClassPathResource("/static/Student-Images/" + imageName).getFile();
				deletingFile.delete();
			}
			studentService.deleteStudent(delId);
			// deleting the user from logging db
			User user = null;
			Optional<User> findByUser = loginUserRepository.findById(student.getEmail());
			if (findByUser.isPresent()) {
				user = findByUser.get();
			}
			loginUserRepository.delete(user);
		} else {
			showMessage = true;
		}
		return new ModelAndView("redirect:/admin/viewStudents/" + pageNo);
	}

	@GetMapping("/searchStudent/{number}")
	public ModelAndView searchStud(@RequestParam(name = "studentid", defaultValue = "0") String studId, Model model,
			@PathVariable("number") String Number) {

		if (!Number.equals("0")) {
			studId = Number;
		}
		if (studId.equals("")) {
			return new ModelAndView("redirect:/admin/viewStudents/0");
		} else {
			Student student = studentService.getStudent(studId);
			if (student != null) {
				model.addAttribute("obj", student);
				model.addAttribute("imgurl", student.getStudImage());
			} else {
				model.addAttribute("noDataFound", "true");
				model.addAttribute("indexName", "View Students  -Admin");
				model.addAttribute("currentPage", "View Students");
				return new ModelAndView("admin/viewStudents");
			}
		}
		model.addAttribute("showEye", "true");
		model.addAttribute("indexName", "View Student  -Admin");
		model.addAttribute("currentPage", "Viewing Student");
		return new ModelAndView("admin/viewStudent");
	}

	@GetMapping("/searchStudent/viewPassord/{number}")
	public ModelAndView showPassword(@PathVariable("number") String studId, Model model) {
		Student student = studentService.getStudent(studId);
		if (student != null) {
			model.addAttribute("obj", student);
			student.setPassword(student.getRawPassword());
			model.addAttribute("imgurl", student.getStudImage());
		} else {
			model.addAttribute("noDataFound", "true");
			model.addAttribute("indexName", "View Students  -Admin");
			model.addAttribute("currentPage", "View Students");
			return new ModelAndView("admin/viewStudents");
		}
		model.addAttribute("showEye", "false");
		model.addAttribute("indexName", "View Student  -Admin");
		model.addAttribute("currentPage", "Viewing Student");
		return new ModelAndView("admin/viewStudent");
	}

	// issue book started.

	@GetMapping("/issueBook")
	public ModelAndView issueBookPage(Model model) {
		model.addAttribute("indexName", "Issue Books -Admin");
		model.addAttribute("currentPage", "Issue Book");
		model.addAttribute("sideBarFullScreen", "true");
		return new ModelAndView("admin/IssueBook");
	}

	@PostMapping("/searchBook")
	public ModelAndView searchWithId(HttpSession session,
			@RequestParam(name = "bookId", defaultValue = "empty") String bookId, Model model) {
		if (bookId.equals("empty")) {
			model.addAttribute("messageError", "Please Enter book Unique Id....!");
			model.addAttribute("sideBarFullScreen", "true");
		} else {
			BookNumbers bookDetails = bookNumbersService.getBookDetails(Integer.parseInt(bookId));
//			if(bookDetails.getStatus().equals("issued")) {
//				model.addAttribute("messageError", "Book already issued to some student....!");
//			}
			if (bookDetails != null) {
				session.setAttribute("bookId", bookId);
				session.setAttribute("booksObj", bookDetails.getBooks());
				model.addAttribute("imgurl", bookDetails.getBooks().getImageName());
				model.addAttribute("showBookDetails", "true");
				model.addAttribute("obj", bookDetails.getBooks());
			} else {
				model.addAttribute("sideBarFullScreen", "true");
				model.addAttribute("messageError", "Please Enter valid book Unique Id....!");
			}
		}
		model.addAttribute("indexName", "Issue Books -Admin");
		model.addAttribute("currentPage", "Issue Book");
		return new ModelAndView("admin/IssueBook");
	}

	@PostMapping("/searchStudentIssue")
	public ModelAndView searchstud(HttpSession session,
			@RequestParam(name = "studHtno", defaultValue = "empty") String htno, Model model) {
		if (htno.equals("empty")) {
			model.addAttribute("sideBarFullScreen", "true");
			model.addAttribute("messageError", "Please Enter Student Hall Ticket Id....!");
		} else {
			Student student = studentService.getStudent(htno);
			if (student != null) {
				Books attribute = (Books) session.getAttribute("booksObj");
				session.removeAttribute("booksObj");
				model.addAttribute("obj", attribute);
				model.addAttribute("imgurl", attribute.getImageName());
				model.addAttribute("imageurl", student.getStudImage());
				model.addAttribute("showBookDetails", "true");
				model.addAttribute("showStudentDetails", "true");
				model.addAttribute("object", student);
			} else {
				model.addAttribute("sideBarFullScreen", "true");
				model.addAttribute("messageError", "Please Enter valid Student Hall Ticket Id....!");
			}
		}
		return new ModelAndView("admin/IssueBook");
	}

	@PostMapping("/issueBookToStudent")
	public ModelAndView issueBook(HttpSession session, @RequestParam("studId") String htno, Model model) {
		String attribute = (String) session.getAttribute("bookId");
		Student student = studentService.getStudent(htno);
		BookNumbers bookDetails = bookNumbersService.getBookDetails(Integer.parseInt(attribute));
		if (!bookDetails.getStatus().equals("issued")) {

			// accessing date.
			LocalDate todaysDate = LocalDate.now();
			String date = todaysDate + "";
			LocalDate After30Days = todaysDate.plusDays(30);
			String returnDate = After30Days + "";
			Issues issuesObj = new Issues(date, student, bookDetails, returnDate, "No");
			bookDetails.setStatus("issued");
			bookDetails.setIssues(issuesObj);
			boolean add = student.getIssues().add(issuesObj);
			if (add) {
				numberRepository.save(bookDetails);
				repository.save(student);
				model.addAttribute("message", "Book Accepted to issue...!");
			}
		} else {
			model.addAttribute("messageError", "Wrong id.This book is already issued...!");
		}
		session.removeAttribute("bookId");
		model.addAttribute("sideBarFullScreen", "true");
		model.addAttribute("indexName", "Issue Books -Admin");
		model.addAttribute("currentPage", "Issue Book");
		return new ModelAndView("admin/IssueBook");
	}

	@GetMapping("/viewIssuedBooks/{pageNo}")
	public ModelAndView viewIssuedBooks(@PathVariable("pageNo") int pageNo, Model model) {
		Pageable pageable = PageRequest.of(pageNo, 7);
		Page<Issues> pages = issuesService.getIssues(pageable);
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
		model.addAttribute("currentPageNumber", pageNo);
		model.addAttribute("indexName", "View Issued Books -Admin");
		model.addAttribute("currentPage", "Viewing Issued Book");
		return new ModelAndView("admin/viewIssuedBooks");
	}

	@PostMapping("/increaseIssueDate")
	public ModelAndView increaseIssue(@RequestParam("studId") String htno,
			@RequestParam("currentPageNumber") int currentPageNumber, @RequestParam("bookId") int bookId, Model model) {

		Student student = studentService.getStudent(htno);
		Issues issued = null;
		List<Issues> issues = student.getIssues();
		for (Issues e : issues) {
			if (e.getBookNumbers().getBookId() == bookId) {
				issued = e;
			}
		}
		if (issued != null) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate date = LocalDate.parse(issued.getReturnDate(), formatter);
			LocalDate Extend30Days = date.plusDays(30);
			issued.setReturnDate(Extend30Days + "");
			issuesService.saveIssueDetails(issued);
		}
		return new ModelAndView("redirect:/admin/viewIssuedBooks/" + currentPageNumber);
	}

	@PostMapping("/IssuedBook-booksearch")
	public ModelAndView booksearch(@RequestParam("currentPageNumber") int pageNo, Model model,
			@RequestParam("bookId") String bookNumber) {
		if (bookNumber.equals("")) {
			return new ModelAndView("redirect:/admin/viewIssuedBooks/" + pageNo);
		} else {
			int bookId = Integer.parseInt(bookNumber);
			BookNumbers bookDetails = bookNumbersService.getBookDetails(bookId);
			if (bookDetails != null) {
				Issues issues = bookDetails.getIssues();
				if (issues != null) {
					Student Student = issues.getIssueStudent();
					ViewIssuedDetails viewIssuedDetails = new ViewIssuedDetails(issues.getIssueId(),
							issues.getIssueDate(), issues.getReturnDate(), bookId, bookDetails.getBooks().getTitle(),
							Student.getHtno(), Student.getName());
					model.addAttribute("obj", viewIssuedDetails);
					model.addAttribute("viewFields", "true");
					model.addAttribute("imageurl", bookDetails.getBooks().getImageName());
				} else {
					model.addAttribute("viewFields", "false");
					model.addAttribute("messageError", "Book is not issued to any one....");
				}
			} else {
				model.addAttribute("viewFields", "false");
				model.addAttribute("messageError", "Entered Invalid Book Id please check....");
			}
		}
		model.addAttribute("indexName", "View Issued Details -Admin");
		model.addAttribute("currentPage", "Viewing Issued Book Details");
		return new ModelAndView("admin/viewIssuedBook");
	}

	// return book starts...

	@GetMapping("/returnBook")
	public ModelAndView returnPage(Model model) {
		model.addAttribute("indexName", "Return Book -Admin");
		model.addAttribute("currentPage", "Return Book");
		return new ModelAndView("admin/returnBook");
	}

	@PostMapping("/returnDetails")
	public ModelAndView returnBook(@RequestParam("bookId") String bookId, @RequestParam("studId") String htno,
			Model model) {
		int fines = 0;

		if (bookId.equals("") || htno.equals("")) {
			model.addAttribute("messageError", "Enter details and click button");
		} else {
			BookNumbers bookDetails = bookNumbersService.getBookDetails(Integer.parseInt(bookId));
			if (bookDetails != null) {
				Student student = studentService.getStudent(htno);
				if (student != null) {
					Issues issues = bookDetails.getIssues();
					if (issues != null) {
						if (issues.getIssueStudent().getHtno().equals(htno)) {

							// calculating fine
							String[] split = issues.getReturnDate().split("-");
							int year = Integer.parseInt(split[0]);
							int month = Integer.parseInt(split[1]);
							int date = Integer.parseInt(split[2]);

							LocalDate date1 = LocalDate.of(year, month, date); // return date
							LocalDate date2 = LocalDate.now(); // current date

							String todaysDate = date2 + "";

							long daysDifference = ChronoUnit.DAYS.between(date1, date2); // Calculate difference in days
							if (daysDifference > 0) {
								Optional<Fines> byId = finesRepository.findById(1);
								Fines fines2 = byId.get();
								if (daysDifference <= 30) {
									fines = fines2.getFineAmount();
								} else {
									fines = (int) ((daysDifference / 30) * fines2.getFineAmount());
								}
								model.addAttribute("messageError", "Take Book and Collect Fine :" + fines);
							}
							bookDetails.setStatus("library");
							bookDetails.setIssues(null);

							issues.setBookNumbers(null);
							issues.setIssueStudent(null);

							numberRepository.save(bookDetails);
							issuesService.deleteIssue(issues);

							Returns returns = new Returns(bookDetails.getBooks().getTitle(), student,
									issues.getIssueDate(), todaysDate, fines);
							returnsService.addReturnDetails(returns);

							model.addAttribute("message", "Book Returned");

						} else {
							model.addAttribute("messageError", "Book not given to that student");
						}

					} else {
						model.addAttribute("messageError", "Book not yet issued.How can you return");
					}

				} else {
					model.addAttribute("messageError", "Student doesnt exist..");
				}
			} else {
				model.addAttribute("messageError", "Book id doesnt exist..");
			}
		}
		model.addAttribute("indexName", "Return Book -Admin");
		model.addAttribute("currentPage", "Return Book");
		return new ModelAndView("admin/returnBook");
	}

	// manage Fines...
	@GetMapping("/manageFines")
	public ModelAndView manageFines(Model model) {
		Optional<Fines> findById = finesRepository.findById(1);
		model.addAttribute("fine", findById.get().getFineAmount());
		model.addAttribute("indexName", "Fines -Admin");
		model.addAttribute("currentPage", "Manage Fines");
		return new ModelAndView("admin/fines");
	}

	@PostMapping("/changeFineAmount")
	public ModelAndView change(@RequestParam("fineAmount") String amount, Model model) {
		Optional<Fines> findById = finesRepository.findById(1);
		Fines fines = findById.get();
		if (amount.equals("")) {
			model.addAttribute("messageError", "Please enter amount and click button...");
		} else {
			fines.setFineAmount(Integer.parseInt(amount));
			finesRepository.save(fines);
			model.addAttribute("message", "Amount changed....!");
		}
		model.addAttribute("fine", fines.getFineAmount());
		model.addAttribute("indexName", "Fines -Admin");
		model.addAttribute("currentPage", "Manage Fines");
		return new ModelAndView("admin/fines");
	}

}