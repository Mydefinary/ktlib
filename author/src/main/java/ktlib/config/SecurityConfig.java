package ktlib.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
                // actuator endpoints permit all
                .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
                // auth endpoints permit all
                .antMatchers("/auth/register/**", "/auth/login/**").permitAll()
                // admin role
                .antMatchers("/admin/**").hasRole("ADMIN")
                // author role for author endpoints
                .antMatchers("/author/**").hasRole("AUTHOR")
                // GET and PATCH on author endpoints allowed for ADMIN
                .antMatchers(HttpMethod.GET, "/author/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/author/**").hasRole("ADMIN")
                .antMatchers("/actuator/**").permitAll()
                // any other requests require authentication
                .anyRequest().authenticated();

        // add JWT authentication filter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
