package br.com.dbc.vemser.GymExploreAPI.controller;

import br.com.dbc.vemser.GymExploreAPI.entity.UserEntity;
import br.com.dbc.vemser.GymExploreAPI.exception.RegraDeNegocioException;
import br.com.dbc.vemser.GymExploreAPI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody @Valid UserEntity user) throws RegraDeNegocioException {
        UserEntity registeredUser = userService.registerUser(user);
        Map<String, Object> response = new HashMap<>();
        response.put("id", registeredUser.getId());
        response.put("username", registeredUser.getUsername());
        response.put("email", registeredUser.getEmail());
        response.put("message", "Usuário registrado com sucesso!");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody UserEntity user) throws RegraDeNegocioException {
        Optional<UserEntity> loggedInUser = userService.loginUser(user.getUsername(), user.getPassword());

        Map<String, Object> response = new HashMap<>();
        response.put("id", loggedInUser.get().getId());
        response.put("username", loggedInUser.get().getUsername());
        response.put("email", loggedInUser.get().getEmail());
        response.put("message", "Login bem-sucedido!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}