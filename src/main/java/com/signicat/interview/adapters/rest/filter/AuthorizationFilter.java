package com.signicat.interview.adapters.rest.filter;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthorizationFilter extends BasicAuthenticationFilter {

    private static final String HEADER_NAME = "Authorization";
    private static final String KEY = "q3t6w9z$C&F)J@NcQfTjWnZr4u7x!A%D*G-KaPdSgUkXp2s5v8y/B?E(H+MbQeTh";

    public AuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        try {
            log.debug("Executing authorization filtering");
            final String header = request.getHeader(HEADER_NAME);
            if (header != null) {
                final UsernamePasswordAuthenticationToken authentication = authenticate(request);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } finally {
            chain.doFilter(request, response);
        }
    }

    private UsernamePasswordAuthenticationToken authenticate(HttpServletRequest request) {

        try {
            log.debug("Authenticating user on application. Request -> {}", request);
            return Optional.ofNullable(request.getHeader(HEADER_NAME))
                    .filter(token -> !token.isBlank())
                    .map(token -> Jwts.parserBuilder()
                            .setSigningKey(Keys.hmacShaKeyFor(KEY.getBytes()))
                            .build()
                            .parseClaimsJws(token)
                            .getBody())
                    .map(claim -> new UsernamePasswordAuthenticationToken(claim, null, Collections.emptyList()))
                    .orElseThrow(() -> new RuntimeException("Error during authentication"));
        } catch (ExpiredJwtException e) {
            log.error("Error authenticating user. This token has expired.", e);
            throw e;
        }
    }
}
