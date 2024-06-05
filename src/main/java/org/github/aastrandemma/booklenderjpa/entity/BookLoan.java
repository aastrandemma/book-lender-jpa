package org.github.aastrandemma.booklenderjpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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
        if (!book.isAvailable()) throw new IllegalArgumentException("Book: " + book.getTitle() + " is not available");
        this.borrower = borrower;
        this.book = book;
        this.loanDate = LocalDate.now();
        this.dueDate = loanDate.plusDays(book.getMaxLoanDays());
        addBookLoan(this);
    }

    public void addBookLoan(BookLoan bookLoan) {
        book.getBookLoans().add(bookLoan);
        borrower.getBookLoans().add(bookLoan);
        bookLoan.getBook().setAvailable(false);
    }

    public void removeBookLoan(BookLoan bookLoan) {
        book.getBookLoans().remove(bookLoan);
        borrower.getBookLoans().remove(bookLoan);
        setReturned(true);
        bookLoan.getBook().setAvailable(true);
    }

    public String newBookLoanMsg() {
        StringBuilder sb = new StringBuilder();

        sb.append("\n###########################\n")
                .append("Book loan: ").append(this.id)
                .append("\nLoan date: ").append(this.loanDate)
                .append("\nDue date: ").append(this.dueDate)
                .append("\nBorrower: ").append(this.borrower.getUsername())
                .append("\n###########################\n");
        return sb.toString();
    }

    public String returnBookLoanMsg() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n###########################\n")
        .append("Returned book loan: ").append(this.id);

        if (getDaysLate() > 0) {
            sb.append("\n########## OBS! ##########\n")
                    .append("LATE RETURN\n")
                    .append(getDaysLate())
                    .append(" days late");
        }

        sb.append("\n###########################\n");
        return sb.toString();
    }

    private long getDaysLate() {
        if (this.dueDate.isAfter(LocalDate.now())) {
            return 0;
        }
        return ChronoUnit.DAYS.between(dueDate, LocalDate.now());
    }
}