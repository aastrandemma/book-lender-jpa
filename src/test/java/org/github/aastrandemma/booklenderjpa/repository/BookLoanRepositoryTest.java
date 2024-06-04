package org.github.aastrandemma.booklenderjpa.repository;

import java.util.*;

import org.github.aastrandemma.booklenderjpa.entity.AppUser;
import org.github.aastrandemma.booklenderjpa.entity.Book;
import org.github.aastrandemma.booklenderjpa.entity.BookLoan;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@DataJpaTest
public class BookLoanRepositoryTest {
	@Autowired
	private BookLoanRepository bookLoanRepository;
	@Autowired
	private AppUserRepository appUserRepository;
	@Autowired
	private BookRepository bookRepository;

	private BookLoan bookLoanOne;
	private BookLoan bookLoanTwo;
	private BookLoan bookLoanThree;
	private Book bookOne;
	private Book bookTwo;
	private Book bookThree;
	private AppUser borrowerOne;
	private AppUser borrowerTwo;

	@BeforeEach
	void setUp() {
		bookOne = bookRepository.save(new Book("testIsbn12345", "Title of Test BookOne", 7));
		bookTwo = bookRepository.save(new Book("testIsbn56789", "Title of Test BookTwo", 14));
		bookThree = bookRepository.save(new Book("testIsbn98765", "Title of Test BookThree", 14));
		borrowerOne = appUserRepository.save(new AppUser("Test_BorrowerOne", "Test_BorrowerOnePassword"));
		borrowerTwo = appUserRepository.save(new AppUser("Test_BorrowerTwo", "Test_BorrowerTwoPassword"));
		bookLoanOne = bookLoanRepository.save(new BookLoan(borrowerOne, bookOne));
		bookLoanTwo = bookLoanRepository.save(new BookLoan(borrowerTwo, bookTwo));
		bookLoanThree = bookLoanRepository.save(new BookLoan(borrowerOne, bookThree));
	}

	@Test
	@Transactional
	public void testFindByBorrower_Id() {
		int borrowerId = borrowerOne.getId();
		List<BookLoan> expected = new ArrayList<>(Arrays.asList(bookLoanOne, bookLoanThree));
		List<BookLoan> actual = bookLoanRepository.findByBorrower_Id(borrowerId);

		assertNotNull(expected);
		assertNotNull(actual);
		assertEquals(expected, actual);
	}

	@Test
	@Transactional
	public void testFindByBook_Id() {
		int bookId = bookOne.getId();
		List<BookLoan> expected = new ArrayList<>(Collections.singletonList(bookLoanOne));
		List<BookLoan> actual = bookLoanRepository.findByBook_Id(bookId);

		assertNotNull(expected);
		assertNotNull(actual);
		assertEquals(expected, actual);
	}

	@Test
	@Transactional
	public void testFindByReturnedIsFalseAndFindAll() {
		List<BookLoan> expected = bookLoanRepository.findAll();
		List<BookLoan> actual = bookLoanRepository.findByReturnedIsFalse();

		assertNotNull(expected);
		assertNotNull(actual);
		assertEquals(expected, actual);
	}

	@Test
	@Transactional
	public void testFindByReturnedIsFalse() {
		List<BookLoan> expected = new ArrayList<>(Arrays.asList(bookLoanOne, bookLoanThree));
		bookLoanTwo.removeBookLoan(bookLoanTwo);
		List<BookLoan> actual = bookLoanRepository.findByReturnedIsFalse();

		assertNotNull(expected);
		assertNotNull(actual);
		assertEquals(expected, actual);
	}

	@Test
	@Transactional
	public void testFindByDueDateIsBeforeAndReturnedIsFalse() {
		LocalDate date = bookLoanOne.getLoanDate().plusDays(14);
		List<BookLoan> expected = new ArrayList<>(Collections.singletonList(bookLoanOne));
		List<BookLoan> actual = bookLoanRepository.findByDueDateIsBeforeAndReturnedIsFalse(date);

		assertNotNull(expected);
		assertNotNull(actual);
		assertEquals(expected, actual);
	}

	@Test
	@Transactional
	public void testFindByLoanDateBetween() {
		bookLoanOne.setLoanDate(LocalDate.now().minusYears(10));

		LocalDate startDate = LocalDate.now().minusDays(2);
		LocalDate endDate = LocalDate.now().plusDays(2);

		List<BookLoan> expected = new ArrayList<>(Arrays.asList(bookLoanTwo, bookLoanThree));
		List<BookLoan> actual = bookLoanRepository.findByLoanDateBetween(startDate, endDate);

		assertNotNull(expected);
		assertNotNull(actual);
		assertEquals(expected, actual);
	}

	@Test
	@Transactional
	public void testUpdateBookLoanReturnedById() {
		int loanId = bookLoanOne.getId();
		bookLoanRepository.updateBookLoanReturnedById(loanId);

		BookLoan copyBookLoanOne = bookLoanOne;
		copyBookLoanOne.setReturned(true);

		Optional<BookLoan> expected = Optional.of(copyBookLoanOne);
		Optional<BookLoan> actual = bookLoanRepository.findById(loanId);

		assertNotNull(expected);
		assertNotNull(actual);
		assertEquals(expected, actual);
	}
}