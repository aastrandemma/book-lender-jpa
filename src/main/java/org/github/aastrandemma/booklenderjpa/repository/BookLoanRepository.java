package org.github.aastrandemma.booklenderjpa.repository;

import org.github.aastrandemma.booklenderjpa.entity.BookLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookLoanRepository extends JpaRepository<BookLoan, Integer> {
    List<BookLoan> findByBorrower_Id(int borrowerId);

    List<BookLoan> findByBook_Id(int bookId);

    List<BookLoan> findByReturnedIsFalse();

    List<BookLoan> findByDueDateIsBeforeAndReturnedIsFalse(LocalDate date);

    List<BookLoan> findByLoanDateBetween(LocalDate startDate, LocalDate endDate);

    @Modifying
    @Query("UPDATE BookLoan bl SET bl.returned = TRUE WHERE bl.id = :loanId")
    void updateBookLoanReturnedById(@Param("loanId") int loanId);
}