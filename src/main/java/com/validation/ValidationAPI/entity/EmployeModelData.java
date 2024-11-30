package com.validation.ValidationAPI.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Entity
@Table(name = "EMPLOYEE_DATA")
public class EmployeModelData {

    @Id
    //@NotNull(message = "Enter a valid employee id")
    @Column(name = "EMP_ID",unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long empId;

    @NotEmpty(message = "Must Not be Empty and Null ")
    @Positive
    @Column(name = "EMP_PH", nullable = false,unique = true )
    @Pattern(regexp = "[7-9][0-9]{9}$", message = "invalid mobile number.")
    @Size(max = 10, message = "digits should be 10")
    private String empPhoneNumber;

    @NotBlank(message = "EmployeeName Cannot be Empty")
    @Size(min = 2 ,max = 30 , message = "Name size must be min of 2 and max limit 30")
    @Column(name = "EMP_NAME")
    private String empName;

    @Min(value = 18, message = "Age must be greater than or equal to 18")
    @Max(value = 60, message = "Age must be maximum 60 ")
    @Column(name = "EMP_AGE")
    private Integer empAge;

    @Email(message = "Please enter a valid email id ")
    @NotBlank(message = "Message Must not be Null or Empty")
    @Column(name = "EMP_MAIL", nullable = false, unique = true)
    //@Email(regexp = "^(.+)@(.+)$", message = "Invalid email pattern")
    private String empEmailId;

    @Pattern(regexp = ("^[0-9]{6}$") , message = "Employee postal code must be 6 digit number")
    @Column(name = "EMP_POSTAL_CODE")
    private String empPostalCode;

    @Size(min = 10 ,max = 100 ,message = "Address should be in between 10 -100 characters")
    @Column(name = "EMP_ADDRESS")
    private String empAddress;

    @Size(min = 10 ,max = 10 , message = "Provided PAN number must be 10 digits " )
    @Column(name = "EMP_PAN", nullable = false, unique = true)
    private String empPan;
}