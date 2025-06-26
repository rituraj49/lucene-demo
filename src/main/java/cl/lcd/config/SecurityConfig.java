package cl.lcd.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    SecurityFilterChain resourceSecurityFilterChain(
        HttpSecurity http
//        Converter<Jwt, AbstractAuthenticationToken> authenticationTokenConverter
    ) throws Exception {
//        http.oauth2ResourceServer(resourceServer -> {
//            resourceServer.jwt(jwtDecoder -> {
//                jwtDecoder.jwtAuthenticationConverter(authenticationTokenConverter);
//            });
//        });

        http.sessionManagement(sessions -> {
            sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }).csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults());

        http.authorizeHttpRequests(requests -> {
           requests.requestMatchers("/secured").authenticated();
              requests.anyRequest().permitAll();
        });

        return http.build();
    }
}
