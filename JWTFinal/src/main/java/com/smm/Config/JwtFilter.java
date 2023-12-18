package com.smm.Config;

import java.io.IOException;
import java.security.Key;

import org.hibernate.service.spi.ServiceException;
import org.springframework.web.filter.GenericFilterBean;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtFilter extends GenericFilterBean {
	
	private final Key secretKey;

    public JwtFilter(Key secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        final HttpServletRequest req = (HttpServletRequest) request;
        final HttpServletResponse res = (HttpServletResponse) response;
        final String authHeader = req.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            throw new ServiceException("Missing Authorization");
        }

        final String token = authHeader.substring(7);

        try {
        	final Claims claims = Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
            req.setAttribute("claims", claims);
            chain.doFilter(request, response);
        } catch (SignatureException e) {
            System.out.println("Signature Exception: " + e.getMessage());
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
