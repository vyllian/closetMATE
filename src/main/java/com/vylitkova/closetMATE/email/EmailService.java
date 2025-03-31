package com.vylitkova.closetMATE.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class EmailService implements EmailSender{

    @Autowired
    private final JavaMailSender mailSender;

    @Override
    @Async
    @Transactional
    public void send(String to, String emailContent) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");
            mimeMessageHelper.setText(emailContent,true);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject("Email confirmation");
            mimeMessageHelper.setFrom("no-reply@closetmate.com");
            mailSender.send(mimeMessage);
            log.info("Sending email to " + to);
        }catch (MessagingException e){
            log.error("Error sending email", e);
            throw new IllegalStateException("Failed to send email", e);
        }
    }
}
