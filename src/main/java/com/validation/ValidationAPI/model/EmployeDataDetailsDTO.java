package com.validation.ValidationAPI.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@JsonPropertyOrder({ "id", "name", "email", "age", "phone" })
public class EmployeDataDetailsDTO {

    @JsonProperty("EMP ID")
    private Long id;

    @JsonProperty("EMP NAME")
    private String name;

    @JsonProperty("EMP MAIL")
    private String email;

    @JsonProperty("EMP AGE")
    private Integer age;

    @JsonProperty("EMP CONTACT")
    private String phone;

    public EmployeDataDetailsDTO() {
        // Default constructor
    }

    public EmployeDataDetailsDTO(Long id, String name, String email, Integer age, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.phone = phone;
    }
}
