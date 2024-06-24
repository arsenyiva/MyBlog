package com.iva.blog.config;

import com.iva.blog.security.UsersDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Конфигурация безопасности для веб-приложения с использованием Spring Security.
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UsersDetailsService usersDetailsService;

    @Autowired
    public SecurityConfig(UsersDetailsService usersDetailsService) {
        this.usersDetailsService = usersDetailsService;
    }

    /**
     * Конфигурация HTTP безопасности.
     *
     * @param http объект HttpSecurity для настройки безопасности.
     * @throws Exception в случае ошибки настройки.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/admin").hasRole("ADMIN")
                .antMatchers("/auth/login", "/auth/registration", "/mainPage", "/auth/verify-registration/**", "/auth/**").permitAll()
                .regexMatchers("/user/\\d+/article/\\d+.*").permitAll()
                .antMatchers("/currentUserPage/update").authenticated()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/auth/login")
                .loginProcessingUrl("/process_login")
                .defaultSuccessUrl("/mainPage", true)
                .failureUrl("/auth/login?error")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/auth/login")
                .and()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> response.sendRedirect("/mainPage"))
                .accessDeniedHandler((request, response, accessDeniedException) -> response.sendRedirect("/mainPage"))
                .and()
                .httpBasic();
    }

    /**
     * Конфигурация менеджера аутентификации.
     *
     * @param auth объект AuthenticationManagerBuilder для настройки аутентификации.
     * @throws Exception в случае ошибки настройки.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usersDetailsService)
                .passwordEncoder(getPasswordEncoder());
    }

    /**
     * Бин для кодировщика паролей.
     *
     * @return PasswordEncoder для кодирования паролей.
     */
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}