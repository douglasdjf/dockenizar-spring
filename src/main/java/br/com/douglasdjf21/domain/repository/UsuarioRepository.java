package br.com.douglasdjf21.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.douglasdjf21.domain.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {


	@Modifying
	@Query("UPDATE Usuario u SET u.ativo = false WHERE u.id =:id")
	void inativaUsuario(@Param("id") Long id);
	
	
	@Query("SELECT u FROM Usuario u WHERE u.nome LIKE LOWER(CONCAT ('%',:nome,'%'))")
	Page<Usuario> findUsuariosPorNomes(@Param("nome") String nome, Pageable pageable);
}
