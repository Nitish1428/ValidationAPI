package com.validation.ValidationAPI.service;

import com.validation.ValidationAPI.entity.EmployeModelData;

public interface EmployeRegistrationMail {
    void organisationEmployeeDetailsRegistrationMail(EmployeModelData employeModelDataSave);

    void employeeDetailsUpdateInOrganisationByEmpId(EmployeModelData employeModelData);

    void employeeDetailsDeleteInOrganisationByEmpId(String name, String mail);

    void employeeDetailsSendMail(EmployeModelData employeModelData);
}
