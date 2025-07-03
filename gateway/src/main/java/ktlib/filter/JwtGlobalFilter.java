package ktlib.filter;

import io.jsonwebtoken.Claims;
import ktlib.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtGlobalFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;

@Override
public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

    if (authHeader != null && authHeader.startsWith("Bearer ")) {
        String token = authHeader.substring(7);

        try {
            Claims claims = jwtUtil.getClaimsFromToken(token);

            if (jwtUtil.isTokenExpired(claims)) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String email = claims.getSubject();
            String role = claims.get("role", String.class);

            exchange.getAttributes().put("userEmail", email);
            exchange.getAttributes().put("userRole", role);

        } catch (Exception e) {
            // 잘못된 토큰, 파싱 오류, 만료된 경우 등 모두 401 반환
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    return chain.filter(exchange);
}


    @Override
    public int getOrder() {
        return -1; // RoleAuthorizationFilter보다 먼저 동작
    }
}
