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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import project.EE.security.ApplicationRoles;
import project.EE.security.handlers.AuthenticationFailedHandler;
import project.EE.security.handlers.UserAccessDeniedHandler;
import project.EE.security.jwt.JwtUsernameAndPasswordFilter;
import project.EE.security.jwt.JwtVerifierFilter;

import java.util.Arrays;

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
                .cors().and()
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

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler(){
        return new AuthenticationFailedHandler();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("X-Requested-With","Origin","Content-Type","Accept","Authorization"));
        configuration.setExposedHeaders(Arrays.asList("Access-Control-Allow-Headers", "Authorization", "x-xsrf-token",
                "Access-Control-Allow-Headers", "Origin", "Accept", "X-Requested-With",
                "Content-Type", "Access-Control-Request-Method", "Access-Control-Request-Headers"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}
