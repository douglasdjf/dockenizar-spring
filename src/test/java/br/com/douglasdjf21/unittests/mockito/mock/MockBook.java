package br.com.douglasdjf21.unittests.mockito.mock;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.douglasdjf21.domain.model.Book;
import br.com.douglasdjf21.dto.BookDTO;

public class MockBook {

    public List<Book> mockEntityList() {
        List<Book> books = new ArrayList<Book>();
        for (int i = 0; i < 5; i++) {
            books.add(mockEntity());
        }
        return books;
    }

    public List<BookDTO> mockVOList() {
        List<BookDTO> books = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            books.add(mockDTO());
        }
        return books;
    }
    
    public Book mockEntity() {
        Book book = new Book();
        book.setKey(1L);
        book.setAuthor("Some Author");
        book.setLaunchDate(new Date());
        book.setPrice(25D);
        book.setTitle("Some Title");
        return book;
    }

    public BookDTO mockDTO() {
        BookDTO book = new BookDTO();
        book.setKey(1L);
        book.setAuthor("Some Author");
        book.setLaunchDate(new Date());
        book.setPrice(25D);
        book.setTitle("Some Title");
        return book;
    }

}
