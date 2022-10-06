package  br.com.douglasdjf21.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.douglasdjf21.domain.service.BookService;
import br.com.douglasdjf21.dto.BookDTO;
import br.com.douglasdjf21.util.MediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/books")
@Tag(name = "Book", description = "Endpoints for Managing Book")
public class BookResource {
	
	@Autowired
	private BookService service;
	
	@GetMapping(
		produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML })
	@Operation(summary = "Finds all Book", description = "Finds all Book",
		tags = {"Book"},
		responses = {
			@ApiResponse(description = "Success", responseCode = "200",
				content = {
					@Content(
						mediaType = "application/json",
						array = @ArraySchema(schema = @Schema(implementation = BookDTO.class))
					)
				}),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
		}
	)
	public ResponseEntity<Page<BookDTO>> findAll(@RequestParam(value = "page" , defaultValue = "0") int page,
		    @RequestParam(value = "limit" , defaultValue = "5") int limit,
		    @RequestParam(value = "direction" , defaultValue = "asc") String  direction,
		    @RequestParam(value = "orderBy" , defaultValue = "title") String orderBy,
		    @RequestBody(required = false) BookDTO filtro){

		var sortDirection = "desc".equalsIgnoreCase(direction)? Sort.Direction.DESC : Sort.Direction.ASC;
		Pageable pageable = PageRequest.of(page,limit,Sort.by(sortDirection,orderBy));
		Page<BookDTO> bookDTOPage = this.service.findAllByExampleAndPage(filtro,pageable);
		
		return ResponseEntity.ok(bookDTOPage);
     }
	
	
	
	
	@GetMapping(value = "/{id}",
		produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML  })
	@Operation(summary = "Finds a Book", description = "Finds a Book",
		tags = {"Book"},
		responses = {
			@ApiResponse(description = "Success", responseCode = "200",
				content = @Content(schema = @Schema(implementation = BookDTO.class))
			),
			@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
		}
	)
	public BookDTO obterPorId(@PathVariable(value = "id") Long id) {
		return service.obterPorId(id);
	}
	
	
	
	
	
	@PostMapping(
		consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML  },
		produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML  })
	@Operation(summary = "Adds a new Book",
		description = "Adds a new Book by passing in a JSON, XML or YML representation of the book!",
		tags = {"Book"},
		responses = {
			@ApiResponse(description = "Success", responseCode = "200",
				content = @Content(schema = @Schema(implementation = BookDTO.class))
			),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
		}
	)
	public BookDTO create(@RequestBody BookDTO book) {
		return service.salvar(book);
	}
	
	
	
	
	@PutMapping(value = "/{id}",
		consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML  },
		produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML  })
	@Operation(summary = "Updates a Book",
		description = "Updates a Book by passing in a JSON, XML or YML representation of the book!",
		tags = {"Book"},
		responses = {
			@ApiResponse(description = "Updated", responseCode = "200",
				content = @Content(schema = @Schema(implementation = BookDTO.class))
			),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
		}
	)
	public BookDTO update(@PathVariable("id") Long id, @RequestBody BookDTO book) {
		return service.atualizar(id,book);
	}
	
	
	
	
	@DeleteMapping(value = "/{id}")
	@Operation(summary = "Deletes a Book",
		description = "Deletes a Book by passing in a JSON, XML or YML representation of the book!",
		tags = {"Book"},
		responses = {
			@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
		}
	)
	public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}