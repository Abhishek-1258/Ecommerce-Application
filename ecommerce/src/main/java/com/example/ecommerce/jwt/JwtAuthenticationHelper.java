package com.example.ecommerce.jwt;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtAuthenticationHelper {

private static final String SECRET_KEY = "7bb563f0701f63eff40182783072d66a444f9b0e0df94bc6aae2d56a86e9dc79b2e5253c859a8379dd0b55005ec2b6d62b3989e080104b6c9423aa1d28f2de52";
	
	private static final long JWT_TOKEN_VALIDITY = 60*60;
	
	public String getUsernameFromToken(String token) {
		Claims claims = getClaims(token);
		return claims.getSubject();
	}
	
	
	public Claims getClaims(String token) {
	    SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));

	    return Jwts.parserBuilder()  
	            .setSigningKey(key) 
	            .build()
	            .parseClaimsJws(token)
	            .getBody();
	}
	
	public boolean isTokenExpired(String token) {
		Claims claims = getClaims(token);
		Date expiryDate = claims.getExpiration();
		return expiryDate.before(new Date());
	}

	public String generateToken(UserDetails userDetails) {
	    SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));

	    return Jwts.builder()
	            .setSubject(userDetails.getUsername())  // ✅ Use setSubject() instead of subject()
	            .setIssuedAt(new Date(System.currentTimeMillis()))
	            .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
	            .signWith(key)
	            .compact();
	}

}
