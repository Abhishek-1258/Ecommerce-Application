package com.ecommerce.userService.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

	private final JwtAuthenticationHelper authenticationHelper;

	@Autowired
	UserDetailsService userDetailsService;

	public JwtAuthenticationFilter(JwtAuthenticationHelper authenticationHelper) {
		this.authenticationHelper = authenticationHelper;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = null;
		String username = null;

		String requestHeader = request.getHeader("Authorization");
		if(requestHeader != null && requestHeader.startsWith("Bearer")) {
			token = requestHeader.split(" ")[1];
			username = authenticationHelper.getUsernameFromToken(token);
			if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails details = userDetailsService.loadUserByUsername(username);
				if(!authenticationHelper.isTokenExpired(token)) {
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(token, null,details.getAuthorities());
					authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				}
			}
		}
		
		filterChain.doFilter(request, response);
	}

}
