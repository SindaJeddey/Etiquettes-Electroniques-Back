package project.EE.security.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import project.EE.security.jwt.JwtUsernameAndPasswordFilter;
import project.EE.security.jwt.JwtVerifierFilter;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .addFilter(new JwtUsernameAndPasswordFilter(authenticationManager()))
                .addFilterAfter(new JwtVerifierFilter(),JwtUsernameAndPasswordFilter.class);
    }
}
