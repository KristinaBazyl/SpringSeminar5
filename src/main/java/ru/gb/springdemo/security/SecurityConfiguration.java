package ru.gb.springdemo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
public class SecurityConfiguration {


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .authorizeHttpRequests(config -> config
//                        .requestMatchers("/users/**").hasAuthority("user")
//                        .requestMatchers("/users/**").hasAuthority("admin")
//                        .requestMatchers("/ui/readers/**").hasAuthority("reader")
//                        .requestMatchers("/books/**").authenticated()
                        .anyRequest().permitAll()
                )
                .formLogin(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .build();

    }
}
