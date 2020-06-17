package com.psikku.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@RestController
@RequestMapping("/api/mail-sender")
public class SendEmailController {

    @Autowired
    public JavaMailSender javaMailSender;
    @Autowired
    private ResourceLoader resourceLoader;

    @GetMapping("/with-attachment")
    public String sendTestEmailWithoutAttachment() {
        String to= "langi.risky@gmail.com";
        String subject="test email";
        String text = "hello, sending this message from spring boot psikku";
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(text);
        javaMailSender.send(mailMessage);

        return "success";
    }

    @GetMapping
    public String sendTestEmailWithAttachment() throws MessagingException{
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);

        mimeMessageHelper.setTo("langi.risky@gmail.com");
        mimeMessageHelper.setSubject("test email with attachment");
        mimeMessageHelper.setText("sending this message from spring boot with attachment");


//        FileSystemResource resource = new FileSystemResource(new File("backend.log"));

        Resource resource1 = resourceLoader.getResource("classpath:backend.log");
        mimeMessageHelper.addAttachment("log",resource1);
        javaMailSender.send(mimeMessage);
        return "success sending email";
    }

}
