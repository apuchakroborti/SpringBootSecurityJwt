package com.apu.example.springsecurityjwt.entity;

import io.swagger.annotations.ApiModelProperty;
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
public class CustomUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "ID of the custom user, it is from the custom user table's id auto increment sequence", name = "id", required = true)
    private Long id;

    @Column(name = "user_id")
    @ApiModelProperty(notes = "USER ID of the custom user, Admin provided ID", name = "userId", required = true)
    private String userId;

    @Column(name = "first_name")
    @ApiModelProperty(notes = "First Name of the custom user", name = "firstName", required = true)
    private String firstName;

    @Column(name = "last_name")
    @ApiModelProperty(notes = "Last Name of the custom user", name = "lastName", required = true)
    private String lastName;

    @ApiModelProperty(notes = "Email of the custom user", name = "email", required = true)
    private String email;

    @OneToOne
    @JoinColumn(name = "user_credential_id")
    private UserCredential userCredential;
}
