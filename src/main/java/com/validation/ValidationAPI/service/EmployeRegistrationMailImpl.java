package com.validation.ValidationAPI.service;

import com.validation.ValidationAPI.entity.EmployeModelData;
import com.validation.ValidationAPI.model.EmployeeMailRegistrationSendDTO;
import com.validation.ValidationAPI.service.jms.MailServiceImpl;
import com.validation.ValidationAPI.util.AppConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmployeRegistrationMailImpl implements EmployeRegistrationMail{

    @Autowired
    private MailServiceImpl mailService ;

    @Autowired
    private AppConstant appConstant;

    @Value("${mail.from}")
    private String fromAddress ;

    private static final Logger logger = LoggerFactory.getLogger(EmployeRegistrationMailImpl.class);


    @Override
    public void organisationEmployeeDetailsRegistrationMail(EmployeModelData employeModelDataSave) {

        EmployeeMailRegistrationSendDTO employeeMailRegistrationSendDTO = new EmployeeMailRegistrationSendDTO(employeModelDataSave.getEmpId(),
                employeModelDataSave.getEmpName(), employeModelDataSave.getEmpEmailId(), employeModelDataSave.getEmpPhoneNumber(),
                employeModelDataSave.getEmpAddress());
         String subject = appConstant.mailSubjectRegistered ;
         String body = createEmailContent(employeeMailRegistrationSendDTO);

        mailService.sendSimpleMail(fromAddress, employeModelDataSave.getEmpEmailId(), subject, body);

        logger.info("Registration email sent");

    }

    @Override
    public void employeeDetailsUpdateInOrganisationByEmpId(EmployeModelData employeModelData) {

        EmployeeMailRegistrationSendDTO employeeMailRegistrationSendDTO = new EmployeeMailRegistrationSendDTO(employeModelData.getEmpId(),
                employeModelData.getEmpName(), employeModelData.getEmpEmailId(), employeModelData.getEmpPhoneNumber(),
                employeModelData.getEmpAddress());
        String subjectUpdate = appConstant.mailSubjectUpdateDetails ;
        String bodyUpdate = createEmailContent(employeeMailRegistrationSendDTO);

        mailService.sendSimpleMail(fromAddress, employeModelData.getEmpEmailId(), subjectUpdate, bodyUpdate);

        logger.info("update email sent ");
    }

    @Override
    public void employeeDetailsDeleteInOrganisationByEmpId(String name, String mail) {

        String subjectUpdate = appConstant.mailSubjectDelete;
        String bodyUpdate = deletionEmailContent(name);
        mailService.sendSimpleMail(fromAddress, mail, subjectUpdate, bodyUpdate);
        logger.info("deleted email sent ");
    }

    @Override
    public void employeeDetailsSendMail(EmployeModelData employeModelData) {
        organisationEmployeeDetailsRegistrationMail(employeModelData);
    }

    private String createEmailContent(EmployeeMailRegistrationSendDTO employeeMailRegistrationSendDTO) {
        StringBuilder content = new StringBuilder();
        content.append("Dear ").append(employeeMailRegistrationSendDTO.getName()).append(",\n\n")
                .append("Your employee details are as follows:\n\n")
                .append("Employee ID: ").append(employeeMailRegistrationSendDTO.getId()).append("\n\n")
                .append("Employee Name: ").append(employeeMailRegistrationSendDTO.getName()).append("\n\n")
                .append("Employee EmailId: ").append(employeeMailRegistrationSendDTO.getEmail()).append("\n\n")
                .append("Employee MobileNumber: ").append(employeeMailRegistrationSendDTO.getPhone()).append("\n\n")
                .append("Employee Address: ").append(employeeMailRegistrationSendDTO.getAddress()).append("\n\n\n")
                .append("The above details are registered as per your onboarding process, if need any corrections. Please reach to helpdesk number : 1800 4509 2367 \n\n\n")
                .append("Regards,\nTES AUSTRIA");

        return content.toString();
    }

    private String deletionEmailContent(String name) {
        StringBuilder content = new StringBuilder();
        content.append("Dear ").append(name).append(",\n\n")
                .append("Your employee details are Deleted Successfully..\n\n")
                .append("Hope you have a good future ahead \n\n\n")
                .append("Regards,\nTES AUSTRIA");

        return content.toString();
    }

}
