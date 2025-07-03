package br.com.dbc.vemser.GymExploreAPI.service;

import br.com.dbc.vemser.GymExploreAPI.dto.UserRegisterDTO;
import br.com.dbc.vemser.GymExploreAPI.dto.UserResponseDTO;
import br.com.dbc.vemser.GymExploreAPI.entity.Role;
import br.com.dbc.vemser.GymExploreAPI.entity.UserEntity;
import br.com.dbc.vemser.GymExploreAPI.exception.RegraDeNegocioException;
import br.com.dbc.vemser.GymExploreAPI.repository.RoleRepository;
import br.com.dbc.vemser.GymExploreAPI.repository.UserRepository;
import lombok.RequiredArgsConstructor; // Importar
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO registerUser(UserRegisterDTO userRegisterDTO) {
        if (userRepository.existsByUsername(userRegisterDTO.getUsername())) {
            throw new RuntimeException("Nome de utilizador já existe.");
        }
        if (userRepository.existsByEmail(userRegisterDTO.getEmail())) {
            throw new RuntimeException("E-mail já registado.");
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userRegisterDTO.getUsername());
        userEntity.setEmail(userRegisterDTO.getEmail());
        userEntity.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        userEntity.setPoints(0);

        Role selectedRole = roleRepository.findByRoleName(userRegisterDTO.getRole())
                .orElseThrow(() -> new RuntimeException("Role '" + userRegisterDTO.getRole() + "' não encontrada."));

        userEntity.setRoles(Collections.singleton(selectedRole));

        UserEntity savedUserEntity = userRepository.save(userEntity);

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

    private UserResponseDTO toUserResponseDTO(UserEntity userEntity) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(userEntity.getId());
        dto.setUsername(userEntity.getUsername());
        dto.setEmail(userEntity.getEmail());
        dto.setPoints(userEntity.getPoints()); // Inclui os pontos
        dto.setRoles(userEntity.getRoles()); // Mantém as roles
        return dto;
    }
}