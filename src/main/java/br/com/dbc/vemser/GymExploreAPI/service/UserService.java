// src/main/java/br/com/dbc.vemser.GymExploreAPI/service/UserService.java
package br.com.dbc.vemser.GymExploreAPI.service;

import br.com.dbc.vemser.GymExploreAPI.dto.UserRegisterDTO;
import br.com.dbc.vemser.GymExploreAPI.dto.UserResponseDTO;
import br.com.dbc.vemser.GymExploreAPI.entity.UserEntity;
import br.com.dbc.vemser.GymExploreAPI.entity.Role;
import br.com.dbc.vemser.GymExploreAPI.exception.RegraDeNegocioException; // Importar a exceção
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

    public UserResponseDTO registerUser(UserRegisterDTO userRegisterDTO) throws RegraDeNegocioException { // Adicionar throws
        if (userRepository.existsByUsername(userRegisterDTO.getUsername())) {
            throw new RegraDeNegocioException("Nome de usuário já existe."); // Mudar para RegraDeNegocioException
        }
        if (userRepository.existsByEmail(userRegisterDTO.getEmail())) {
            throw new RegraDeNegocioException("E-mail já cadastrado."); // Mudar para RegraDeNegocioException
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userRegisterDTO.getUsername());
        userEntity.setEmail(userRegisterDTO.getEmail());

        System.out.println("DEBUG: Senha bruta (registro): " + userRegisterDTO.getPassword());
        String encodedPassword = passwordEncoder.encode(userRegisterDTO.getPassword());
        System.out.println("DEBUG: Senha codificada (registro): " + encodedPassword);
        userEntity.setPassword(encodedPassword);

        Role selectedRole = roleRepository.findByRoleName(userRegisterDTO.getRole())
                .orElseThrow(() -> new RegraDeNegocioException("Role '" + userRegisterDTO.getRole() + "' não encontrada no banco de dados.")); // Mudar para RegraDeNegocioException

        userEntity.setRoles(Collections.singleton(selectedRole));

        UserEntity savedUserEntity = userRepository.save(userEntity);
        System.out.println("DEBUG: Usuário salvo no BD (ID): " + savedUserEntity.getId());
        System.out.println("DEBUG: Roles do usuário salvo: " + savedUserEntity.getRoles().stream().map(Role::getRoleName).collect(Collectors.toSet()));

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(savedUserEntity.getId());
        userResponseDTO.setUsername(savedUserEntity.getUsername());
        userResponseDTO.setEmail(savedUserEntity.getEmail());
        userResponseDTO.setRoles(savedUserEntity.getRoles());
        return userResponseDTO;
    }

    public Optional<UserResponseDTO> loginUser(String username, String password) {
        System.out.println("DEBUG: Tentando logar com username: " + username);
        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(username);

        if (userEntityOptional.isPresent()) {
            UserEntity userEntity = userEntityOptional.get();
            System.out.println("DEBUG: Senha do BD para " + username + ": " + userEntity.getPassword());
            System.out.println("DEBUG: Senha bruta fornecida para " + username + ": " + password);

            if (passwordEncoder.matches(password, userEntity.getPassword())) {
                System.out.println("DEBUG: Senhas correspondem!");
                UserResponseDTO userResponseDTO = new UserResponseDTO();
                userResponseDTO.setId(userEntity.getId());
                userResponseDTO.setUsername(userEntity.getUsername());
                userResponseDTO.setEmail(userEntity.getEmail());
                userResponseDTO.setRoles(userEntity.getRoles());
                return Optional.of(userResponseDTO);
            } else {
                System.out.println("DEBUG: Senhas NÃO correspondem!");
            }
        } else {
            System.out.println("DEBUG: Usuário " + username + " NÃO encontrado no BD.");
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