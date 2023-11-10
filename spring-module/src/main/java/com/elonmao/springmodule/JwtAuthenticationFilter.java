package com.elonmao.springmodule;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Stream;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final SecretKey KEY = Jwts.SIG.HS256.key().build();
    private static final int COOKIE_AGE = 4 * 24 * 60 * 60;
    private static final long TOKEN_EXPIRATION = COOKIE_AGE * 1000;
    private static final String COOKIE_NAME = "JWT";

    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public static void setJwt(HttpServletResponse response, String id) {
        long now = System.currentTimeMillis();
        Cookie cookie = new Cookie(COOKIE_NAME, Jwts.builder()
                .expiration(new Date(now + TOKEN_EXPIRATION))
                .issuedAt(new Date(now))
                .id(id)
                .signWith(KEY)
                .compact());
        cookie.setMaxAge(COOKIE_AGE);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();

        Optional<Cookie> optionalCookie = Optional.ofNullable(cookies)
                .map(Arrays::stream)
                .orElseGet(Stream::empty)
                .filter(cookie -> COOKIE_NAME.equals(cookie.getName()))
                .findFirst();

        if (optionalCookie.isPresent()) {
            Cookie cookie = optionalCookie.get();
            try {
                Claims claims = Jwts.parser()
                        .verifyWith(KEY)
                        .build()
                        .parseSignedClaims(cookie.getValue()).getPayload();
                String username = claims.getId();
                UserDetails user = userDetailsService.loadUserByUsername(username);
                Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                cookie.setMaxAge(0);
                response.addCookie(cookie);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }

        filterChain.doFilter(request, response);
    }
}
