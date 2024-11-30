package com.validation.ValidationAPI.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@JsonPropertyOrder({ "id", "name", "email", "phone", "address" })
@Valid
@Validated
public class EmployeeMailRegistrationSendDTO {


    @JsonProperty("EMP ID")
    @NotNull
    private Long id;

    @JsonProperty("EMP NAME")
    @NotBlank
    private String name;

    @JsonProperty("EMP MAIL")
    @NotBlank
    @NotNull
    private String email;

    @JsonProperty("EMP CONTACT")
    private String phone;

    @JsonProperty("EMP ADDRESS")
    private String address;

    public EmployeeMailRegistrationSendDTO() {
        //default Constructor
    }

    public EmployeeMailRegistrationSendDTO(Long id, String name, String email, String phone, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }


}
