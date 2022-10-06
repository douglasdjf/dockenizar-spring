package br.com.douglasdjf21.unittests.mockito.services;

import static org.junit.jupiter.api.Assertions.*;
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

import br.com.douglasdjf21.domain.model.Book;
import br.com.douglasdjf21.domain.model.Usuario;
import br.com.douglasdjf21.domain.repository.BookRepository;
import br.com.douglasdjf21.domain.service.BookService;
import br.com.douglasdjf21.dto.BookDTO;
import br.com.douglasdjf21.dto.UsuarioDTO;
import br.com.douglasdjf21.exception.RequiredObjectIsNullException;
import br.com.douglasdjf21.unittests.mockito.mock.MockBook;


@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class BookServicesTest {

	MockBook input;
	
	@InjectMocks
	private BookService service;
	
	@Mock
	BookRepository repository;
	
	@Mock
	private ModelMapper modelMapper;
	
	@BeforeEach
	void setUpMocks() throws Exception {
		input = new MockBook();
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testObterPorId() {
	    Book book = input.mockEntity();
		
		BookDTO bookDTO = input.mockDTO();
		
		when(repository.findById(1L)).thenReturn(Optional.of(book));
		when(modelMapper.map(book, BookDTO.class)).thenReturn(bookDTO);
	
		var result = service.obterPorId(1L);
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		
		assertTrue(result.toString().contains("links: [</api/v1/books/1>;rel=\"self\"]"));
		assertEquals("Some Author", result.getAuthor());
		assertEquals("Some Title", result.getTitle());
		assertEquals(25D, result.getPrice());
		assertNotNull(result.getLaunchDate());
	}
	
	@Test
	void testSalvar() {
		BookDTO bookDTO = input.mockDTO();
		bookDTO.setKey(null);
		
		Book bookAntes = input.mockEntity();
		bookAntes.setKey(null);
		
		Book bookSalvo = input.mockEntity();
		BookDTO bookDtoSalvo = input.mockDTO();
		
		when(modelMapper.map(bookDTO, Book.class)).thenReturn(bookAntes);
		when(repository.save(bookAntes)).thenReturn(bookSalvo);
		when(modelMapper.map(bookSalvo, BookDTO.class)).thenReturn(bookDtoSalvo);

		var result = service.salvar(bookDTO);
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		assertTrue(result.toString().contains("links: [</api/v1/books/1>;rel=\"self\"]"));
		assertEquals("Some Author", result.getAuthor());
		assertEquals("Some Title", result.getTitle());
		assertEquals(25D, result.getPrice());
		assertNotNull(result.getLaunchDate());
	}
	
	@Test
	void testCreateWithNullBook() {
		Exception exception = assertThrows(RequiredObjectIsNullException.class, ()->{
			service.atualizar(null, null);
		});
		
		String expectedMessage = "Não é permitido persistir um objeto null";
		String actualMessage = exception.getMessage();
		
		assertTrue(actualMessage.contains(expectedMessage));
	}


	@Test
	void testAtualizar() {
		Book bookAtual = input.mockEntity();
		
		Book bookNovo = input.mockEntity();
		bookNovo.setKey(null);
		bookNovo.setAuthor("NOVO Author");
		bookNovo.setPrice(30D);
		bookNovo.setTitle("Novo Titulo");

		Book bookSalvo = input.mockEntity();
		bookSalvo.setAuthor("NOVO Author");
		bookSalvo.setPrice(30D);
		bookSalvo.setTitle("Novo Titulo");

		BookDTO bookDTO = input.mockDTO();
		bookDTO.setKey(null);
		bookDTO.setAuthor("NOVO Author");
		bookDTO.setPrice(30D);
		bookDTO.setTitle("Novo Titulo");

		BookDTO bookDTOSalvo = input.mockDTO();
		bookDTOSalvo.setAuthor("NOVO Author");
		bookDTOSalvo.setPrice(30D);
		bookDTOSalvo.setTitle("Novo Titulo");

		when(repository.findById(1L)).thenReturn(Optional.of(bookAtual));
		when(modelMapper.map(bookDTO, Book.class)).thenReturn(bookNovo);
		when(repository.save(any())).thenReturn(bookSalvo);
		when(modelMapper.map(bookSalvo, BookDTO.class)).thenReturn(bookDTOSalvo);

		var result = service.atualizar(1L,bookDTO);
		
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		assertNotNull(result.getPrice());
		assertNotNull(result.getAuthor());
		
		assertTrue(result.toString().contains("links: [</api/v1/books/1>;rel=\"self\"]"));
		assertEquals("NOVO Author", result.getAuthor());
		assertEquals("Novo Titulo", result.getTitle());
		assertEquals(30D, result.getPrice());
		assertNotNull(result.getLaunchDate());
	}
	

	
	@Test
	void testUpdateWithNullBook() {
		Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
			service.atualizar(null,null);
		});
		
		String expectedMessage = "Não é permitido persistir um objeto null";
		String actualMessage = exception.getMessage();
		
		assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	void testDelete() {
		Book entity = input.mockEntity();
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		service.delete(1L);
	}
	
	@Test
	void testFindAll() {
		List<Book> books = input.mockEntityList();

		when(repository.findAll()).thenReturn(books);

		var list = service.findAll();

		assertNotNull(list);
		assertEquals(5, list.size());
	}

}
