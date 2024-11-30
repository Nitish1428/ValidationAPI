package com.validation.ValidationAPI.service.jms;

import com.validation.ValidationAPI.entity.EmployeModelData;

public interface MailService
{
    void sendSimpleMail(String from, String to, String subject, String body);
    void sendMailWithAttachment(String from, String to, String subject, String body, String attachmentPath);
}

