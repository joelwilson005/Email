package com.joel.email.controller;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    private static final String TEMPLATE_NAME = "registration";
    private final Environment environment;
    private final JavaMailSender mailSender;
    private final TemplateEngine htmlTemplateEngine;

    @Autowired
    public UserController(Environment environment, JavaMailSender mailSender, TemplateEngine htmlTemplateEngine) {
        this.environment = environment;
        this.mailSender = mailSender;
        this.htmlTemplateEngine = htmlTemplateEngine;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody MessageRequest messageRequest)
            throws MessagingException, UnsupportedEncodingException {

        String mailFrom = environment.getProperty("spring.mail.properties.mail.smtp.from");
        String mailFromName = environment.getProperty("mail.from.name", "Identity");
        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper email;

        email = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        email.setTo(messageRequest.recipient());
        email.setSubject("TestEmail");
        email.setFrom(new InternetAddress(mailFrom, mailFromName));

        final Context ctx = new Context(LocaleContextHolder.getLocale());
        ctx.setVariable("message", messageRequest.message());

        final String htmlContent = this.htmlTemplateEngine.process(TEMPLATE_NAME, ctx);
        email.setText(htmlContent, true);
        mailSender.send(mimeMessage);

        Map<String, String> body = new HashMap<>();
        body.put("message", "Message successfully sent");

        return new ResponseEntity<>(body, HttpStatus.OK);
    }
}