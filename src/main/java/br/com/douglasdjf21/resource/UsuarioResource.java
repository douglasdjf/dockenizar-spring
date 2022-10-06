package br.com.douglasdjf21.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.douglasdjf21.domain.service.UsuarioService;
import br.com.douglasdjf21.dto.UsuarioDTO;
import br.com.douglasdjf21.util.MediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuarios", description = "EndPoints para usuários")
public class UsuarioResource {
	
	@Autowired
	private UsuarioService service;
	
	@GetMapping(produces = {MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML,MediaType.APPLICATION_YAML},
			    consumes = {MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML,MediaType.APPLICATION_YAML})
	@Operation(summary = "Find All Usuários", description = "Find All Usuários",
				tags = {"Usuarios"},
				responses = {
						@ApiResponse(description = "Success", responseCode = "200",
								content = {
										@Content(
												mediaType = "application/json",
												array = @ArraySchema(schema = @Schema(implementation = UsuarioDTO.class))
											)
								}),
						@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
						@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
						@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
						@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
				})
	public ResponseEntity<Page<UsuarioDTO>> findAll(@RequestParam(value = "page" , defaultValue = "0") int page,
												    @RequestParam(value = "limit" , defaultValue = "12") int limit,
												    @RequestParam(value = "direction" , defaultValue = "asc") String  direction,
												    @RequestParam(value = "orderBy" , defaultValue = "nome") String orderBy,
												    @RequestBody(required = false) UsuarioDTO filtro){
		
		var sortDirection = "desc".equalsIgnoreCase(direction)? Sort.Direction.DESC : Sort.Direction.ASC;
		Pageable pageable = PageRequest.of(page,limit,Sort.by(sortDirection,orderBy));
		Page<UsuarioDTO> usuarioDTOPage = this.service.findAllByExampleAndPage(filtro,pageable);
		
		return ResponseEntity.ok(usuarioDTOPage);
	}
	
	
	@GetMapping(value = "/hateoas",
			    produces = {MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML,MediaType.APPLICATION_YAML},
		        consumes = {MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML,MediaType.APPLICATION_YAML})
	@Operation(summary = "Find All Usuários Pageble Hateoas", description = "Find All Usuários  Pageble Hateoas",
			tags = {"Usuarios"},
			responses = {
					@ApiResponse(description = "Success", responseCode = "200",
							content = {
									@Content(
											mediaType = "application/json",
											array = @ArraySchema(schema = @Schema(implementation = UsuarioDTO.class))
										)
							}),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
			})
	public ResponseEntity<PagedModel<EntityModel<UsuarioDTO>>> findAllHateosPageble(@RequestParam(value = "page" , defaultValue = "0") int page,
																				    @RequestParam(value = "limit" , defaultValue = "12") int limit,
																				    @RequestParam(value = "direction" , defaultValue = "asc") String  direction,
																				    @RequestParam(value = "orderBy" , defaultValue = "nome") String orderBy,
																				    @RequestBody(required = false) UsuarioDTO filtro){
	
			var sortDirection = "desc".equalsIgnoreCase(direction)? Sort.Direction.DESC : Sort.Direction.ASC;
			Pageable pageable = PageRequest.of(page,limit,Sort.by(sortDirection,orderBy));
			
	   return ResponseEntity.ok(this.service.findAllByExampleAndPageHateoas(filtro,pageable));
	}
	
	
	
