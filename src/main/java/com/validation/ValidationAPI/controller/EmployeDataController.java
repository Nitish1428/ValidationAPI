package com.validation.ValidationAPI.controller;


import com.validation.ValidationAPI.entity.EmployeModelData;
import com.validation.ValidationAPI.model.EmployeDataAdressDTO;
import com.validation.ValidationAPI.model.EmployeDataDetailsDTO;
import com.validation.ValidationAPI.service.EmployeDataService;
import com.validation.ValidationAPI.service.jms.MailService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/employee")
@Validated
public class EmployeDataController {

    @Autowired
    private EmployeDataService employeDataService;

    @Autowired
    private MailService mailSender ;

   /* To restrict access to specific endpoints based on user roles,
     you can configure method-level security with role-based authorization.
     This approach leverages annotations like @PreAuthorize and @Secured,
     or you can configure it at the endpoint level in HttpSecurity within your SecurityConfig.
     @Secured or @PreAuthorize from both any one can be used
     For Preauthorizewe need to decalre  for one role @PreAuthorize("hasRole('SUPERUSER')")
     for multiple roles @PreAuthorize("hasAnyRole('ADMIN', 'SUPERUSER')")
    for Secured for one role or multiple roles @Secured({"ROLE_ADMIN","ROLE_SUPERUSER"}) "ROLE_" is mainly Required
     */

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({"ROLE_ADMIN","ROLE_SUPERUSER"}) //MULTIPLE ROLES CAN BE DECLARED
    //@PreAuthorize("hasAnyRole('ADMIN', 'SUPERUSER')") // we can use preauthorize and can be defined
    public ResponseEntity<EmployeModelData> saveEmployeeData(@Valid @RequestBody EmployeModelData employeModelData) {
        return new ResponseEntity<EmployeModelData>(employeDataService.saveEmployee(employeModelData), HttpStatus.CREATED);
    }

