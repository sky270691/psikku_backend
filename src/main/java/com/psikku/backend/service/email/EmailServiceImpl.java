package com.psikku.backend.service.email;

import com.psikku.backend.exception.CustomEmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    JavaMailSender mailSender;

    @Override
    public void sendEmail(String to, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        try {
            mailSender.send(mailMessage);
        } catch (MailException e) {
            e.printStackTrace();
            throw new CustomEmailException("sending email error to: "+to);
        }
    }

    @Override
    public void sendEmailWithAttachment(String to, String subject, String message, InputStreamResource resource, String filename) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);

            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(message);

            mimeMessageHelper.addAttachment(filename,resource);
            mailSender.send(mimeMessage);
        } catch (MessagingException | MailException e) {
            e.printStackTrace();
            throw new CustomEmailException("Sending email with attachment error to: "+to);
        }

    }
}
