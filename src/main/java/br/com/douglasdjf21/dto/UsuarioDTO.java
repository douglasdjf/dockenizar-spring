package br.com.douglasdjf21.dto;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id","nome","idade"})
public class UsuarioDTO extends RepresentationModel<UsuarioDTO> {
	
	@JsonProperty("id")
    private Long key;
	
	private String nome;
	
	private Integer idade;
	
	private Boolean ativo;
	
	public UsuarioDTO() {
	}
	
	public UsuarioDTO(Long key, String nome, Integer idade) {
		super();
		this.key = key;
		this.nome = nome;
		this.idade = idade;
	}

	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Integer getIdade() {
		return idade;
	}
	public void setIdade(Integer idade) {
		this.idade = idade;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	

}
