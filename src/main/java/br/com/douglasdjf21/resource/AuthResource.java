package br.com.douglasdjf21.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.douglasdjf21.domain.service.AuthService;
import br.com.douglasdjf21.dto.security.AccountCredentialsDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação")
public class AuthResource {
	
	@Autowired
	private AuthService authService;
	
	@SuppressWarnings("rawtypes")
	@Operation(summary = "Autenticação de usuarios")
	@PostMapping("/login")
	public ResponseEntity login(@RequestBody AccountCredentialsDTO data) {		
		var token = authService.login(data);
		if(token == null)
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Credenciais inválidas");
		
		return token;
	}
	
	@SuppressWarnings("rawtypes")
	@Operation(summary = "Refresh Token Autenticação de usuarios")
	@PutMapping("/refresh/{username}")
	public ResponseEntity refreshToken(@PathVariable("username") String username, @RequestHeader("Authorization") String refreshToken) {
		var token = authService.refreshToken(username,refreshToken);
		if(token == null)
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Credenciais inválidas");
		
		return token;
	}

}
