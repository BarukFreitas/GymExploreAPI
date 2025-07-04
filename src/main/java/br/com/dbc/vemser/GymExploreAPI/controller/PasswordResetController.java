package br.com.dbc.vemser.GymExploreAPI.controller;

import br.com.dbc.vemser.GymExploreAPI.dto.ForgotPasswordDTO;
import br.com.dbc.vemser.GymExploreAPI.dto.ResetPasswordDTO;
import br.com.dbc.vemser.GymExploreAPI.exception.RegraDeNegocioException;
import br.com.dbc.vemser.GymExploreAPI.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/password")
@RequiredArgsConstructor
@Validated
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @PostMapping("/forgot")
    public ResponseEntity<Map<String, String>> forgotPassword(@Valid @RequestBody ForgotPasswordDTO forgotPasswordDTO) {
        try {
            passwordResetService.requestPasswordReset(forgotPasswordDTO.getEmail());
            return ResponseEntity.ok(Map.of("message", "Se o e-mail estiver registado, um link para redefinição de senha foi enviado."));
        } catch (RegraDeNegocioException e) {
            // Retornamos OK mesmo em caso de erro para não revelar quais e-mails estão registados
            return ResponseEntity.ok(Map.of("message", "Se o e-mail estiver registado, um link para redefinição de senha foi enviado."));
        }
    }

    @PostMapping("/reset")
    public ResponseEntity<Map<String, String>> resetPassword(@Valid @RequestBody ResetPasswordDTO resetPasswordDTO) {
        try {
            passwordResetService.resetPassword(resetPasswordDTO.getToken(), resetPasswordDTO.getNewPassword());
            return ResponseEntity.ok(Map.of("message", "Senha redefinida com sucesso!"));
        } catch (RegraDeNegocioException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}