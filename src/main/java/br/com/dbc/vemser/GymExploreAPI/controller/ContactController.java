package br.com.dbc.vemser.GymExploreAPI.controller;

import br.com.dbc.vemser.GymExploreAPI.dto.ContactDTO;
import br.com.dbc.vemser.GymExploreAPI.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.validation.Valid;

@RestController
@RequestMapping("/contact")
@RequiredArgsConstructor
public class ContactController {

    private final EmailService emailService;

    @PostMapping
    public ResponseEntity<Void> sendContactMessage(@RequestBody @Valid ContactDTO contactDTO) {
        try {
            emailService.sendContactEmailToAdmin(
                    contactDTO.getEmail(),
                    contactDTO.getSubject(),
                    contactDTO.getMessage()
            );

            emailService.sendContactConfirmationEmailToUser(
                    contactDTO.getName(),
                    contactDTO.getEmail()
            );

            return ResponseEntity.ok().build();
        } catch (MessagingException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}