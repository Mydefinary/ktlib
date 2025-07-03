package ktlib.filter;

import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import reactor.core.publisher.Mono;

@Component
public class RoleAuthorizationFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        String role = (String) exchange.getAttribute("userRole");

        if (path.startsWith("/authors")) {
            if (!"AUTHOR".equals(role)) {
                return unauthorized(exchange);
            }
        } else if (path.startsWith("/manuscripts") || path.startsWith("/publishes")) {
            if (!isOneOf(role, "AUTHOR", "ADMIN")) {
                return unauthorized(exchange);
            }
        } else if (path.startsWith("/books")) {
            if (!isOneOf(role, "USER", "AUTHOR", "ADMIN")) {
                return unauthorized(exchange);
            }
        } else if (path.startsWith("/points")) {
            if (!isOneOf(role, "USER", "ADMIN")) {
                return unauthorized(exchange);
            }
        } else if (path.startsWith("/users")) {
            if (!"USER".equals(role)) {
                return unauthorized(exchange);
            }
        } else if (path.startsWith("/subscribeSus") || path.startsWith("/subscribeLists")) {
            if (!isOneOf(role, "USER", "ADMIN")) {
                return unauthorized(exchange);
            }
        } else if (path.startsWith("/periodSubscribes")) {
            if (!isOneOf(role, "USER", "ADMIN")) {
                return unauthorized(exchange);
            }
        } else if (path.startsWith("/")) { // 프론트 기본
            if (!isOneOf(role, "USER", "AUTHOR", "ADMIN")) {
                return unauthorized(exchange);
            }
        }

        return chain.filter(exchange);
    }

    private boolean isOneOf(String role, String... allowedRoles) {
        if (role == null) return false;
        for (String allowed : allowedRoles) {
            if (allowed.equals(role)) {
                return true;
            }
        }
        return false;
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return 1; // JwtAuthenticationFilter 실행 후 동작하도록 설정
    }
}
