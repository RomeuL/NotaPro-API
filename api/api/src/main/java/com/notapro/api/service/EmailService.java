package com.notapro.api.service;

import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.exceptions.TemplateInputException;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;
    
    @Value("${notapro.mail.sender}")
    private String sender;

    @Async
    public void sendSimpleMessage(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(sender);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            emailSender.send(message);
            log.info("Email simples enviado para: {}", to);
        } catch (Exception e) {
            log.error("Erro ao enviar email simples: {}", e.getMessage());
        }
    }

    @Async
    public void sendWelcomeEmail(String to, String name) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(sender);
            helper.setTo(to);
            helper.setSubject("Bem-vindo ao NotaPro!");
            
            Context context = new Context();
            context.setVariable("name", name);
            String htmlContent = templateEngine.process("welcome-email", context);
            
            helper.setText(htmlContent, true);
            
            emailSender.send(message);
            log.info("Email de boas-vindas enviado para: {}", to);
        } catch (Exception e) {
            log.error("Erro ao enviar email com template: {}", e.getMessage());
            
            if (e instanceof TemplateInputException || 
                (e.getCause() != null && e.getCause() instanceof FileNotFoundException)) {
                log.info("Usando método de fallback para enviar email simples");
                sendWelcomeEmailSimple(to, name);
            }
        }
    }

    @Async
    public void sendWelcomeEmailSimple(String to, String name) {
        String subject = "Bem-vindo ao NotaPro!";
        String text = String.format(
                "Olá %s,\n\n" +
                "Seja bem-vindo ao NotaPro! Estamos muito felizes por você ter se juntado a nós.\n\n" +
                "Com o NotaPro, você poderá gerenciar suas notas e empresas de forma eficiente.\n\n" +
                "Atenciosamente,\n" +
                "Equipe NotaPro", name);
        
        sendSimpleMessage(to, subject, text);
    }
}