package bootiful.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
class SecurityConfiguration {

    // <1>
    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange((authorize) -> authorize.anyExchange().authenticated())//<2>
                .csrf(ServerHttpSecurity.CsrfSpec::disable)// <3>
                .oauth2Login(Customizer.withDefaults())//<4>
                .oauth2Client(Customizer.withDefaults());
        return http.build();
    }


}
