package com.jaypi4c.demo.backend.controller;

import com.jaypi4c.demo.backend.api.BooksApiDelegate;
import com.jaypi4c.demo.backend.config.RabbitConfig;
import com.jaypi4c.demo.backend.dto.BookDTODto;
import com.jaypi4c.demo.backend.entitiy.Book;
import com.jaypi4c.demo.backend.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BookController implements BooksApiDelegate {

    private final BookRepository repository;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public ResponseEntity<List<BookDTODto>> booksGet() {
        return ResponseEntity.ok(repository.findAll()
                .stream()
                .map(entity -> {
                    BookDTODto dto = new BookDTODto();
                    dto.setName(entity.getBookName());
                    dto.setAuthor(entity.getAuthor());
                    return dto;
                }).toList());
    }

    @Override
    public ResponseEntity<BookDTODto> booksPost(BookDTODto bookDTODto) {
        Book book = new Book();
        book.setBookName(bookDTODto.getName());
        book.setAuthor(bookDTODto.getAuthor());
        repository.save(book);

        rabbitTemplate.convertAndSend(RabbitConfig.JOBS_QUEUE, bookDTODto.getName());

        return ResponseEntity.status(HttpStatus.CREATED).body(bookDTODto);
    }
}
