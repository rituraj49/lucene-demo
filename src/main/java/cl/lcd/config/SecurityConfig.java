package cl.lcd.config;

import org.apache.tomcat.util.file.ConfigurationSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.*;

@Configuration
public class SecurityConfig {

    private Environment env;

    public SecurityConfig(Environment env) {
        this.env = env;
    }
    @Bean
    SecurityFilterChain resourceSecurityFilterChain(
        HttpSecurity http,
        Converter<Jwt, AbstractAuthenticationToken> authenticationTokenConverter
    ) throws Exception {
        http.oauth2ResourceServer(resourceServer -> {
            resourceServer.jwt(jwtDecoder -> {
                jwtDecoder.jwtAuthenticationConverter(authenticationTokenConverter);
            });
        });

        http
            .sessionManagement(sessions -> {
                sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            })
            .csrf(csrf -> csrf.disable())
            .cors(c -> {

                c.configurationSource(corsConfigurationSource());
            });

        http.authorizeHttpRequests(requests -> {
           requests.requestMatchers("/secured").authenticated();
              requests.anyRequest().permitAll();
        });

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        return request -> {
//                    ConfigurationSource source = request -> {
            CorsConfiguration config = new CorsConfiguration();

//            List<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
//
//            if(activeProfiles.contains("dev")) {
//                config.setAllowedOrigins(List.of("http://localhost:3000"));
//            } else if(activeProfiles.contains("prod")) {
//                config.setAllowedOrigins(List.of("https://flightservice.com"));
//            } else {
//                config.setAllowedOrigins(List.of());
//            }
            config.setAllowedOrigins(List.of("*"));
            config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            config.setAllowedHeaders(List.of("*"));
            config.setAllowCredentials(false);
            return config;
        };
    }

    @Bean
    JwtAuthenticationConverter authenticationConverter(AuthoritiesConverter authoritiesConverter) {
        var authenticationConverter = new  JwtAuthenticationConverter();
        authenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            return authoritiesConverter.convert(jwt.getClaims());
        });
        return authenticationConverter;
    }

    @Bean
    AuthoritiesConverter realmRolesConverter() {
        return (claims) -> {
            var realmAccess = Optional.ofNullable(
                    (Map<String, Object>) claims.get("realm_access"));
            var resourceAccess = Optional.ofNullable(
                    (Map<String, Object>) claims.get("resource_access"));

            var roles = resourceAccess.flatMap(map -> Optional.ofNullable(
                    (List<String>) map.get("roles")));
//            roles.map(List::stream)
//                    .orElse(Stream.empty())
            return roles.stream().flatMap(Collection::stream)
                    .map(SimpleGrantedAuthority::new)
                    .map(GrantedAuthority.class::cast)
                    .toList();
        };
    }
}
