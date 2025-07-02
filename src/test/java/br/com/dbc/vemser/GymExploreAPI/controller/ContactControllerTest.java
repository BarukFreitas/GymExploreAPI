package br.com.dbc.vemser.GymExploreAPI.controller;

import br.com.dbc.vemser.GymExploreAPI.dto.ContactDTO;
import br.com.dbc.vemser.GymExploreAPI.service.CustomUserDetailsService;
import br.com.dbc.vemser.GymExploreAPI.service.EmailService;
import br.com.dbc.vemser.GymExploreAPI.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import javax.mail.MessagingException;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ContactController.class)
@DisplayName("Testes do ContactController")
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmailService emailService;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @DisplayName("Deve enviar mensagem de contato com sucesso e retornar status 200")
    @WithMockUser
    void deveEnviarMensagemDeContatoComSucesso() throws Exception {

        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setName("Utilizador Teste");
        contactDTO.setEmail("teste@email.com");
        contactDTO.setSubject("Assunto de Teste");
        contactDTO.setMessage("Esta é uma mensagem de teste.");

        doNothing().when(emailService).sendContactEmailToAdmin(anyString(), anyString(), anyString());
        doNothing().when(emailService).sendContactConfirmationEmailToUser(anyString(), anyString());

        mockMvc.perform(post("/contact")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contactDTO)))
                .andExpect(status().isOk());

        verify(emailService, times(1)).sendContactEmailToAdmin(anyString(), anyString(), anyString());
        verify(emailService, times(1)).sendContactConfirmationEmailToUser(anyString(), anyString());
    }

    @Test
    @DisplayName("Deve retornar status 400 ao tentar enviar com e-mail inválido")
    @WithMockUser
    void deveRetornarBadRequestParaEmailInvalido() throws Exception {

        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setName("Utilizador Teste");
        contactDTO.setEmail("email-invalido");
        contactDTO.setSubject("Assunto");
        contactDTO.setMessage("Mensagem");


        mockMvc.perform(post("/contact")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contactDTO)))
                .andExpect(status().isBadRequest()); // Esperamos um status 400
    }

    @Test
    @DisplayName("Deve retornar status 500 quando o serviço de e-mail falha")
    @WithMockUser
    void deveRetornarInternalServerErrorQuandoEmailServiceFalha() throws Exception {

        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setName("Utilizador Teste");
        contactDTO.setEmail("teste@email.com");
        contactDTO.setSubject("Assunto");
        contactDTO.setMessage("Mensagem");


        doThrow(new MessagingException("Erro de SMTP"))
                .when(emailService).sendContactEmailToAdmin(anyString(), anyString(), anyString());


        mockMvc.perform(post("/contact")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contactDTO)))
                .andExpect(status().isInternalServerError());
    }
}