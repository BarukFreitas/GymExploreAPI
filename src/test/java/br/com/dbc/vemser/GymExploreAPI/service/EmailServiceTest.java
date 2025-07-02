package br.com.dbc.vemser.GymExploreAPI.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static org.mockito.Mockito.*;

@SpringBootTest
@DisplayName("Testes do EmailService")
class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @MockBean
    private JavaMailSender emailSender;

    @Test
    @DisplayName("Deve processar o template e tentar enviar e-mail para o administrador")
    void deveProcessarEEnviarEmailParaAdmin() throws MessagingException {

        MimeMessage mimeMessageMock = mock(MimeMessage.class);
        when(emailSender.createMimeMessage()).thenReturn(mimeMessageMock);
        emailService.sendContactEmailToAdmin("remetente@teste.com", "Assunto", "Mensagem");
        verify(emailSender, times(1)).send(mimeMessageMock);
    }

    @Test
    @DisplayName("Deve processar o template e tentar enviar e-mail de confirmação para o utilizador")
    void deveProcessarEEnviarEmailDeConfirmacaoParaUtilizador() throws MessagingException {
        MimeMessage mimeMessageMock = mock(MimeMessage.class);
        when(emailSender.createMimeMessage()).thenReturn(mimeMessageMock);

        emailService.sendContactConfirmationEmailToUser("Utilizador Teste", "utilizador@teste.com");

        verify(emailSender, times(1)).send(mimeMessageMock);
    }
}