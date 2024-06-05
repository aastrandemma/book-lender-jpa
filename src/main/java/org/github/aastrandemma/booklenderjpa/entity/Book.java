package org.github.aastrandemma.booklenderjpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Integer id;

    @Column(nullable = false, length = 13)
    private String isbn;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer maxLoanDays;

    private boolean isAvailable;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private Set<BookLoan> bookLoans = new HashSet<>();

    @ManyToMany(mappedBy = "writtenBooks")
    private Set<Author> authors = new HashSet<>();

    public Book(String isbn, String title, Integer maxLoanDays) {
        this.isbn = isbn;
        this.title = title;
        this.maxLoanDays = maxLoanDays;
        this.isAvailable = true;
    }

    public void addAuthorToBook(Author author) {
        author.getWrittenBooks().add(this);
        authors.add(author);
    }

    public void removeAuthorFromBook(Author author) {
       author.getWrittenBooks().remove(this);
       authors.remove(author);
    }
}