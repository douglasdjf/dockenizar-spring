package br.com.douglasdjf21.integrationtests.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UsuarioDTO{
	
	@JsonProperty("id")
    private Long id;
	
	private String nome;
	
	private Integer idade;
	
	private Boolean ativo;
	
	public UsuarioDTO() {
	}
	
	public UsuarioDTO(Long id, String nome, Integer idade) {
		super();
		this.id = id;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}



}
