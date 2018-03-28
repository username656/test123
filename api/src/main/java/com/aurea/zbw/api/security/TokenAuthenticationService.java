package com.aurea.zbw.api.security;

import com.aurea.zbw.api.ApiProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

/**
 * This service helps to manage the JWT Tokens
 *
 * References:
 * - <a href="https://auth0.com/blog/securing-spring-boot-with-jwts/">Securing Spring Boot with JWS</a>
 * - <a href="https://www.toptal.com/java/rest-security-with-jwt-spring-security-and-java">Rest Security with JWT</a>
 */
@RequiredArgsConstructor
@Service
public class TokenAuthenticationService {

    private static final String AUTHORITIES_SEPARATOR = ",";
    private static final String AUTHORITIES_CLAIM = "Authorities";

    private final ApiProperties properties;

    /**
     * @param authentication The authentication data to create the token
     * @return A token created from the Authentication data provided
     */
    public String getToken(final Authentication authentication) {
        return Jwts.builder()
            .setSubject(authentication.getName())
            .claim(AUTHORITIES_CLAIM, authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(AUTHORITIES_SEPARATOR)))
            .setExpiration(new Date(System.currentTimeMillis() + properties.getApi().getSecurity().getJwt().getExpiration()))
            .signWith(SignatureAlgorithm.HS512, properties.getApi().getSecurity().getJwt().getSecret())
            .compact();
    }

    /**
     * @param token The token to be analyzed
     * @return The authentication obtained from the token
     */
    public Optional<Authentication> getAuthentication(final String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(properties.getApi().getSecurity().getJwt().getSecret())
            .parseClaimsJws(token.replace(properties.getApi().getSecurity().getAuthentication().getType(), ""))
            .getBody();
        String username = claims.getSubject();
        Set<GrantedAuthority> authorities =
            Arrays.stream(claims
                .get(AUTHORITIES_CLAIM, String.class)
                .split(AUTHORITIES_SEPARATOR)
            ).map(SimpleGrantedAuthority::new)
            .collect(Collectors.toSet());

        // For anonymous logins there is no attached user
        return username == null ? Optional.empty() :
            Optional.of(new UsernamePasswordAuthenticationToken(username, null, authorities));
    }

}
