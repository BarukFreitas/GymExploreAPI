package br.com.dbc.vemser.GymExploreAPI.service;

import br.com.dbc.vemser.GymExploreAPI.dto.UserRegisterDTO;
import br.com.dbc.vemser.GymExploreAPI.dto.UserResponseDTO;
import br.com.dbc.vemser.GymExploreAPI.entity.UserEntity;
import br.com.dbc.vemser.GymExploreAPI.entity.Role;
import br.com.dbc.vemser.GymExploreAPI.exception.RegraDeNegocioException;
import br.com.dbc.vemser.GymExploreAPI.repository.UserRepository;
import br.com.dbc.vemser.GymExploreAPI.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponseDTO registerUser(UserRegisterDTO userRegisterDTO) {
        if (userRepository.existsByUsername(userRegisterDTO.getUsername())) {
            throw new RuntimeException("Nome de usuário já existe.");
        }
        if (userRepository.existsByEmail(userRegisterDTO.getEmail())) {
            throw new RuntimeException("E-mail já cadastrado.");
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userRegisterDTO.getUsername());
        userEntity.setEmail(userRegisterDTO.getEmail());
        userEntity.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));

        Role selectedRole = roleRepository.findByRoleName(userRegisterDTO.getRole())
                .orElseThrow(() -> new RuntimeException("Role '" + userRegisterDTO.getRole() + "' não encontrada no banco de dados."));

        userEntity.setRoles(Collections.singleton(selectedRole));

        UserEntity savedUserEntity = userRepository.save(userEntity);

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(savedUserEntity.getId());
        userResponseDTO.setUsername(savedUserEntity.getUsername());
        userResponseDTO.setEmail(savedUserEntity.getEmail());
        userResponseDTO.setRoles(savedUserEntity.getRoles());
        return userResponseDTO;
    }

    public Optional<UserResponseDTO> loginUser(String username, String password) {
        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(username);

        if (userEntityOptional.isPresent()) {
            UserEntity userEntity = userEntityOptional.get();
            if (passwordEncoder.matches(password, userEntity.getPassword())) {
                UserResponseDTO userResponseDTO = new UserResponseDTO();
                userResponseDTO.setId(userEntity.getId());
                userResponseDTO.setUsername(userEntity.getUsername());
                userResponseDTO.setEmail(userEntity.getEmail());
                userResponseDTO.setRoles(userEntity.getRoles());
                return Optional.of(userResponseDTO);
            }
        }
        return Optional.empty();
    }

    public Optional<UserResponseDTO> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userEntity -> {
                    UserResponseDTO dto = new UserResponseDTO();
                    dto.setId(userEntity.getId());
                    dto.setUsername(userEntity.getUsername());
                    dto.setEmail(userEntity.getEmail());
                    dto.setRoles(userEntity.getRoles());
                    return dto;
                });
    }

    public UserEntity findById(Long userId) throws RegraDeNegocioException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado!"));
    }
}