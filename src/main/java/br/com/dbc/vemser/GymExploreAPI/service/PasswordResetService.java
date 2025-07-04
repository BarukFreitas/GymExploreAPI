package br.com.dbc.vemser.GymExploreAPI.service;

import br.com.dbc.vemser.GymExploreAPI.entity.UserEntity;
import br.com.dbc.vemser.GymExploreAPI.exception.RegraDeNegocioException;
import br.com.dbc.vemser.GymExploreAPI.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordResetService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public void requestPasswordReset(String userEmail) throws RegraDeNegocioException {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RegraDeNegocioException("E-mail não encontrado."));

        String token = UUID.randomUUID().toString();

        user.setPasswordResetToken(token);
        user.setTokenExpirationDate(LocalDateTime.now().plusHours(1));

        userRepository.save(user);

        try {
            emailService.sendPasswordResetEmail(user, token);
        } catch (MessagingException e) {
            log.error("Falha ao enviar e-mail de redefinição de senha para {}", userEmail, e);
            throw new RegraDeNegocioException("Não foi possível enviar o e-mail de redefinição.");
        }
    }

    public void resetPassword(String token, String newPassword) throws RegraDeNegocioException {
        UserEntity user = userRepository.findByPasswordResetToken(token)
                .orElseThrow(() -> new RegraDeNegocioException("Token inválido ou expirado."));

        if (user.getTokenExpirationDate().isBefore(LocalDateTime.now())) {
            throw new RegraDeNegocioException("Token expirado.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));

        user.setPasswordResetToken(null);
        user.setTokenExpirationDate(null);

        userRepository.save(user);
    }
}