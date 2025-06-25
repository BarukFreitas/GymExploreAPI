package br.com.dbc.vemser.GymExploreAPI.service;

import br.com.dbc.vemser.GymExploreAPI.dto.UserLoginDTO;
import br.com.dbc.vemser.GymExploreAPI.dto.UserRegisterDTO;
import br.com.dbc.vemser.GymExploreAPI.dto.UserResponseDTO;
import br.com.dbc.vemser.GymExploreAPI.entity.UserEntity;
import br.com.dbc.vemser.GymExploreAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserResponseDTO registerUser(UserRegisterDTO userRegisterDTO) {
        if (userRepository.existsByUsername(userRegisterDTO.getUsername())) {
            throw new RuntimeException("Nome de usuário já existe.");
        }
        if (userRepository.existsByEmail(userRegisterDTO.getEmail())) {
            throw new RuntimeException("E-mail já cadastrado.");
        }

        // Mapeia DTO para Entidade
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userRegisterDTO.getUsername());
        userEntity.setEmail(userRegisterDTO.getEmail());
        userEntity.setPassword(userRegisterDTO.getPassword()); // Lembre-se de HASHEAR a senha em produção!

        UserEntity savedUser = userRepository.save(userEntity);

        // Mapeia Entidade para DTO de resposta
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(savedUser.getId());
        userResponseDTO.setUsername(savedUser.getUsername());
        userResponseDTO.setEmail(savedUser.getEmail());
        return userResponseDTO;
    }

    public Optional<UserResponseDTO> loginUser(String username, String password) {
        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(username);

        if (userEntityOptional.isPresent()) {
            UserEntity userEntity = userEntityOptional.get();
            // Em produção: usar BCryptPasswordEncoder.matches(rawPassword, encodedPassword)
            if (userEntity.getPassword().equals(password)) {
                UserResponseDTO userResponseDTO = new UserResponseDTO();
                userResponseDTO.setId(userEntity.getId());
                userResponseDTO.setUsername(userEntity.getUsername());
                userResponseDTO.setEmail(userEntity.getEmail());
                return Optional.of(userResponseDTO);
            }
        }
        return Optional.empty();
    }

    // Este método pode não ser necessário se o login retornar todas as informações relevantes
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