package com.validation.ValidationAPI.service.jms;

import jakarta.mail.*;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    private static final Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);


    @Autowired
    public MailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    @Override
    public void sendSimpleMail(String from, String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false);

            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, false);  // 'true' for HTML content

            mailSender.send(message);
            LOGGER.info("Mail Sent");
        } catch (MessagingException e) {
            e.printStackTrace();
            LOGGER.error("Error while sending mail");
        }
    }

    @Override
    public void sendMailWithAttachment(String from, String to, String subject, String body, String attachmentPath) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);  // 'true' for multipart messages

            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);  // 'true' for HTML content

            // Add attachment
            FileSystemResource file = new FileSystemResource(new File(attachmentPath));
            helper.addAttachment(file.getFilename(), file);

            mailSender.send(message);
            LOGGER.info("Mail sent successfully with attachment");
        } catch (MessagingException e) {
            e.printStackTrace();
            LOGGER.error("Error while sending mail with attachment");
        }
    }


}

