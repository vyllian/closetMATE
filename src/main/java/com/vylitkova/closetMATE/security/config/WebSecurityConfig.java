package com.vylitkova.closetMATE.security.config;

import com.vylitkova.closetMATE.security.CustomAuthenticationEntryPoint;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import com.vylitkova.closetMATE.entity.user.UserService;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig  {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())  // Вимкнення CSRF (за потреби)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/registration/**", "/api/public/**").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(httpBasic -> httpBasic
                        .authenticationEntryPoint(customAuthenticationEntryPoint) // Встановлення кастомного EntryPoint
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(customAuthenticationEntryPoint) // Також обробка помилок аутентифікації
                )
                //.httpBasic(Customizer.withDefaults()) // Використання стандартного логіну
                //.formLogin(Customizer.withDefaults())
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(userService);
        return provider;
    }


}
