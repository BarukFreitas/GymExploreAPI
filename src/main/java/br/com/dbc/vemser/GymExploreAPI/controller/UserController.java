package br.com.dbc.vemser.GymExploreAPI.controller;

import br.com.dbc.vemser.GymExploreAPI.dto.UserLoginDTO;
import br.com.dbc.vemser.GymExploreAPI.dto.UserRegisterDTO;
import br.com.dbc.vemser.GymExploreAPI.dto.UserResponseDTO;
import br.com.dbc.vemser.GymExploreAPI.exception.RegraDeNegocioException;
import br.com.dbc.vemser.GymExploreAPI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserRegisterDTO userRegisterDTO) throws RegraDeNegocioException {
        UserResponseDTO registeredUserDTO = userService.registerUser(userRegisterDTO);
        Map<String, Object> response = new HashMap<>();
        response.put("id", registeredUserDTO.getId());
        response.put("username", registeredUserDTO.getUsername());
        response.put("email", registeredUserDTO.getEmail());
        response.put("message", "Usuário registrado com sucesso!");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDTO userLoginDTO) {
        Optional<UserResponseDTO> loggedInUserDTO = userService.loginUser(userLoginDTO.getUsername(), userLoginDTO.getPassword());

        if (loggedInUserDTO.isPresent()) {
            UserResponseDTO user = loggedInUserDTO.get();
            Map<String, Object> response = new HashMap<>();
            response.put("id", user.getId());
            response.put("username", user.getUsername());
            response.put("email", user.getEmail());
            response.put("message", "Login bem-sucedido!");
            // Adicionar as roles aqui
            response.put("roles", user.getRoles().stream()
                    .map(role -> role.getRoleName())
                    .collect(Collectors.toList()));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Nome de usuário ou senha inválidos.");
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
    }
    @GetMapping("/{id}/points")
    public ResponseEntity<Map<String, Integer>> getUserPoints(@PathVariable Long id) {
        try {
            Integer points = userService.getUserPoints(id);
            Map<String, Integer> response = new HashMap<>();
            response.put("points", points);
            return ResponseEntity.ok(response);
        } catch (RuntimeException | RegraDeNegocioException e) {
            return ResponseEntity.notFound().build();
        }
    }
}