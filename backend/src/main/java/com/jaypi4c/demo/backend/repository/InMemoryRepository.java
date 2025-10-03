package com.jaypi4c.demo.backend.repository;

import com.jaypi4c.demo.backend.dto.BookDTODto;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class InMemoryRepository {

    private final List<BookDTODto> books;

    public InMemoryRepository() {
        books = new ArrayList<>();
        BookDTODto book1 = new BookDTODto();
        book1.setAuthor("Stephen King");
        book1.setName("The Shining");
        books.add(book1);
        BookDTODto book2 = new BookDTODto();
        book2.setAuthor("J.K. Rowling");
        book2.setName("Harry Potter and the Philosopher's Stone");
        books.add(book2);
    }


    public List<BookDTODto> findAll() {
        return books;
    }

    public void save(BookDTODto book) {
        books.add(book);
    }

    public void delete(BookDTODto book) {
        books.remove(book);
    }

}
