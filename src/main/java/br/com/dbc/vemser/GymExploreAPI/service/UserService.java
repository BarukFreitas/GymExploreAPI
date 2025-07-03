package br.com.dbc.vemser.GymExploreAPI.service;

import br.com.dbc.vemser.GymExploreAPI.dto.UserRegisterDTO;
import br.com.dbc.vemser.GymExploreAPI.dto.UserResponseDTO;
import br.com.dbc.vemser.GymExploreAPI.entity.Role;
import br.com.dbc.vemser.GymExploreAPI.entity.UserEntity;
import br.com.dbc.vemser.GymExploreAPI.exception.RegraDeNegocioException;
import br.com.dbc.vemser.GymExploreAPI.repository.RoleRepository;
import br.com.dbc.vemser.GymExploreAPI.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors; // Manter este import para o Collectors.toSet()

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO registerUser(UserRegisterDTO userRegisterDTO) throws RegraDeNegocioException {
        if (userRepository.existsByUsername(userRegisterDTO.getUsername())) {
            throw new RegraDeNegocioException("Nome de usuário já existe.");
        }
        if (userRepository.existsByEmail(userRegisterDTO.getEmail())) {
            throw new RegraDeNegocioException("E-mail já cadastrado.");
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userRegisterDTO.getUsername());
        userEntity.setEmail(userRegisterDTO.getEmail());
        userEntity.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword())); 
        userEntity.setPoints(0); // Manter a inicialização de pontos da 'main'

        Role selectedRole = roleRepository.findByRoleName(userRegisterDTO.getRole())
                .orElseThrow(() -> new RegraDeNegocioException("Role '" + userRegisterDTO.getRole() + "' não encontrada no banco de dados."));

        userEntity.setRoles(Collections.singleton(selectedRole));

        UserEntity savedUserEntity = userRepository.save(userEntity);
        System.out.println("DEBUG: Usuário salvo no BD (ID): " + savedUserEntity.getId());
        System.out.println("DEBUG: Roles do usuário salvo: " + savedUserEntity.getRoles().stream().map(Role::getRoleName).collect(Collectors.toSet()));

        return toUserResponseDTO(savedUserEntity); 
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
                return Optional.of(toUserResponseDTO(userEntity));
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
        dto.setPoints(userEntity.getPoints());
        dto.setRoles(userEntity.getRoles());
        return dto;
    }
}