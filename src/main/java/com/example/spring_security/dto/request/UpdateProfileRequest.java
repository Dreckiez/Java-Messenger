package com.example.spring_security.dto.request;

import com.example.spring_security.entities.Enum.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateProfileRequest {
    @Pattern(
            regexp = "^[a-zA-ZÀ-ỹà-ỹ\\s]{1,20}$",
            message = "First name must contain only letters (with optional accents) and spaces, and be 1-30 characters long"
    )
    private String firstName;
    @Pattern(
            regexp = "^[a-zA-ZÀ-ỹà-ỹ\\s]{1,20}$",
            message = "Last name must contain only letters (with optional accents) and spaces, and be 1-30 characters long"
    )
    private String lastName;


    private Gender gender;

    @Size(max = 50, message = "City name must be at most 50 characters")
    private String city;

    @Size(max = 50, message = "District name must be at most 50 characters")
    private String district;

    @Size(max = 100, message = "Street name must be at most 50 characters")
    private String street;

    @Size(max = 50, message = "Country name must be at most 50 characters")
    private String country;

    @Past(message = "Birthday must be a past date")
    private LocalDate birthday;

    @Size(max = 255, message = "Avatar URL must be at most 255 characters")
    private String avatarUrl;
}
