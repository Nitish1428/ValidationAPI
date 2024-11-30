package com.validation.ValidationAPI.service;

import com.validation.ValidationAPI.entity.EmployeModelData;

import java.util.List;
import java.util.Optional;

public interface EmployeDataService {

    EmployeModelData saveEmployee(EmployeModelData employee);

    Optional<EmployeModelData> getEmployeeById(Long empId);

    List<EmployeModelData> getAllEmployees();

    Boolean deleteEmployee(Long empId);

    EmployeModelData getEmployeeByEmail(String email);

    Optional<EmployeModelData> getEmployeeByName(String name);

    List<EmployeModelData> getEmployeesByPostalCode(String postalCode);

    List<EmployeModelData> getEmployeesByAgeRange(int minAge, int maxAge);

    List<EmployeModelData> getEmployeesByNameAndPostalCode(String name, String postalCode);

    String updateEmployeeDetailInformation(String email, String phoneNumber, EmployeModelData employeModelData);

    void employeeDetailsSendMailService(String emailAddress, String mobile);
}
