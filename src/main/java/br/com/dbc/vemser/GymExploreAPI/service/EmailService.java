package br.com.dbc.vemser.GymExploreAPI.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String remetente;

    private final String destinatario = "netinho.rec@gmail.com";


    public void sendContactEmail(String fromEmail, String subject, String message) throws MessagingException {

        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);


        Context context = new Context();
        context.setVariable("fromEmail", fromEmail);
        context.setVariable("subject", subject);
        context.setVariable("message", message);


        String htmlBody = templateEngine.process("contact-template", context);


        helper.setFrom(remetente);
        helper.setTo(destinatario);
        helper.setSubject("Nova Mensagem de Contato: " + subject);
        helper.setText(htmlBody, true);

        emailSender.send(mimeMessage);
    }
}