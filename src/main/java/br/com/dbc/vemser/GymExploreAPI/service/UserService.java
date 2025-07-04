package br.com.dbc.vemser.GymExploreAPI.service;

import br.com.dbc.vemser.GymExploreAPI.dto.UserRegisterDTO;
import br.com.dbc.vemser.GymExploreAPI.dto.UserResponseDTO;
import br.com.dbc.vemser.GymExploreAPI.entity.Role;
import br.com.dbc.vemser.GymExploreAPI.entity.UserEntity;
import br.com.dbc.vemser.GymExploreAPI.exception.RegraDeNegocioException;
import br.com.dbc.vemser.GymExploreAPI.repository.RoleRepository;
import br.com.dbc.vemser.GymExploreAPI.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    @Transactional
    public UserResponseDTO registerUser(UserRegisterDTO userRegisterDTO) throws RegraDeNegocioException {
        if (userRepository.existsByUsername(userRegisterDTO.getUsername())) {
            throw new RegraDeNegocioException("Nome de utilizador já existe.");
        }
        if (userRepository.existsByEmail(userRegisterDTO.getEmail())) {
            throw new RegraDeNegocioException("E-mail já registado.");
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userRegisterDTO.getUsername());
        userEntity.setEmail(userRegisterDTO.getEmail());
        userEntity.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        userEntity.setPoints(0);

        Role selectedRole = roleRepository.findByRoleName(userRegisterDTO.getRole())
                .orElseThrow(() -> new RegraDeNegocioException("Role '" + userRegisterDTO.getRole() + "' não encontrada."));

        userEntity.setRoles(Collections.singleton(selectedRole));

        UserEntity savedUserEntity = userRepository.save(userEntity);

        try {
            emailService.sendWelcomeEmail(savedUserEntity);
        } catch (MessagingException e) {
            log.error("Não foi possível enviar o e-mail de boas-vindas para: {}", savedUserEntity.getEmail(), e);
        }

        return toUserResponseDTO(savedUserEntity);
    }

    public Optional<UserResponseDTO> loginUser(String username, String password) {
        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(username);

        if (userEntityOptional.isPresent()) {
            UserEntity userEntity = userEntityOptional.get();
            if (passwordEncoder.matches(password, userEntity.getPassword())) {
                return Optional.of(toUserResponseDTO(userEntity));
            }
        }
        return Optional.empty();
    }

    public Optional<UserResponseDTO> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(this::toUserResponseDTO);
    }

    public UserEntity findById(Long userId) throws RegraDeNegocioException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RegraDeNegocioException("Utilizador não encontrado!"));
    }

    public Integer getUserPoints(Long userId) throws RegraDeNegocioException {
        UserEntity user = findById(userId);
        return user.getPoints();
    }

    @Transactional
    public void deleteUser(Long userId) throws RegraDeNegocioException {
        if (!userRepository.existsById(userId)) {
            throw new RegraDeNegocioException("Usuário não encontrado para exclusão.");
        }
        userRepository.deleteById(userId);
    }

    private UserResponseDTO toUserResponseDTO(UserEntity userEntity) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(userEntity.getId());
        dto.setUsername(userEntity.getUsername());
        dto.setEmail(userEntity.getEmail());
        dto.setPoints(userEntity.getPoints());
        dto.setRoles(userEntity.getRoles());
        return dto;
    }
}