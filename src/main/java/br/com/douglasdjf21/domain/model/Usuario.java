package br.com.douglasdjf21.domain.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@JsonInclude(Include.NON_NULL)
@Entity
@Table(name = "tb_usuario")
public class Usuario implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long key;
	
	private String nome;
	
	private Integer idade;
	
	@Column(nullable = false)
	private Boolean ativo;
	
	public Usuario() {
	}
	
	@PrePersist
	public void ativarUsuario() {
		this.ativo = true;
	}
	
	public Usuario(Long key, String nome, Integer idade) {
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
