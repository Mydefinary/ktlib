package ktlib;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.SecurityWebFilterChain;

import javax.crypto.SecretKey;
import io.jsonwebtoken.security.Keys;
import java.util.Collection;
import reactor.core.publisher.Flux;
import org.springframework.http.HttpMethod;

@Configuration
@EnableWebFluxSecurity
public class JwtAuthConfig {

    private final String secretKey = "yourSecretKeyExampleMustBeLongEnough123456!"; // 32+ chars

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
            .csrf().disable()
            .authorizeExchange(exchanges -> exchanges
                .pathMatchers("/actuator/**", "/login/**").permitAll()
                .pathMatchers(HttpMethod.POST, "/users").permitAll() // ✅ 회원가입 인증 없이 허용
                .pathMatchers("/admin/**").hasRole("ADMIN")
                .pathMatchers("/authors/**").hasRole("AUTHOR")
                .pathMatchers("/users/**").hasRole("USER")
                .anyExchange().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .jwtAuthenticationConverter(grantedAuthoritiesExtractor())
                )
            );

        return http.build();
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
        return NimbusReactiveJwtDecoder.withSecretKey(key).build();
    }

    @Bean
    public ReactiveJwtAuthenticationConverter grantedAuthoritiesExtractor() {
        ReactiveJwtAuthenticationConverter converter = new ReactiveJwtAuthenticationConverter();

        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Collection<GrantedAuthority> authorities = jwtGrantedAuthoritiesConverter.convert(jwt);
            return Flux.fromIterable(authorities); // ✅ Flux로 래핑
        });

        return converter;
    }
}
