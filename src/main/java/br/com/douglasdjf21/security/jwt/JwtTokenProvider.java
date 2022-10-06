package br.com.douglasdjf21.security.jwt;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

import br.com.douglasdjf21.dto.security.TokenDTO;
import br.com.douglasdjf21.exception.InvalidoJwtException;
import br.com.douglasdjf21.exception.JwtExpiredException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class JwtTokenProvider {
	
	
	@Value("${security.jwt.token.secret-key:secret}")
	private String secretKey = "secret";
	
	@Value("${security.jwt.token.expire-lenght:3600000}")
	private long expireLenght = 3600000;
	
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	Algorithm algorithm = null;
	
	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
		algorithm = Algorithm.HMAC256(secretKey.getBytes());
	}
	
	public TokenDTO createAccesToken(String username, List<String> roles) {
		Date now = new Date();
		Date valid = new Date(now.getTime() + expireLenght);
		
		var acessToken = getAccessToken(username,roles,now,valid);
		var refreshToken = getRefreshToken(username,roles,now);
		
		return new TokenDTO(username,true,now,valid,acessToken,refreshToken);
	}
	
	public TokenDTO refreshToken(String refreshToken) {
		if(refreshToken.contains("Bearer ")) 
			refreshToken = refreshToken.substring("Bearer ".length());
		
		JWTVerifier jwtVerifier = JWT.require(algorithm).build();
		DecodedJWT decodedJWT = jwtVerifier.verify(refreshToken);
		String username = decodedJWT.getSubject();
		List<String> roles = decodedJWT.getClaim("roles").asList(String.class);
		return createAccesToken(username,roles);
	}


	private String getAccessToken(String username, List<String> roles, Date now, Date valid) {
		String issuerUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
		
		
		return JWT.create()
				.withClaim("roles", roles)
				.withIssuedAt(now)
				.withExpiresAt(valid)
				.withSubject(username)
				.withIssuer(issuerUrl)
				.sign(algorithm)
				.strip();
	}
	
	private String getRefreshToken(String username, List<String> roles, Date now) {
		Date validRefreshToken = new Date(now.getTime() + (expireLenght * 3));
		
		return JWT.create()
				.withClaim("roles", roles)
				.withIssuedAt(now)
				.withExpiresAt(validRefreshToken)
				.withSubject(username)
				.sign(algorithm)
				.strip();
	}
	
	public Authentication getAuthentication(String token) {
		DecodedJWT decodedJWT = decodeToken(token);
		UserDetails userDetails = this.userDetailsService.loadUserByUsername(decodedJWT.getSubject());
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	private DecodedJWT decodeToken(String token) {
		Algorithm alg = Algorithm.HMAC256(secretKey.getBytes());
		JWTVerifier jwtVerifier = JWT.require(alg).build();
		try {
			DecodedJWT decodedJWT = jwtVerifier.verify(token);
			return decodedJWT;
		} catch (TokenExpiredException e) {
			throw new JwtExpiredException(e.getMessage());
			
		}
	}
	
	public String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring("Bearer ".length());
		}
		
		return null;
		
	}
	
	
	public boolean validateToken(String token) {
		DecodedJWT decodedJWT = decodeToken(token);
		try {
			if(decodedJWT.getExpiresAt().before(new Date())) 
				return false;
			
			return true;
		}catch (Exception e) {
			throw new InvalidoJwtException("Jwt token expirado ou inválido");
		}
	}

}
