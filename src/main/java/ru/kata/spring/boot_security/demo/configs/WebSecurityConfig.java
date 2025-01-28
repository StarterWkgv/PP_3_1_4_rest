package ru.kata.spring.boot_security.demo.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import ru.kata.spring.boot_security.demo.util.RoleType;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final SuccessUserHandler successUserHandler;

    public WebSecurityConfig(SuccessUserHandler successUserHandler) {
        this.successUserHandler = successUserHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .and()
                .authorizeRequests()
                .antMatchers("/admin/**","/api/users/**").hasAuthority(RoleType.ADMIN.name())
                .antMatchers("/user").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.USER.name())
                .antMatchers("/", "/index" ).permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().successHandler(successUserHandler)
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .permitAll();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // аутентификация inMemory (закомментировать в объявлении UserServiceImp  UserDetailsService
    // и аннотацию @Override в методе loadUserByUsername).

//    @Bean
//    @Override
//    public UserDetailsService userDetailsService() {
//        UserDetails user =
//                User.builder()
//                        .username("user")
//                        .passwordEncoder(passwordEncoder()::encode)
//                        .password("user")
//                        .authorities("USER")
//                        .build();
//        UserDetails admin =
//                User.builder()
//                        .username("admin")
//                        .passwordEncoder(passwordEncoder()::encode)
//                        .password("admin")
//                        .authorities("ADMIN")
//                        .build();
//        System.out.println(admin.getAuthorities());
//
//        return new InMemoryUserDetailsManager(user, admin);
//    }
}