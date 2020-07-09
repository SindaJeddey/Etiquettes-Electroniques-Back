package project.EE.security.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import project.EE.security.ApplicationRoles;
import project.EE.security.handlers.AuthenticationFailedHandler;
import project.EE.security.handlers.UserAccessDeniedHandler;
import project.EE.security.jwt.JwtUsernameAndPasswordFilter;
import project.EE.security.jwt.JwtVerifierFilter;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationSecurityConfiguration(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JwtUsernameAndPasswordFilter(authenticationManager()))
                .addFilterAfter(new JwtVerifierFilter(),JwtUsernameAndPasswordFilter.class)
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler());

    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("123"))
                .authorities(ApplicationRoles.ADMIN.getRoles())
                .build();
        UserDetails operator = User.builder()
                .username("operator")
                .password(passwordEncoder.encode("123"))
                .authorities(ApplicationRoles.OPERATOR.getRoles())
                .build();
        UserDetailsService service = new InMemoryUserDetailsManager(admin,operator);
        return service;
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new UserAccessDeniedHandler();
    }

    public AuthenticationFailureHandler authenticationFailureHandler(){
        return new AuthenticationFailedHandler();
    }
}
