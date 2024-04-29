package com.clearsolutions.practicalassignment.users.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.UUID;

@Data
@EqualsAndHashCode
public class User {
    private UUID uuid = UUID.randomUUID();

    @NotBlank(message = "Email address must not be blank")
    @Email(message = "Email address must be well formed")
    private String email;

    @NotBlank(message = "Name must not be blank")
    private String firstName;

    @NotBlank(message = "Surname must not be blank")
    private String lastName;

    @Past(message = "Birth date must be earlier than current date")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate birthDate;
    private String address;
    private String phoneNumber;

}
