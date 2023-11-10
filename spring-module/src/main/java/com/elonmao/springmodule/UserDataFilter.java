package com.elonmao.springmodule;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class UserDataFilter extends OncePerRequestFilter {

    private final Collection<String> personRoles = Arrays.asList("ROLE_admin");

    private boolean isAuthorized(Authentication authentication, Collection<String> authorities) {
        Iterator<? extends GrantedAuthority> var3 = authentication.getAuthorities().iterator();

        GrantedAuthority grantedAuthority;
        do {
            if (!var3.hasNext()) {
                return false;
            }

            grantedAuthority = var3.next();
        } while (!authorities.contains(grantedAuthority.getAuthority()));

        return true;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String[] uStrings = Arrays.stream(requestURI.split("/")).filter(s -> !"".equals(s)).toArray(String[]::new);
        if (uStrings.length > 1) {
            if ("persons".equals(uStrings[0])) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (!isAuthorized(authentication, personRoles)) {
                    UserDetails user = (UserDetails) authentication.getPrincipal();
                    if (!uStrings[1].equals(user.getUsername())) {
                        throw new AccessDeniedException("Access Denied");
                    }
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
