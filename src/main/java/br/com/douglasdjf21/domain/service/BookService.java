package br.com.douglasdjf21.domain.service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import br.com.douglasdjf21.domain.model.Book;
import br.com.douglasdjf21.domain.repository.BookRepository;
import br.com.douglasdjf21.dto.BookDTO;
import br.com.douglasdjf21.exception.RequiredObjectIsNullException;
import br.com.douglasdjf21.resource.BookResource;


@Service
public class BookService {

	@Autowired
	private BookRepository repository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	
	public List<BookDTO> findAllByExample(BookDTO filtro){
		
		if(ObjectUtils.isEmpty(filtro))
			filtro = new BookDTO();
		
		Book BookFiltro = modelMapper.map(filtro, Book.class);
		
		ExampleMatcher matcher = ExampleMatcher
								.matching()
								.withIgnoreCase()
								.withStringMatcher(ExampleMatcher.StringMatcher.STARTING);
		
		Example<Book> example = Example.of(BookFiltro,matcher);
			
		return repository.findAll(example).stream().map(u -> modelMapper.map(u, BookDTO.class)).collect(Collectors.toList());
		
	}



	public Page<BookDTO> findAllByPage(Pageable pageable) {
		var page = repository.findAll(pageable);
		return page.map(Book -> modelMapper.map(Book, BookDTO.class));
	}
	
	public Page<BookDTO> findAllByExampleAndPage(BookDTO filtro, Pageable pageable) {
		
		if(ObjectUtils.isEmpty(filtro))
			filtro = new BookDTO();
		
		Book BookFiltro = modelMapper.map(filtro, Book.class);
		
		ExampleMatcher matcher = ExampleMatcher
								.matching()
								.withIgnoreCase()
								.withStringMatcher(ExampleMatcher.StringMatcher.STARTING);
		
		Example<Book> example = Example.of(BookFiltro,matcher);
		
		Page<Book> pageBook = repository.findAll(example,pageable);
		
		Page<BookDTO> pageBookDTO = pageBook.map(Book ->{
						  				         BookDTO BookDTo = modelMapper.map(Book, BookDTO.class);
						  				         return BookDTo.add(linkTo(methodOn(BookResource.class).obterPorId(BookDTo.getKey())).withSelfRel());
											 }) ;
		
	
		return pageBookDTO;
	
	}
	
	public BookDTO salvar(BookDTO BookDTO) {
		
		if(ObjectUtils.isEmpty(BookDTO)) 
			throw new RequiredObjectIsNullException();
		
		Book Book = modelMapper.map(BookDTO, Book.class);
		Book Booksalvo =repository.save(Book);
		
		BookDTO BookDto = modelMapper.map(Booksalvo, BookDTO.class);
		BookDto.add(linkTo(methodOn(BookResource.class).obterPorId(BookDto.getKey())).withSelfRel());
		return BookDto;
	}
	
	public BookDTO obterPorId(Long key) {
		Book Book = findById(key);
		BookDTO BookDto = modelMapper.map(Book, BookDTO.class);
		BookDto.add(linkTo(methodOn(BookResource.class).obterPorId(key)).withSelfRel());
		return BookDto;
		
	}
	
	public void delete(Long key) {
		findById(key);
		repository.deleteById(key);		
	}
	
	public Book findById(Long key) {
		Optional<Book> optional = repository.findById(key);
		return optional.orElseThrow(()-> new RuntimeException("id inválido"));
	}



	public BookDTO atualizar(Long id, BookDTO bookDTO) {
		
		if(ObjectUtils.isEmpty(bookDTO) || ObjectUtils.isEmpty(id)) 
			throw new RequiredObjectIsNullException();
		
		Book bookAtual = findById(id);
		Book bookNovo = modelMapper.map(bookDTO, Book.class);
		
	
		BeanUtils.copyProperties(bookNovo, bookAtual,"id");
		
		Book bookAlterado = repository.save(bookAtual);
			
		BookDTO bookNovoDto = modelMapper.map(bookAlterado, BookDTO.class);
		bookNovoDto.add(linkTo(methodOn(BookResource.class).obterPorId(bookNovoDto.getKey())).withSelfRel());
		return bookNovoDto;
	
	}
	
	public List<Book> findAll(){
		return repository.findAll();
	}
}
