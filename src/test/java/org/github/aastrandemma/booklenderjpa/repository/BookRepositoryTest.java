package org.github.aastrandemma.booklenderjpa.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
public class BookRepositoryTest {
	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private BookLoanRepository bookLoanRepository;
	@Autowired
	private AppUserRepository appUserRepository;

	private Book bookOne;
	private Book bookTwo;
	private Book bookThree;
	private Book bookOneCopy;
	private BookLoan bookTwoLoan;
	private AppUser borrower;

	@BeforeEach
	void setUp() {
		bookOne = bookRepository.save(new Book("testIsbn12345", "Title of Test BookOne", 7));
		bookTwo = bookRepository.save(new Book("testIsbn56789", "Title of Test BookTwo", 14));
		bookThree = bookRepository.save(new Book("testIsbn98765", "Title of Test BookThree", 14));
		bookOneCopy = bookRepository.save(new Book("testIsbn12345", "Title of Test BookOne", 7));
		borrower = appUserRepository.save(new AppUser("Test_BookTwoBorrower", "Test_BookTwoBorrowerPassword"));
		bookTwoLoan = bookLoanRepository.save(new BookLoan(borrower, bookTwo));
	}

	@Test
	@Transactional
	public void testFindByIsbnIgnoreCase() {
		String isbn = "testIsbn12345";
		List<Book> expected = new ArrayList<>(Arrays.asList(bookOne, bookOneCopy));
		List<Book> actual = bookRepository.findByIsbnIgnoreCase(isbn);

		assertNotNull(expected);
		assertNotNull(actual);
		assertEquals(expected, actual);
	}

	@Test
	@Transactional
	public void testFindByIsbnIgnoreCaseExpectEmptyList() {
		String isbn = "testNotExistingIsbn";
		List<Book> expected = new ArrayList<>();
		List<Book> actual = bookRepository.findByIsbnIgnoreCase(isbn);

		assertEquals(expected, actual);
	}

	@Test
	@Transactional
	public void testFindByTitleContainingAndFindAll() {
		String titleContaining = "Title";
		List<Book> expected = bookRepository.findAll();
		List<Book> actual = bookRepository.findByTitleContaining(titleContaining);

		assertNotNull(expected);
		assertNotNull(actual);
		assertEquals(expected, actual);
	}

	@Test
	@Transactional
	public void testFindByTitleContaining() {
		String titleContaining = "BookThree";
		List<Book> expected = new ArrayList<>(Collections.singletonList(bookThree));
		List<Book> actual = bookRepository.findByTitleContaining(titleContaining);

		assertNotNull(expected);
		assertNotNull(actual);
		assertEquals(expected, actual);
	}

	@Test
	@Transactional
	public void testFindByMaxLoanDaysLessThan() {
		int maxLoanDays = 10;
		List<Book> expected = new ArrayList<>(Arrays.asList(bookOne, bookOneCopy));
		List<Book> actual = bookRepository.findByMaxLoanDaysLessThan(maxLoanDays);

		assertNotNull(expected);
		assertNotNull(actual);
		assertEquals(expected, actual);
	}

	@Test
	@Transactional
	public void testFindBooksCurrentlyOnLoan() {
		List<Book> expected = new ArrayList<>(Collections.singletonList(bookTwo));
		List<Book> actual = bookRepository.findBooksCurrentlyOnLoan();

		assertNotNull(expected);
		assertNotNull(actual);
		assertEquals(expected, actual);
	}

	@Test
	@Transactional
	public void testFindOverdueBooks() {
		bookTwoLoan.setLoanDate(LocalDate.now().minusDays(30));
		bookTwoLoan.setDueDate(bookTwoLoan.getLoanDate().minusDays(bookTwo.getMaxLoanDays()));
		List<Book> expected = new ArrayList<>(Collections.singletonList(bookTwo));
		List<Book> actual = bookRepository.findOverdueBooks();

		assertNotNull(expected);
		assertNotNull(actual);
		assertEquals(expected, actual);
	}

	@Test
	@Transactional
	public void testFindBooksLoanDateBetween() {
		bookLoanRepository.save(new BookLoan(borrower, bookThree));
		bookLoanRepository.save(new BookLoan(borrower, bookOneCopy));
		LocalDate startDate = LocalDate.now().minusDays(2);
		LocalDate endDate = LocalDate.now().plusDays(2);
		List<Book> expected = new ArrayList<>(Arrays.asList(bookTwo, bookThree, bookOneCopy));
		List<Book> actual = bookRepository.findBooksLoanDateBetween(startDate, endDate);

		assertNotNull(expected);
		assertNotNull(actual);
		assertEquals(expected, actual);
	}
}