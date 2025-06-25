package br.com.dbc.vemser.GymExploreAPI.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Desabilita CSRF para facilitar testes com Postman/Insomnia
                .authorizeRequests()
                .antMatchers("/**").permitAll() // Permite acesso a TODAS as rotas
                .and()
                .headers().frameOptions().sameOrigin(); // Permite que o H2 Console seja exibido em um frame
        return http.build();
    }
}