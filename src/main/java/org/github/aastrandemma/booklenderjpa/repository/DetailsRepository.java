package org.github.aastrandemma.booklenderjpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.github.aastrandemma.booklenderjpa.entity.Details;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface DetailsRepository extends JpaRepository<Details, Integer> {
    Optional<Details> findByEmail(String email);
    List<Details> findByNameContaining(String nameContains);
    List<Details> findByNameIgnoreCase(String name);
}