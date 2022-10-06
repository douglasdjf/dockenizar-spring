package br.com.douglasdjf21.integrationtests.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.douglasdjf21.domain.model.Usuario;
import br.com.douglasdjf21.domain.repository.UsuarioRepository;
import br.com.douglasdjf21.integrationtests.testcontainers.AbstractIntegretationTest;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(OrderAnnotation.class)
public class UsuarioRepositoryTest extends AbstractIntegretationTest {
	
	
	@Autowired
	UsuarioRepository repository;
	
	private static Usuario usuario;
	
	@BeforeAll
	public static void setup() {
		usuario = new Usuario();
	}
	
	@Test
	@Order(1)
	public void testFindByName() {
		Pageable pageable = PageRequest.of(0, 12, Sort.by(Direction.ASC,"nome"));
		usuario = repository.findUsuariosPorNomes("doug", pageable).getContent().get(0);
		
		
		Assertions.assertNotNull(usuario);
		Assertions.assertNotNull(usuario.getIdade());
		Assertions.assertNotNull(usuario.getNome());
		Assertions.assertNotNull(usuario.getKey());
		Assertions.assertTrue("Douglas Fonseca".contains(usuario.getNome()));
	}
	
	@Test
	@Order(2)
	public void disablePerson() {
		
		repository.inativaUsuario(usuario.getKey());
		
		Pageable pageable = PageRequest.of(0, 12, Sort.by(Direction.ASC,"nome"));
		usuario = repository.findUsuariosPorNomes("doug", pageable).getContent().get(0);
		
		
		Assertions.assertNotNull(usuario);
		Assertions.assertNotNull(usuario.getIdade());
		Assertions.assertNotNull(usuario.getNome());
		Assertions.assertNotNull(usuario.getKey());
		Assertions.assertTrue("Douglas Fonseca".contains(usuario.getNome()));
		Assertions.assertFalse(usuario.getAtivo());
	}

}
