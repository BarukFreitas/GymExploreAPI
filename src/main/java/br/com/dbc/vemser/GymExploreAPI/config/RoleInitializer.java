package br.com.dbc.vemser.GymExploreAPI.config;

import br.com.dbc.vemser.GymExploreAPI.entity.Role;
import br.com.dbc.vemser.GymExploreAPI.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RoleInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        // Verifica e cria a ROLE_USER se não existir
        createRoleIfNotExists("ROLE_USER");
        // Verifica e cria a ROLE_GYM_OWNER se não existir
        createRoleIfNotExists("ROLE_GYM_OWNER");

        System.out.println("Roles inicializadas com sucesso!");
    }

    private void createRoleIfNotExists(String roleName) {
        Optional<Role> existingRole = roleRepository.findByRoleName(roleName);
        if (existingRole.isEmpty()) {
            Role newRole = new Role();
            newRole.setRoleName(roleName);
            roleRepository.save(newRole);
            System.out.println("Role '" + roleName + "' criada.");
        } else {
            System.out.println("Role '" + roleName + "' já existe.");
        }
    }
}