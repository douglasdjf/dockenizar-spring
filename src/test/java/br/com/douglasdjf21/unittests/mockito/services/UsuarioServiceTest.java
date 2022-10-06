package br.com.douglasdjf21.unittests.mockito.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import br.com.douglasdjf21.domain.model.Usuario;
import br.com.douglasdjf21.domain.repository.UsuarioRepository;
import br.com.douglasdjf21.domain.service.UsuarioService;
import br.com.douglasdjf21.dto.UsuarioDTO;
import br.com.douglasdjf21.exception.RequiredObjectIsNullException;
import br.com.douglasdjf21.unittests.mockito.mock.MockUsuario;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

	MockUsuario input;
	
	@InjectMocks
	private UsuarioService service;
	
	@Mock
	private UsuarioRepository repository;
	
	@Mock
	private ModelMapper modelMapper;
	
	
	@BeforeEach
	void setUp() throws Exception {
		input = new MockUsuario();
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	void testSalvar() {
		
		UsuarioDTO usuarioDto = input.mockDTO();
		usuarioDto.setKey(null);
		
		Usuario usuarioAntes = input.mockEntity();
		usuarioAntes.setKey(null);
		
		Usuario usuarioSalvo = input.mockEntity();
		UsuarioDTO usuarioDtoSalvo = input.mockDTO();
		
		when(modelMapper.map(usuarioDto, Usuario.class)).thenReturn(usuarioAntes);
		when(repository.save(usuarioAntes)).thenReturn(usuarioSalvo);
		when(modelMapper.map(usuarioSalvo, UsuarioDTO.class)).thenReturn(usuarioDtoSalvo);

		var result = service.salvar(usuarioDto);
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		assertTrue(result.toString().contains("links: [</api/v1/usuarios/1>;rel=\"self\"]"));
		assertEquals(27, result.getIdade());
		assertEquals("Nome completo", result.getNome());
		
	}
	@Test
	void testSalvarComNullUsuario() {
		
		Exception exception = assertThrows(RequiredObjectIsNullException.class, ()->{
			service.salvar(null);
		});
				
		String mensagemEsperada = "Não é permitido persistir um objeto null";
		String mensagemAtual = exception.getMessage();
	
		assertTrue(mensagemAtual.contains(mensagemEsperada));

		
	}

	@Test
	void testObterPorId() {
		
		Usuario usuario = input.mockEntity();
		
		UsuarioDTO usuarioDto = input.mockDTO();
		
		when(repository.findById(1L)).thenReturn(Optional.of(usuario));
		when(modelMapper.map(usuario, UsuarioDTO.class)).thenReturn(usuarioDto);
	
		var result = service.obterPorId(1L);
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		assertTrue(result.toString().contains("links: [</api/v1/usuarios/1>;rel=\"self\"]"));
		assertEquals(27, result.getIdade());
		assertEquals("Nome completo", result.getNome());
		
		
	}

	@Test
	void testDelete() {
		
		Usuario usuario = input.mockEntity();
		when(repository.findById(1L)).thenReturn(Optional.of(usuario));
		service.delete(1L);
		
	}

	@Test
	void testFindById() {
		
		Usuario usuario = input.mockEntity();
		when(repository.findById(1L)).thenReturn(Optional.of(usuario));
		var result =service.findById(1L);
		assertNotNull(result);
		assertNotNull(result.getIdade());
		assertNotNull(result.getKey());
		assertNotNull(result.getIdade());
		
	}

	@Test
	void testAtualizar() {
		
		Usuario userAtual = input.mockEntity();
		
		Usuario usuarioNovo = input.mockEntity();
		usuarioNovo.setKey(null);
		usuarioNovo.setNome("NOVO");
		usuarioNovo.setIdade(25);
		
		Usuario usuarioSalvo = input.mockEntity();
		usuarioSalvo.setNome("NOVO");
		usuarioSalvo.setIdade(25);
		
		UsuarioDTO usuarioDTO = input.mockDTO();
		usuarioDTO.setKey(null);
		usuarioDTO.setNome("NOVO");
		usuarioDTO.setIdade(25);
		
		UsuarioDTO usuarioDTOSalvo = input.mockDTO();
		usuarioDTOSalvo.setNome("NOVO");
		usuarioDTOSalvo.setIdade(25);
		
		when(repository.findById(1L)).thenReturn(Optional.of(userAtual));
		when(modelMapper.map(usuarioDTO, Usuario.class)).thenReturn(usuarioNovo);
		when(repository.save(any())).thenReturn(usuarioSalvo);
		when(modelMapper.map(usuarioSalvo, UsuarioDTO.class)).thenReturn(usuarioDTOSalvo);
		
		var result = service.atualizar(1L,usuarioDTO);
		assertNotNull(result);
		assertNotNull(result.getIdade());
		assertNotNull(result.getKey());
		assertNotNull(result.getIdade());
		assertNotNull(result.getLinks());
		assertTrue(result.toString().contains("links: [</api/v1/usuarios/1>;rel=\"self\"]"));
		assertTrue(result.getNome().equals("NOVO"));
		assertTrue(result.getIdade().equals(25));
	  		
	}
	
	@Test
	void testSalvarComNullAtualizar() {
		
		Exception exception = assertThrows(RequiredObjectIsNullException.class, ()->{
			service.atualizar(null, null);
		});
				
		String mensagemEsperada = "Não é permitido persistir um objeto null";
		String mensagemAtual = exception.getMessage();
	
		assertTrue(mensagemAtual.contains(mensagemEsperada));
		
	}
	
	@Test
	void testFindAll() {
		
		List<Usuario> usuarios = input.mockList();
		
		when(repository.findAll()).thenReturn(usuarios);
		
		var list = service.findAll();
		
		assertNotNull(list);
		assertEquals(2, list.size());
		 
	}

}
