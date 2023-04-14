package com.service.jewelry.config;

import com.service.jewelry.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final UserService userDetailService;

    @Autowired
    public SecurityConfig(@Lazy UserService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Bean
    public BCryptPasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailService);
        authProvider.setPasswordEncoder(getPasswordEncoder());
        return new ProviderManager(authProvider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/registration/**").permitAll()
                        .requestMatchers("/catalog/**").permitAll()
                        .requestMatchers("/product/**").permitAll()
                        .requestMatchers("/about/**").permitAll()
                        .requestMatchers("/address/**").permitAll()
                        .requestMatchers("/cart/**").permitAll()
                        .requestMatchers("/reviews/**").permitAll()
                        .requestMatchers("/addReview/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/login/**").permitAll()
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/main-page")
                        .permitAll()
                )
                .logout(logout -> logout
//                        .logoutUrl("/logout")
                                .clearAuthentication(true)
                                .invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID")
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .logoutSuccessUrl("/catalog?logout")
                                .permitAll()
                )
                .exceptionHandling().accessDeniedPage("/access-denied");
        return http.build();
    }
}
