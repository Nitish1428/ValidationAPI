package com.validation.ValidationAPI.service;

import com.validation.ValidationAPI.entity.EmployeModelData;
import com.validation.ValidationAPI.exception.ValidationException;
import com.validation.ValidationAPI.repository.EmployeDataRepo;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeDataServiceImpl implements EmployeDataService {

    public static final Logger LOGGER = LoggerFactory.getLogger(EmployeDataServiceImpl.class);

    @Autowired
    private EmployeDataRepo employeDataRepo;

    @Autowired
    private EmployeRegistrationMail employeRegistrationMail ;

    @Override
    public EmployeModelData saveEmployee(EmployeModelData employee) {
        try {
            EmployeModelData employeModelDataSave = employeDataRepo.save(employee) ;
            employeRegistrationMail.organisationEmployeeDetailsRegistrationMail(employeModelDataSave);
            return employeModelDataSave;
        } catch (Exception exception) {
            throw new ValidationException(exception.getMessage());
        }
    }

    @Override
    public Optional<EmployeModelData> getEmployeeById(Long empId) {
        return employeDataRepo.findById(empId);
    }

    @Override
    public List<EmployeModelData> getAllEmployees() {
        try {
            List<EmployeModelData> allEmployeesData = employeDataRepo.findAll();
            if (!allEmployeesData.isEmpty()) {
                return allEmployeesData;
            }
            throw new ValidationException("Employees Not Found ");
        } catch (Exception exception) {
            throw new ValidationException(exception.getMessage());
        }
    }

    @Override
    public Boolean deleteEmployee(Long empId) {

        Optional<EmployeModelData> response = employeDataRepo.findById(empId);
        try {
            if (response.isPresent()) {
                String mailAddress = response.get().getEmpEmailId();
                String name = response.get().getEmpName() ;
                employeDataRepo.deleteById(empId);
                employeRegistrationMail.employeeDetailsDeleteInOrganisationByEmpId(name, mailAddress);
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } catch (Exception exception) {
            throw new ValidationException(exception.getMessage());
        }
    }

    @Override
    public EmployeModelData getEmployeeByEmail(String email) {
        try {
            Optional<EmployeModelData> employeeByEmailId = employeDataRepo.findByEmpEmailIdIn(Collections.singletonList(email));

            return employeeByEmailId.orElse(null);
        } catch (Exception exception) {
            throw new ValidationException(exception.getMessage());
        }
    }

    @Override
    public Optional<EmployeModelData> getEmployeeByName(String name) {
        try {
            Optional<EmployeModelData> employeeByName = employeDataRepo.findByName(name);
            return employeeByName.filter(employeModelData -> employeModelData.getEmpName().contains(name));
        } catch (Exception ex) {
            throw new ValidationException(ex.getMessage());
        }
    }

    @Override
    public List<EmployeModelData> getEmployeesByPostalCode(String postalCode) {
        List<EmployeModelData> responsePostalCode;
        try {
            responsePostalCode = employeDataRepo.findByPostalCode(postalCode);
            return responsePostalCode;
        } catch (Exception exception) {
            throw new ValidationException(exception.getMessage());
        }
    }

    @Override
    public List<EmployeModelData> getEmployeesByAgeRange(int minAge, int maxAge) {

        List<EmployeModelData> listOfEmployeeDataForAgeRange = employeDataRepo.findByEmpAgeBetween(minAge, maxAge);

        if (listOfEmployeeDataForAgeRange.isEmpty()) {
            throw new ValidationException("Employees Not found with that Age Range");
        }
        return listOfEmployeeDataForAgeRange;
    }

    @Override
    public List<EmployeModelData> getEmployeesByNameAndPostalCode(String name, String postalCode) {

        if (name == null || postalCode == null) {
            throw new IllegalArgumentException("Name and address must not be null");
        }
        List<EmployeModelData> detailsOfNameAndAddressOfEmployee = employeDataRepo.findByNameAndAddress(name, postalCode);

        if (detailsOfNameAndAddressOfEmployee == null || detailsOfNameAndAddressOfEmployee.isEmpty()) {
            return List.of(); // return an empty list if no results are found or the list is null
        }

        return detailsOfNameAndAddressOfEmployee;

    }

    @Override
    public String updateEmployeeDetailInformation(String email, String phoneNumber, EmployeModelData employeModelData) {
        try {
            // Retrieve the employee data based on name, email, and phone number
            EmployeModelData employeeData = employeDataRepo.findByEmpEmailIdAndEmpPhoneNumber(email, phoneNumber);

            // Check if employee data exists
            if (employeeData == null) {
                throw new ValidationException("No Records found for given details to Update");
            }

            // Update the fields of the retrieved employee data
            employeeData.setEmpName(employeModelData.getEmpName());
            employeeData.setEmpAddress(employeModelData.getEmpAddress());
            employeeData.setEmpPan(employeModelData.getEmpPan());
            employeeData.setEmpAge(employeModelData.getEmpAge());
            employeeData.setEmpPhoneNumber(employeModelData.getEmpPhoneNumber());
            employeeData.setEmpPostalCode(employeModelData.getEmpPostalCode());
            employeeData.setEmpEmailId(employeModelData.getEmpEmailId());

            // Save the updated employee data to the repository
            employeDataRepo.save(employeeData);

            //send mail to employee after update of details
            employeRegistrationMail.employeeDetailsUpdateInOrganisationByEmpId(employeModelData);

            return "Employee details updated successfully";

        } catch (ValidationException e) {
            // Handle custom validation exceptions
            return "Error: " + e.getMessage();
        } catch (Exception e) {
            // Handle any other exceptions
            return "An error occurred while updating employee details.";
        }
    }

    @Override
    public void employeeDetailsSendMailService(String emailAddress, String mobile)  {
       EmployeModelData employeeModelDataSend = employeDataRepo.findByEmpEmailIdAndEmpPhoneNumber(emailAddress, mobile);
       try{
           if(employeeModelDataSend == null){
               throw new ValidationException("No Employee Found with the Provided details");
           }
           employeRegistrationMail.employeeDetailsSendMail(employeeModelDataSend);

       }catch (Exception exception)
       {
           throw new RuntimeException(exception.getMessage());
       }
    }


    @Scheduled (fixedRate = 5000, initialDelay = 1000)
    public void customSchedularTasks()
    {
        LOGGER.info("Schedular Started");
       int modelDataRepoCount = employeDataRepo.findAll().size();
       LOGGER.info("Schedular Finished Count of Employees : " + modelDataRepoCount );
    }


    @SchedulerLock(name = "employeeCount", lockAtLeastFor = "PT10S")
    @Scheduled(cron = "0/45 * * * * *")
    public void senderMail()
    {
      List<EmployeModelData> employeeDetails  = employeDataRepo.findAll() ;
      for(EmployeModelData employeeDetail: employeeDetails)
      {
          employeRegistrationMail.employeeDetailsSendMail(employeeDetail);

      }
    }

}
