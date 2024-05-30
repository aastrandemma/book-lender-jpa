package org.github.aastrandemma.booklenderjpa.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.github.aastrandemma.booklenderjpa.entity.AppUser;
import org.github.aastrandemma.booklenderjpa.entity.Details;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;

@DataJpaTest
public class DetailsRepositoryTest {
	@Autowired
	private DetailsRepository detailsRepository;
    @Autowired
    private AppUserRepository appUserRepository;

    private Details existingDetails;
    private AppUser existingAppUser;

    @BeforeEach
    void setUp() {
        existingDetails = detailsRepository.save(new Details("jane@doe.com", "Jane Doe", LocalDate.of(2000, 1, 1)));
        existingAppUser = appUserRepository.save(new AppUser("test_username", "test_password", existingDetails));
    }

	@Test
    @Transactional
	public void testFindByEmail() {
		String email = existingDetails.getEmail();
		Optional<Details> expected = Optional.of(existingDetails);
		Optional<Details> actual = detailsRepository.findByEmail(email);

		assertEquals(expected, actual);
	}

    @Test
    @Transactional
    public void testFindByEmailThroughAppUser() {
        String email = existingAppUser.getUserDetails().getEmail();
        Optional<Details> expected = Optional.of(existingDetails);
        Optional<Details> actual = detailsRepository.findByEmail(email);

        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    public void testFindByEmailExpectingEmpty() {
        String email = "not@existing.com";
        Optional<Details> expected = Optional.empty();
        Optional<Details> actual = detailsRepository.findByEmail(email);

        assertEquals(expected, actual);
    }

	@Test
    @Transactional
	public void testFindByNameContaining() {
		String nameContains = "Doe";
		List<Details> expected = detailsRepository.findAll();
		List<Details> actual = detailsRepository.findByNameContaining(nameContains);

		assertEquals(expected, actual);
	}

	@Test
    @Transactional
	public void testFindByNameIgnoreCase() {
		String nameIgnoreCase = "jaNe dOe";
        List<Details> expected = detailsRepository.findAll();
		List<Details> actual = detailsRepository.findByNameIgnoreCase(nameIgnoreCase);

		assertEquals(expected, actual);
	}

    @Test
    @Transactional
    public void testFindByNameIgnoreCaseExpectEmptyList() {
        String nameNotExist = "This name string doesn't exist";
        List<Details> expected = new ArrayList<>();
        List<Details> actual = detailsRepository.findByNameIgnoreCase(nameNotExist);

        assertEquals(expected, actual);
    }
}