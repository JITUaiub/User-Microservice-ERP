package com.example.microservice.eurekaservice.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.example.microservice.eurekaservice.security.model.UserPrinciple;

@Component
public class JwtTokenProvider {

	// @Value("${grokonez.app.jwtSecret}")
	private String jwtSecret = "TheVeryVerySecretKey";

	// @Value("${grokonez.app.jwtExpiration}")
	private int jwtExpiration = 60;

	public String generateJwtToken(Authentication authentication) {

		UserPrinciple userPrincipal = (UserPrinciple) authentication
				.getPrincipal();
		return Jwts
				.builder()
				.setSubject(userPrincipal.getUsername())
				.setIssuedAt(new Date())
				.setExpiration(
						new Date((new Date()).getTime() + jwtExpiration * 1000))
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			// logger.error("Invalid JWT signature -> Message: {} ", e);
		} catch (MalformedJwtException e) {
			// logger.error("Invalid JWT token -> Message: {}", e);
		} catch (ExpiredJwtException e) {
			// logger.error("Expired JWT token -> Message: {}", e);
		} catch (UnsupportedJwtException e) {
			// logger.error("Unsupported JWT token -> Message: {}", e);
		} catch (IllegalArgumentException e) {
			// logger.error("JWT claims string is empty -> Message: {}", e);
		}

		return false;
	}

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token)
				.getBody().getSubject();
	}
}