package br.com.dbc.vemser.GymExploreAPI.service;

import br.com.dbc.vemser.GymExploreAPI.entity.UserEntity;
import br.com.dbc.vemser.GymExploreAPI.exception.RegraDeNegocioException;
import br.com.dbc.vemser.GymExploreAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserEntity registerUser(UserEntity user) throws RegraDeNegocioException {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RegraDeNegocioException("Nome de usuário já existe.");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RegraDeNegocioException("E-mail já cadastrado.");
        }
        return userRepository.save(user);
    }

    public Optional<UserEntity> loginUser(String username, String password) throws RegraDeNegocioException {
        Optional<UserEntity> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new RegraDeNegocioException("Nome de usuário ou senha inválidos.");
        }

        UserEntity user = userOptional.get();
        if (!user.getPassword().equals(password)) {
            throw new RegraDeNegocioException("Nome de usuário ou senha inválidos.");
        }
        return Optional.of(user);
    }

    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}