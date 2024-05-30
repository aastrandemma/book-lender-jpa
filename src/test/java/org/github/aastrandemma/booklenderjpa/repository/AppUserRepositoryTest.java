package org.github.aastrandemma.booklenderjpa.repository;

import org.github.aastrandemma.booklenderjpa.entity.AppUser;
import org.github.aastrandemma.booklenderjpa.entity.Details;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;

@DataJpaTest
public class AppUserRepositoryTest {
    @Autowired
    DetailsRepository detailsRepository;
    @Autowired
    AppUserRepository appUserRepository;

    private Details existingDetails;
    private AppUser existingAppUser;

    @BeforeEach
    void setUp() {
        existingDetails = detailsRepository.save(new Details("jane@doe.com", "Jane Doe", LocalDate.of(2000, 1, 1)));
        existingAppUser = appUserRepository.save(new AppUser("test_username", "test_password", existingDetails));
    }

    @Test
    @Transactional
    public void testSaveAndFindAppUserWithoutDetailsById() {
        AppUser appUser = new AppUser("janedoe", "test_password");
        AppUser savedAppUser = appUserRepository.save(appUser);

        assertNotNull(savedAppUser);
        assertNotNull(savedAppUser.getId());

        Optional<AppUser> foundAppUser = appUserRepository.findById(savedAppUser.getId());
        assertTrue(foundAppUser.isPresent());
    }

    @Test
    @Transactional
    public void testSaveAndFindAppUserWithDetailsById() {
        Details details = new Details("jane@doe.com", "Jane Doe", LocalDate.of(1990, 1, 1));
        AppUser appUser = new AppUser("janedoe", "test_password", details);
        AppUser savedAppUser = appUserRepository.save(appUser);

        assertNotNull(savedAppUser);
        assertNotNull(savedAppUser.getId());

        Optional<AppUser> foundAppUser = appUserRepository.findById(savedAppUser.getId());
        assertTrue(foundAppUser.isPresent());
    }

    @Test
    @Transactional
    public void testFindByUsername() {
        String username = existingAppUser.getUsername();
        Optional<AppUser> expected = Optional.of(existingAppUser);
        Optional<AppUser> actual = appUserRepository.findByUsername(username);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    public void testFindByRegDateBetweenExpectEmptyList() {
        LocalDate start = LocalDate.of(2022, 1, 1);
        LocalDate end = LocalDate.of(2023, 1, 1);
        List<AppUser> expected = new ArrayList<>();
        List<AppUser> actual = appUserRepository.findByRegDateBetween(start, end);

        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    public void testFindAllAndFindByRegDateBetween() {
        LocalDate start = LocalDate.now().minusDays(2);
        LocalDate end = LocalDate.now().plusDays(2);
        List<AppUser> expected = appUserRepository.findAll();
        List<AppUser> actual = appUserRepository.findByRegDateBetween(start, end);

        assertNotNull(expected);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    public void testFindByUserDetails_Id() {
        Integer detailsId = existingDetails.getId();
        Optional<AppUser> expected = Optional.of(existingAppUser);
        Optional<AppUser> actual = appUserRepository.findByUserDetails_Id(detailsId);

        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    public void testFindByUserDetails_EmailIgnoreCase() {
        String email = existingAppUser.getUserDetails().getEmail();
        Optional<AppUser> expected = Optional.of(existingAppUser);
        Optional<AppUser> actual = appUserRepository.findByUserDetails_EmailIgnoreCase(email);

        assertEquals(expected, actual);
    }
}