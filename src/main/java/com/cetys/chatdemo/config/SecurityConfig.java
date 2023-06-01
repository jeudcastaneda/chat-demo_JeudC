package com.cetys.chatdemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/register").anonymous()
                        .requestMatchers("/chat/**").authenticated()
                        .requestMatchers("/chat.html").authenticated()
                        .requestMatchers("/js/**").permitAll()
                        .requestMatchers("/ws/**").authenticated()
                ).formLogin((form) -> form
                        // .loginPage("/login")
                        .defaultSuccessUrl("/chat")
                        .permitAll()
                ).logout((logout) -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll()
                        .logoutSuccessHandler(((request, response, authentication) -> {
                            String sessionId = request.getRequestedSessionId();
                            sessionRegistry().removeSessionInformation(sessionId);

                            response.sendRedirect("/");
                        }))
                ).sessionManagement()
                .maximumSessions(1).sessionRegistry(sessionRegistry());

        return http.build();
    }
}
