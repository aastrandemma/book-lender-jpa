package org.github.aastrandemma.booklenderjpa;

import org.github.aastrandemma.booklenderjpa.entity.*;
import org.github.aastrandemma.booklenderjpa.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
public class MyCommandLineRunner implements CommandLineRunner {
    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    DetailsRepository detailsRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    BookLoanRepository bookLoanRepository;

    @Autowired
    AuthorRepository authorRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Details testDetails = detailsRepository.save(new Details("jane@doe.com", "Jane Doe", LocalDate.of(2000, 1, 1)));
        AppUser testAppuser = appUserRepository.save(new AppUser("test_username", "test_password", testDetails));
        Book testBook = bookRepository.save(new Book("testIsbn12345", "test_title", 7));
        BookLoan bookLoan = new BookLoan(testAppuser, testBook);
        bookLoanRepository.save(bookLoan);
        Author testAuthor = authorRepository.save(new Author("testFirstName", "testLastName"));
        testBook.addAuthorToBook(testAuthor);
        authorRepository.save(testAuthor);

        System.out.println(bookLoan.newBookLoanMsg());
        bookLoan.setDueDate(LocalDate.of(2024, 5, 1));
        System.out.println(bookLoan.returnBookLoanMsg());
    }
}