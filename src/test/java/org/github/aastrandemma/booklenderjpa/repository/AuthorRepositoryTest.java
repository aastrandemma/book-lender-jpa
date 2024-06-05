package org.github.aastrandemma.booklenderjpa.repository;

import org.github.aastrandemma.booklenderjpa.entity.Author;
import org.github.aastrandemma.booklenderjpa.entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class AuthorRepositoryTest {
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookRepository bookRepository;

    private Book bookOne;
    private Book bookTwo;
    private Author authorOne;
    private Author authorTwo;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void setUp() {
        bookOne = bookRepository.save(new Book("testIsbn12345", "Title of Test BookOne", 7));
        bookTwo = bookRepository.save(new Book("testIsbn56789", "Title of Test BookTwo", 14));
        authorOne = authorRepository.save(new Author("Test_AuthorFirstNameOne", "Test_AuthorLastNameOne"));
        authorTwo = authorRepository.save(new Author("Test_AuthorFirstNameTwo", "Test_AuthorLastNameTwo"));
    }

    @Test
    @Transactional
    public void testFindByWrittenBooks_Id() {
        authorOne.addBook(bookOne);
        Integer bookId = bookOne.getId();

        List<Author> expected = new ArrayList<>(Collections.singletonList(authorOne));
        List<Author> actual = authorRepository.findByWrittenBooks_Id(bookId);

        assertNotNull(expected);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    public void testFindByFirstNameOrLastNameContaining() {
        String keyword = "AUTHOR";
        List<Author> expected = new ArrayList<>(Arrays.asList(authorOne, authorTwo));
        List<Author> actual = authorRepository.findByFirstNameOrLastNameContaining(keyword);

        assertNotNull(expected);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    public void testFindByLastName() {
        String lastName = authorTwo.getLastName();
        List<Author> expected = new ArrayList<>(Collections.singletonList(authorTwo));
        List<Author> actual = authorRepository.findByLastName(lastName);

        assertNotNull(expected);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    public void testFindByFirstName() {
        String firstName = authorTwo.getFirstName();
        List<Author> expected = new ArrayList<>(Collections.singletonList(authorTwo));
        List<Author> actual = authorRepository.findByFirstName(firstName);

        assertNotNull(expected);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    public void testUpdateAuthorById() {
        int authorId = authorOne.getId();
        authorRepository.updateAuthorById("Update_FirstName", "Update_LastName", authorId);

        Author authorOneCopy = authorOne;
        authorOneCopy.setFirstName("Update_FirstName");
        authorOneCopy.setLastName("Update_LastName");

        Optional<Author> expected = Optional.of(authorOneCopy);
        Optional<Author> actual = authorRepository.findById(authorId);

        assertNotNull(expected);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    public void testDeleteAuthorById() {
        int authorId = authorOne.getId();

        authorRepository.deleteById(authorId);
        assertFalse(authorRepository.findById(authorId).isPresent());
    }
}