	@GetMapping(value = "/nome/{nome}",
			    produces = {MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML,MediaType.APPLICATION_YAML},
		        consumes = {MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML,MediaType.APPLICATION_YAML})
	@Operation(summary = "Find Name Usuários Pageble Hateoas", description = "Find Name Usuários  Pageble Hateoas",
			tags = {"Usuarios"},
			responses = {
					@ApiResponse(description = "Success", responseCode = "200",
							content = {
									@Content(
											mediaType = "application/json",
											array = @ArraySchema(schema = @Schema(implementation = UsuarioDTO.class))
										)
							}),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
			})
	public ResponseEntity<PagedModel<EntityModel<UsuarioDTO>>> findNameHateosPageble(@RequestParam(value = "page" , defaultValue = "0") int page,
																				     @RequestParam(value = "limit" , defaultValue = "12") int limit,
																				     @RequestParam(value = "direction" , defaultValue = "asc") String  direction,
																				     @RequestParam(value = "orderBy" , defaultValue = "nome") String orderBy,
																				     @PathVariable("nome") String nome){
	
			var sortDirection = "desc".equalsIgnoreCase(direction)? Sort.Direction.DESC : Sort.Direction.ASC;
			Pageable pageable = PageRequest.of(page,limit,Sort.by(sortDirection,orderBy));
			
	   return ResponseEntity.ok(this.service.findAllByExampleAndByNameHateoas(nome,pageable));
	}
	
	
	
	
	@GetMapping(value = "/{id}",
			produces = {MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML,MediaType.APPLICATION_YAML},
		    consumes = {MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML,MediaType.APPLICATION_YAML}
			)
	@Operation(summary = "Finds a Usuários", description = "Finds a Usuários",
	tags = {"Usuarios"},
	responses = {
			@ApiResponse(description = "Success", responseCode = "200",
					content = 
							@Content(schema = @Schema(implementation = UsuarioDTO.class))			
			),
			@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
	})
	public ResponseEntity<UsuarioDTO> obterPorId(@PathVariable("id") Long id) {
		return ResponseEntity.ok(service.obterPorId(id));
	}
	
	
	
	
	@PostMapping(produces = {MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML,MediaType.APPLICATION_YAML},
			    consumes = {MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML,MediaType.APPLICATION_YAML})
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Created a Usuários", description = "Created a Usuários",
	tags = {"Usuarios"},
	responses = {
			@ApiResponse(description = "Created", responseCode = "201",
					content = 
							@Content(schema = @Schema(implementation = UsuarioDTO.class))			
			),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
	})
	public ResponseEntity<UsuarioDTO> salvar(@RequestBody UsuarioDTO usuarioDTO) {
		return ResponseEntity.ok(service.salvar(usuarioDTO));
	}
	
	
	
	
	
	@PutMapping(value = "/{id}",
			produces = {MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML,MediaType.APPLICATION_YAML},
		    consumes = {MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML,MediaType.APPLICATION_YAML})
	@Operation(summary = "Updates a Usuários", description = "Updates a Usuários",
	tags = {"Usuarios"},
	responses = {
			@ApiResponse(description = "Success", responseCode = "200",
					content = 
							@Content(schema = @Schema(implementation = UsuarioDTO.class))			
			),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
	})
	public ResponseEntity<UsuarioDTO> atualizar(@PathVariable("id") Long id,@RequestBody UsuarioDTO usuarioDTO) {
		return ResponseEntity.ok(service.atualizar(id,usuarioDTO));
	}
	
	
	
	
	@DeleteMapping(value="/{id}",
			produces = {MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML,MediaType.APPLICATION_YAML})
	@Operation(summary = "Delete a Usuários", description = "Delete a Usuários",
	tags = {"Usuarios"},
	responses = {
			@ApiResponse(description = "No Content", responseCode = "204",
					content = @Content			
			),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
	})
	public ResponseEntity<UsuarioDTO> delete(@PathVariable("id") Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	
	
	
	
	@PatchMapping(value = "/{id}",
			produces = {MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML,MediaType.APPLICATION_YAML},
		    consumes = {MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML,MediaType.APPLICATION_YAML}
			)
	@Operation(summary = "Inativar  Usuário", description = "Inativar  Usuário",
	tags = {"Usuarios"},
	responses = {
			@ApiResponse(description = "Success", responseCode = "200",
					content = 
							@Content(schema = @Schema(implementation = UsuarioDTO.class))			
			),
			@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
	})
	public ResponseEntity<UsuarioDTO> inativarUsuario(@PathVariable("id") Long id) {
		return ResponseEntity.ok(service.inativarUsuario(id));
	}
	
}
