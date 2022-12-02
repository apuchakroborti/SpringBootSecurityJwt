package com.apu.example.springsecurityjwt.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name = "custom_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CustomUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String email;

    @OneToOne
    @JoinColumn(name = "user_credential_id")
    private UserCredential userCredential;
}
