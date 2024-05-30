package org.github.aastrandemma.booklenderjpa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 50)
    @Setter private String username;

    @Getter(AccessLevel.NONE)
    @Column(nullable = false)
    @Setter private String password;

    @Column
    private LocalDate regDate;

    @OneToOne
    @JoinColumn(name = "details_id")
    @Setter private Details userDetails;

    public AppUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public AppUser(String username, String password, Details userDetails) {
        this.username = username;
        this.password = password;
        this.regDate = LocalDate.now();
        this.userDetails = userDetails;
    }
}