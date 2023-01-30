package com.Jedi.OnePlacementServer.security;

import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// VALIDATION OF JWT IN REQUESTS AND THUS REQUEST ACCESS TO API:
@Component // to enable auto-wiring of things;
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. Get Token from user request:
        String requestToken = request.getHeader("Authorization"); // "Auth..." is they key to header in request;

        System.out.println(requestToken); // something like bearer 21736ie.adf.hadst;

        // GET Username from token. implementation in JWT-helper;
        String username = null;
        String token = null;
        try {
            if (requestToken.startsWith("Bearer")) {
                token = requestToken.substring(7);
                try {
                    username = this.jwtTokenHelper.getUsernameFromToken(token);
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                } catch (MalformedJwtException e) {
                    System.out.println("Invalid JWT");
                }
            } else {
                System.out.println("Invalid JWT format, doesn't start with BEARER.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // 2. Validate Token:
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            if (this.jwtTokenHelper.validateToken(token, userDetails)) {
                // AUTHENTICATION SET KRE:
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            } else {
                System.out.println("Invalid payload in Token!");
            }
        } else {
            System.out.println("Username is NULL | Context Holder Not Null");
        }

        // we need to filter the request further, to API access maybe...
        filterChain.doFilter(request, response);
    }
}