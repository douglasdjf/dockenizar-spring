package br.com.douglasdjf21.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import br.com.douglasdjf21.domain.repository.UserRepository;
import br.com.douglasdjf21.dto.security.AccountCredentialsDTO;
import br.com.douglasdjf21.dto.security.TokenDTO;
import br.com.douglasdjf21.security.jwt.JwtTokenProvider;

@Service
public class AuthService {
	
	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserRepository repository;
	
	
	@SuppressWarnings("rawtypes")
	public ResponseEntity login(AccountCredentialsDTO data) {
		
		if(data == null || !StringUtils.hasText(data.getUsername()) || !StringUtils.hasText(data.getPassword())) 
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Credenciais inválidas");
		
		try {
			var username = data.getUsername();
			var password = data.getPassword();
			
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
			
			var user = repository.findByUsername(username);
			var tokenResponse = new TokenDTO();
			
			if (user != null) {
				tokenResponse = tokenProvider.createAccesToken(username, user.getRoles());
			}else {
				throw new UsernameNotFoundException("Usuário " + username + " não encontrado!");
			}
			
			return ResponseEntity.ok(tokenResponse);
		} catch (Exception e) {
			throw new BadCredentialsException("Usuário/Senha inválidos");
		}
	}
	@SuppressWarnings("rawtypes")
	public ResponseEntity refreshToken(String username, String refreshToken) {
		
		if(username == null || !StringUtils.hasText(username) || refreshToken == null || !StringUtils.hasText(refreshToken)) 
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Credenciais inválidas");
		
		var user = repository.findByUsername(username);
		var tokenResponse = new TokenDTO();
		
		if (user != null) {
			tokenResponse = tokenProvider.refreshToken(refreshToken);
		}else {
			throw new UsernameNotFoundException("Usuário " + username + " não encontrado!");
		}
		
		return ResponseEntity.ok(tokenResponse);
	}

}
