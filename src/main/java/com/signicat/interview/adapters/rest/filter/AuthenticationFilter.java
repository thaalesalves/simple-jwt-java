package com.signicat.interview.adapters.rest.filter;

import java.io.IOException;
import java.security.Key;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.signicat.interview.adapters.data.entity.SubjectEntity;
import com.signicat.interview.application.service.UserService;
import com.signicat.interview.domain.model.Subject;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final String KEY = "q3t6w9z$C&F)J@NcQfTjWnZr4u7x!A%D*G-KaPdSgUkXp2s5v8y/B?E(H+MbQeTh";
    private static final Long EXPIRATION_TIME = 1000L * 60 * 30;

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException {

        try {
            final Subject subject = new ObjectMapper().readValue(req.getInputStream(), Subject.class);
            final UsernamePasswordAuthenticationToken tokenData = new UsernamePasswordAuthenticationToken(
                    subject.getUsername(), subject.getPassword());

            return authenticationManager.authenticate(tokenData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
            Authentication auth) throws IOException, ServletException {

        final SubjectEntity subject = userService.retrieveByUsername(((User) auth.getPrincipal()).getUsername());
        final Date exp = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
        final Key key = Keys.hmacShaKeyFor(KEY.getBytes());
        final String token = Jwts.builder()
                .claim("sub", subject.getId())
                .claim("username", subject.getUsername())
                //.claim("groups", subject.getGroups())
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(exp)
                .compact();

        res.addHeader("token", token);
    }
}
