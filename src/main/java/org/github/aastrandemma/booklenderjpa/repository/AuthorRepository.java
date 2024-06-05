package org.github.aastrandemma.booklenderjpa.repository;

import org.github.aastrandemma.booklenderjpa.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {
    List<Author> findByFirstName(String firstName);
    List<Author> findByLastName(String lastName);

    @Query("SELECT a FROM Author a WHERE lower(concat(a.firstName,' ', a.lastName)) LIKE lower(concat('%', :keyword, '%'))")
    List<Author> findByFirstNameOrLastNameContaining(@Param("keyword") String keyword);

    List<Author> findByWrittenBooks_Id(Integer bookId);

    @Modifying
    @Query("UPDATE Author a SET a.firstName = :firstName, a.lastName = :lastName WHERE a.id = :authorId")
    void updateAuthorById(@Param("firstName") String firstName, @Param("lastName") String lastName, @Param("authorId") Integer authorId);
}