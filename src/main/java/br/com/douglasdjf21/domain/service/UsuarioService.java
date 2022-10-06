package br.com.douglasdjf21.domain.service;

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
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import br.com.douglasdjf21.domain.model.Usuario;
import br.com.douglasdjf21.domain.repository.UsuarioRepository;
import br.com.douglasdjf21.dto.UsuarioDTO;
import br.com.douglasdjf21.exception.RequiredObjectIsNullException;
import br.com.douglasdjf21.resource.UsuarioResource;
import jakarta.transaction.Transactional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository repository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	
	@Autowired
	PagedResourcesAssembler<UsuarioDTO> assembler;
	
	
	public List<UsuarioDTO> findAllByExample(UsuarioDTO filtro){
		
		if(ObjectUtils.isEmpty(filtro))
			filtro = new UsuarioDTO();
		
		Usuario usuarioFiltro = modelMapper.map(filtro, Usuario.class);
		
		ExampleMatcher matcher = ExampleMatcher
								.matching()
								.withIgnoreCase()
								.withStringMatcher(ExampleMatcher.StringMatcher.STARTING);
		
		Example<Usuario> example = Example.of(usuarioFiltro,matcher);
			
		return repository.findAll(example).stream().map(u -> modelMapper.map(u, UsuarioDTO.class)).collect(Collectors.toList());
		
	}



	public Page<UsuarioDTO> findAllByPage(Pageable pageable) {
		var page = repository.findAll(pageable);
		return page.map(usuario -> modelMapper.map(usuario, UsuarioDTO.class));
	}
	
	public Page<UsuarioDTO> findAllByExampleAndPage(UsuarioDTO filtro, Pageable pageable) {
		
		if(ObjectUtils.isEmpty(filtro))
			filtro = new UsuarioDTO();
		
		Usuario usuarioFiltro = modelMapper.map(filtro, Usuario.class);
		
		ExampleMatcher matcher = ExampleMatcher
								.matching()
								.withIgnoreCase()
								.withStringMatcher(ExampleMatcher.StringMatcher.STARTING);
		
		Example<Usuario> example = Example.of(usuarioFiltro,matcher);
		
		Page<Usuario> pageUsuario = repository.findAll(example,pageable);
		
		Page<UsuarioDTO> pageUsuarioDTO = pageUsuario.map(usuario ->{
						  				         UsuarioDTO usuarioDTo = modelMapper.map(usuario, UsuarioDTO.class);
						  				         return usuarioDTo.add(linkTo(methodOn(UsuarioResource.class).obterPorId(usuarioDTo.getKey())).withSelfRel());
											 }) ;
		
	
		return pageUsuarioDTO;
	
	}
	
	public PagedModel<EntityModel<UsuarioDTO>>findAllByExampleAndPageHateoas(UsuarioDTO filtro, Pageable pageable) {
		
		if(ObjectUtils.isEmpty(filtro))
			filtro = new UsuarioDTO();
		
		Usuario usuarioFiltro = modelMapper.map(filtro, Usuario.class);
		
		ExampleMatcher matcher = ExampleMatcher
								.matching()
								.withIgnoreCase()
								.withStringMatcher(ExampleMatcher.StringMatcher.STARTING);
		
		Example<Usuario> example = Example.of(usuarioFiltro,matcher);
		
		Page<Usuario> pageUsuario = repository.findAll(example,pageable);
		
		Page<UsuarioDTO> pageUsuarioDTO = pageUsuario.map(usuario ->{
						  				         UsuarioDTO usuarioDTo = modelMapper.map(usuario, UsuarioDTO.class);
						  				         return usuarioDTo.add(linkTo(methodOn(UsuarioResource.class).obterPorId(usuarioDTo.getKey())).withSelfRel());
											 }) ;
		
		String orderBy = obterOrderSortFirst(pageable);
	
		Link link = linkTo(methodOn(UsuarioResource.class).findAllHateosPageble(pageable.getPageNumber(),
																				pageable.getPageSize(), 
																				"asc",orderBy, filtro)
																				).withSelfRel();
		
		return assembler.toModel(pageUsuarioDTO,link );
	
	}
	
	public PagedModel<EntityModel<UsuarioDTO>>findAllByExampleAndByNameHateoas(String nome, Pageable pageable) {

		
		Page<Usuario> pageUsuario = repository.findUsuariosPorNomes(nome,pageable);
		
		Page<UsuarioDTO> pageUsuarioDTO = pageUsuario.map(usuario ->{
						  				         UsuarioDTO usuarioDTo = modelMapper.map(usuario, UsuarioDTO.class);
						  				         return usuarioDTo.add(linkTo(methodOn(UsuarioResource.class).obterPorId(usuarioDTo.getKey())).withSelfRel());
											 }) ;
		
		String orderBy = obterOrderSortFirst(pageable);
	
		Link link = linkTo(methodOn(UsuarioResource.class).findNameHateosPageble(pageable.getPageNumber(),
																				pageable.getPageSize(), 
																				"asc", orderBy, nome)
																				).withSelfRel();
		
		return assembler.toModel(pageUsuarioDTO,link);
	
	}
	
	public UsuarioDTO salvar(UsuarioDTO usuarioDTO) {
		
		if(ObjectUtils.isEmpty(usuarioDTO)) 
			throw new RequiredObjectIsNullException();
		
		Usuario usuario = modelMapper.map(usuarioDTO, Usuario.class);
		Usuario usuariosalvo =repository.save(usuario);
		
		UsuarioDTO usuarioDto = modelMapper.map(usuariosalvo, UsuarioDTO.class);
		usuarioDto.add(linkTo(methodOn(UsuarioResource.class).obterPorId(usuarioDto.getKey())).withSelfRel());
		return usuarioDto;
	}
	
	public UsuarioDTO obterPorId(Long key) {
		Usuario usuario = findById(key);
		UsuarioDTO usuarioDto = modelMapper.map(usuario, UsuarioDTO.class);
		usuarioDto.add(linkTo(methodOn(UsuarioResource.class).obterPorId(key)).withSelfRel());
		return usuarioDto;
		
	}
	
	public void delete(Long key) {
		findById(key);
		repository.deleteById(key);		
	}
	
	public Usuario findById(Long key) {
		Optional<Usuario> optional = repository.findById(key);
		return optional.orElseThrow(()-> new RuntimeException("id inválido"));
	}



	public UsuarioDTO atualizar(Long id, UsuarioDTO usuarioDTO) {
		
		if(ObjectUtils.isEmpty(usuarioDTO) || ObjectUtils.isEmpty(id)) 
			throw new RequiredObjectIsNullException();
		
		Usuario userAtual = findById(id);
		Usuario usuarioNovo = modelMapper.map(usuarioDTO, Usuario.class);
		
	
		BeanUtils.copyProperties(usuarioNovo, userAtual,"key");
		
		Usuario usuarioAlterado = repository.save(userAtual);
			
		UsuarioDTO usuarioNovoDto = modelMapper.map(usuarioAlterado, UsuarioDTO.class);
		usuarioNovoDto.add(linkTo(methodOn(UsuarioResource.class).obterPorId(usuarioNovoDto.getKey())).withSelfRel());
		return usuarioNovoDto;
	
	}
	
	@Transactional
	public UsuarioDTO inativarUsuario(Long key) {
		repository.inativaUsuario(key);
		return obterPorId(key);
	}
	
	public List<Usuario> findAll(){
		return repository.findAll();
	}
	
	private String obterOrderSortFirst( Pageable pageable) {
		if(pageable.isPaged() && pageable.getSort() != null) {
			String[] orders =pageable.getSort().get().toArray().toString().split(":");
			if(orders.length > 0) {
				return orders[0].replace("[", "").replace("]", "");
			}
			
		}
		
		
		return "";
	}
	

	

}
