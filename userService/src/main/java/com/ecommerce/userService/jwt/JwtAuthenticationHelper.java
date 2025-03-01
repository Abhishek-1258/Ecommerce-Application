package com.ecommerce.userService.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureAlgorithm;

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

		return Jwts.parser()
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
                .subject(userDetails.getUsername()) 
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(key)
                .compact();
    }

}
