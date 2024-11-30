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
@JsonPropertyOrder({ "empName", "empAddress", "empEmailId", "empPhoneNumber" })
public class EmployeDataAdressDTO {

    @JsonProperty("EMP NAME")
    private String empName;

    @JsonProperty("EMP ADDRESS")
    private String empAddress;

    @JsonProperty("EMP EMAIL")
    private String empEmailId;

    @JsonProperty("EMP PHONE NUMBER")
    private String empPhoneNumber;

    public EmployeDataAdressDTO() {
        // Default constructor
    }

    public EmployeDataAdressDTO(String empName, String empAddress, String empEmailId, String empPhoneNumber) {
        this.empName = empName;
        this.empAddress = empAddress;
        this.empEmailId = empEmailId;
        this.empPhoneNumber = empPhoneNumber;
    }
}
