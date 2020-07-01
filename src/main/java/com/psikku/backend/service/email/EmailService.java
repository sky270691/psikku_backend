package com.psikku.backend.service.email;

import org.springframework.core.io.InputStreamResource;

public interface EmailService {

    void sendEmail(String to,String subject, String message);
    void sendEmailWithAttachment(String to, String subject, String message, InputStreamResource resource, String filename);

}
