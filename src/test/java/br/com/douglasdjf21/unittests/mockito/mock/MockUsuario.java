package br.com.douglasdjf21.unittests.mockito.mock;

import java.util.ArrayList;
import java.util.List;

import br.com.douglasdjf21.domain.model.Usuario;
import br.com.douglasdjf21.dto.UsuarioDTO;

public class MockUsuario {
	
	
	public  Usuario mockEntity() {
		Usuario usuario = new Usuario();
		usuario.setIdade(27);
		usuario.setKey(1L);
		usuario.setNome("Nome completo");
		return usuario;
	}
	
	public UsuarioDTO mockDTO() {
		UsuarioDTO usuario = new UsuarioDTO();
		usuario.setIdade(27);
		usuario.setKey(1L);
		usuario.setNome("Nome completo");
		return usuario;
	}
	
	public List<Usuario> mockList(){
		List usuarios = new ArrayList<>();
		Usuario usuario = new Usuario(1L,"Douglas",27);
		Usuario usuario2 = new Usuario(1L,"Patrick",27);
		usuarios.add(usuario);
		usuarios.add(usuario2);
		
		return usuarios;
		
	}

}
