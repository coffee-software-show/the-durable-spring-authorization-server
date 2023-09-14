package bootiful.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class GatewayConfiguration {

    @Bean
    RouteLocator gateway(RouteLocatorBuilder rlb) {
        var apiPrefix = "/api/";
        return rlb
                .routes()
                // <1>
                .route(rs -> rs
                        .path(apiPrefix + "**")
                        .filters(f -> f
                                .tokenRelay()
                                .rewritePath(apiPrefix + "(?<segment>.*)", "/$\\{segment}")
                        )
                        .uri("http://localhost:8081"))
                // <2>
                .route(rs -> rs
                        .path("/**")
                        .uri("http://localhost:8020")
                )
                .build();
    }

}
