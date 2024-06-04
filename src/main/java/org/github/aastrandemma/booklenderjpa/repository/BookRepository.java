package org.github.aastrandemma.booklenderjpa.repository;

import org.github.aastrandemma.booklenderjpa.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findByIsbnIgnoreCase(String isbn);
    List<Book> findByTitleContaining(String titleContaining);
    List<Book> findByMaxLoanDaysLessThan(int maxLoanDays);

    @Query("SELECT b FROM Book b JOIN b.bookLoans bl WHERE bl.returned = FALSE")
    List<Book> findBooksCurrentlyOnLoan();

    @Query("SELECT b FROM Book b JOIN b.bookLoans bl WHERE bl.returned = FALSE AND bl.dueDate < CURRENT_DATE")
    List<Book> findOverdueBooks();

    @Query("SELECT b FROM Book b JOIN b.bookLoans bl WHERE bl.loanDate BETWEEN :startDate AND :endDate")
    List<Book>findBooksLoanDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}