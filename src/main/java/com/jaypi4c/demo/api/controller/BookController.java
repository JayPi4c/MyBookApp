package com.jaypi4c.demo.api.controller;

import com.jaypi4c.demo.api.BooksApiDelegate;
import com.jaypi4c.demo.dto.BookDTODto;
import com.jaypi4c.demo.repository.InMemoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BookController implements BooksApiDelegate {

    private final InMemoryRepository repository;

    @Override
    public ResponseEntity<List<BookDTODto>> booksGet() {
        return ResponseEntity.ok(repository.findAll());
    }

    @Override
    public ResponseEntity<BookDTODto> booksPost(BookDTODto bookDTODto) {
        repository.save(bookDTODto);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookDTODto);
    }
}
