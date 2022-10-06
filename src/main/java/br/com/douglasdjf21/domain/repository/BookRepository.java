package br.com.douglasdjf21.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.douglasdjf21.domain.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {}