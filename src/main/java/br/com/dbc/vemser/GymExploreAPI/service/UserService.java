package br.com.dbc.vemser.GymExploreAPI.service;

import br.com.dbc.vemser.GymExploreAPI.dto.UserRegisterDTO;
import br.com.dbc.vemser.GymExploreAPI.dto.UserResponseDTO;
import br.com.dbc.vemser.GymExploreAPI.entity.UserEntity;
import br.com.dbc.vemser.GymExploreAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

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

        UserEntity savedUserEntity = userRepository.save(userEntity);

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(savedUserEntity.getId());
        userResponseDTO.setUsername(savedUserEntity.getUsername());
        userResponseDTO.setEmail(savedUserEntity.getEmail());
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
                    return dto;
                });
    }
}