package org.github.aastrandemma.booklenderjpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class BookLoan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Integer id;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private boolean returned;

    @ManyToOne
    @JoinColumn(name = "borrower_id", nullable = false)
    private AppUser borrower;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    public BookLoan(AppUser borrower, Book book) {
        this.borrower = borrower;
        this.book = book;
        this.loanDate = LocalDate.now();
        this.dueDate = loanDate.plusDays(book.getMaxLoanDays());
    }

    public void addBookLoan(BookLoan bookLoan) {
        book.getBookLoans().add(bookLoan);
        borrower.getBookLoans().add(bookLoan);
    }

    public void removeBookLoan(BookLoan bookLoan) {
        book.getBookLoans().remove(bookLoan);
        borrower.getBookLoans().remove(bookLoan);
        setReturned(true);
    }
}