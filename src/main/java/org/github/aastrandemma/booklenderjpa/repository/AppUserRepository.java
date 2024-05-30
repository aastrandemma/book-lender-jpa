package org.github.aastrandemma.booklenderjpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.github.aastrandemma.booklenderjpa.entity.AppUser;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Integer> {
    Optional<AppUser> findByUsername(String username);
    List<AppUser> findByRegDateBetween(LocalDate start, LocalDate end);
    Optional<AppUser> findByUserDetails_Id(Integer detailsId);
    Optional<AppUser> findByUserDetails_EmailIgnoreCase(String email);
}