package org.github.aastrandemma.booklenderjpa;

import org.github.aastrandemma.booklenderjpa.repository.AppUserRepository;
import org.github.aastrandemma.booklenderjpa.repository.DetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.github.aastrandemma.booklenderjpa.entity.AppUser;
import org.github.aastrandemma.booklenderjpa.entity.Details;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class MyCommandLineRunner implements CommandLineRunner {
    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    DetailsRepository detailsRepository;

    @Override
    public void run(String... args) throws Exception {
        Details testDetails = detailsRepository.save(new Details("jane@doe.com", "Jane Doe", LocalDate.of(2000, 1, 1)));
        appUserRepository.save(new AppUser("test_username", "test_password", testDetails));
    }
}