    @GetMapping(value = "/get/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_SUPERUSER", "ROLE_USER"})
    public ResponseEntity<Object> getEmployeeById(@PathVariable  @Valid @Min(value = 1,message = "Value should be Greater than 0" )
                                                            Long id) {
        Optional<EmployeModelData> employeeGetId = employeDataService.getEmployeeById(id);
        try {
            if (employeeGetId.isPresent()) {
                return new ResponseEntity<Object>(employeeGetId, HttpStatus.OK);
            }
            return new ResponseEntity<>("Employee Id not found for given id ", HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
    }

    @GetMapping(value = "/employee")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERUSER')")
    public ResponseEntity<Object> getAllEmployees() {
        List<EmployeModelData> listOfEmployees = employeDataService.getAllEmployees();
        if (!listOfEmployees.isEmpty()) {
            return new ResponseEntity<>(listOfEmployees, HttpStatus.OK);
        }

        return new ResponseEntity<Object>("No data found ", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/employee/{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Object> deleteEmployeeById(@PathVariable @Valid @Min(value = 1,message = "Value should be Greater than 0" )
                                                         Long id) {

        Boolean isDeleted = employeDataService.deleteEmployee(id);

        if (isDeleted) {
            //return new ResponseEntity<>("Employee Data Deleted Successfully ", HttpStatus.OK);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("Employee Data Deleted Successfully ");
        }

        return new ResponseEntity<>("No Employee id found to delete ", HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/get/mailid")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Object> getEmployeeByEmail(@RequestParam("email") String email) {
        if (email == null || email.isEmpty()) {
            return new ResponseEntity<>("Email parameter is missing", HttpStatus.BAD_REQUEST);
        }
        EmployeModelData empEmailId = employeDataService.getEmployeeByEmail(email);
        if (empEmailId == null) {
            return new ResponseEntity<>("No data found for empid ", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(empEmailId);
    }

    @GetMapping(value = "/get/name")
    @Secured({"ROLE_ADMIN", "ROLE_SUPERUSER",})
    public ResponseEntity<Object> getEmployeeByName(@RequestParam("name") String name) {
        if (name == null || name.isEmpty()) {
            return new ResponseEntity<>("Name parameter is missing", HttpStatus.BAD_REQUEST);
        }
        Optional<EmployeModelData> employeeByName = employeDataService.getEmployeeByName(name);
        return employeeByName.<ResponseEntity<Object>>map(ResponseEntity::ok).
                orElseGet(() -> new ResponseEntity<>("No data found for Emp Email id ", HttpStatus.NOT_FOUND));
    }


    @GetMapping(value = "/get/postalcode/{code}")
    @Secured({"ROLE_ADMIN", "ROLE_SUPERUSER", "ROLE_USER"})
    public ResponseEntity<Object> getEmployeesByPostalCode(@PathVariable String code) {
        if (code == null || code.isEmpty()) {
            return new ResponseEntity<>("Code parameter is missing", HttpStatus.BAD_REQUEST);
        }
         List<EmployeModelData> employeePostalData = employeDataService.getEmployeesByPostalCode(code);
        if(employeePostalData != null && !employeePostalData.isEmpty()) {
            List<EmployeDataAdressDTO> address = employeePostalData.stream()
                    .map(e -> new EmployeDataAdressDTO(e.getEmpName(), e.getEmpAddress(), e.getEmpEmailId(), e.getEmpPhoneNumber()))
                    .toList();

            return ResponseEntity.status(HttpStatus.OK).body(address);
        }
        return new ResponseEntity<>("No data found for postal Code", HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/get/employeesbyage")
    @Valid
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Object> getEmployeesAgeBetween(@RequestParam("min") @NotNull @Min(value = 18 ,
            message = "Age must be minimum of 18 or greater") Integer min,
            @RequestParam("max") @NotNull @Max(value = 60 , message = "Age must be maximum of 60 or less") Integer max) {
        List<EmployeModelData> employeePostalData = employeDataService.getEmployeesByAgeRange(min, max);
        if(employeePostalData != null && !employeePostalData.isEmpty()) {
            List<EmployeDataDetailsDTO> details = employeePostalData.stream()
                    .map(e -> new EmployeDataDetailsDTO(e.getEmpId(),e.getEmpName(),e.getEmpEmailId(), e.getEmpAge(),e.getEmpPhoneNumber()))
                    .toList();

            return new ResponseEntity<>(details, HttpStatus.OK);

        }
        return new ResponseEntity<>("Details Not Found", HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/get/employeesbynamepostalcode")
    @Valid
    @Secured({"ROLE_ADMIN", "ROLE_SUPERUSER", "ROLE_USER"})
    public ResponseEntity<Object> getEmployeesByNameAndAddress(@RequestParam("name") @NotBlank
         @Size(min = 2, max = 30, message = "Name size must be min of 2 and max limit 30") String name ,
        @RequestParam("postalCode") @NotBlank @Size(min = 6, max = 6, message = "Employee postal code must be 6 digit number")
        @Pattern(regexp = ("^[0-9]{6}$") , message = "Employee postal code must be 6 digit number") String postalCode)
    {
        List<EmployeModelData> employeePostalData = employeDataService.getEmployeesByNameAndPostalCode(name, postalCode);
        if(employeePostalData != null && !employeePostalData.isEmpty()) {
            List<EmployeDataAdressDTO> details = employeePostalData.stream()
                    .map(e -> new EmployeDataAdressDTO(e.getEmpName(), e.getEmpAddress(), e.getEmpEmailId(), e.getEmpPhoneNumber()))
                    .toList();

            return new ResponseEntity<>(details, HttpStatus.OK);
        }
        return new ResponseEntity<>("Details Not Found", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/updateemployeedetails")
    @Valid
    @Secured({"ROLE_ADMIN", "ROLE_SUPERUSER"})
    public ResponseEntity<Object> updateEmployeeDetails( @RequestParam("email") @NotBlank String email,
     @RequestParam("phoneNumber") @NotNull @Size(max = 10) @Pattern(regexp = "[7-9][0-9]{9}$", message = "invalid mobile number.") String phoneNumber,
                                                      @Valid @RequestBody EmployeModelData employeModelData)
    {
        String isUpdated = employeDataService.updateEmployeeDetailInformation(email, phoneNumber, employeModelData);
        return new ResponseEntity<>(isUpdated, HttpStatus.ACCEPTED);
    }

    @PostMapping("/sendMail")
    @Valid
    @Secured({"ROLE_ADMIN", "ROLE_SUPERUSER", "ROLE_USER"})
    public ResponseEntity<Object> sendMail(@RequestParam(value = "emailAddress") @NotNull String emailAddress, @RequestParam(value = "mobile") @NotBlank String mobile )
    {

        employeDataService.employeeDetailsSendMailService(emailAddress, mobile);

        return new ResponseEntity<>("Mail Sent Successfully", HttpStatus.OK);
    }

}
