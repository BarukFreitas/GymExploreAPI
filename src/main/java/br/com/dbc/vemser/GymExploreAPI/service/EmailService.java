package br.com.dbc.vemser.GymExploreAPI.service;

import br.com.dbc.vemser.GymExploreAPI.entity.StoreItem;
import br.com.dbc.vemser.GymExploreAPI.entity.UserEntity;
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

    @Value("${app.frontend.url}")
    private String frontendUrl;

    private final String destinatario = "netinho.rec@gmail.com";

    public void sendContactEmailToAdmin(String fromEmail, String subject, String message) throws MessagingException {
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

    public void sendContactConfirmationEmailToUser(String userName, String userEmail) throws MessagingException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        Context context = new Context();
        context.setVariable("userName", userName);

        String htmlBody = templateEngine.process("confirmation-template", context);

        helper.setFrom(remetente);
        helper.setTo(userEmail);
        helper.setSubject("Recebemos a sua mensagem - Gym Explore");
        helper.setText(htmlBody, true);

        emailSender.send(mimeMessage);
    }

    public void sendPurchaseConfirmationEmail(UserEntity user, StoreItem item) throws MessagingException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        Context context = new Context();
        context.setVariable("userName", user.getUsername());
        context.setVariable("itemName", item.getName());
        context.setVariable("pointsCost", item.getPointsCost());
        context.setVariable("remainingPoints", user.getPoints());


        String htmlBody = templateEngine.process("purchase-confirmation-template", context);

        helper.setFrom(remetente);
        helper.setTo(user.getEmail());
        helper.setSubject("Sua troca de pontos no Gym Explore!");
        helper.setText(htmlBody, true);

        emailSender.send(mimeMessage);
    }
    public void sendWelcomeEmail(UserEntity user) throws MessagingException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        Context context = new Context();
        context.setVariable("username", user.getUsername());


        String htmlBody = templateEngine.process("welcome-template", context);

        helper.setFrom(remetente);
        helper.setTo(user.getEmail());
        helper.setSubject("Bem-vindo(a) ao Gym Explore!");
        helper.setText(htmlBody, true);

        emailSender.send(mimeMessage);
    }

    public void sendPasswordResetEmail(UserEntity user, String token) throws MessagingException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        Context context = new Context();
        context.setVariable("username", user.getUsername());

        String resetLink = frontendUrl + "/reset-password?token=" + token;
        context.setVariable("resetLink", resetLink);

        String htmlBody = templateEngine.process("password-reset-template", context);

        helper.setFrom(remetente);
        helper.setTo(user.getEmail());
        helper.setSubject("Redefinição de Senha - Gym Explore");
        helper.setText(htmlBody, true);

        emailSender.send(mimeMessage);
    }
}