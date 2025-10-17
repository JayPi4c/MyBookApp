package com.jaypi4c.demo.backend.controller;

import com.jaypi4c.demo.backend.api.BooksApiDelegate;
import com.jaypi4c.demo.backend.config.RabbitConfig;
import com.jaypi4c.demo.backend.dto.BookDto;
import com.jaypi4c.demo.backend.entitiy.Book;
import com.jaypi4c.demo.backend.repository.BookRepository;
import com.jaypi4c.demo.worker.dto.Worker;
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
    public ResponseEntity<List<BookDto>> booksGet() {
        return ResponseEntity.ok(repository.findAll()
                .stream()
                .map(entity -> {
                    BookDto dto = new BookDto();
                    dto.setName(entity.getBookName());
                    dto.setAuthor(entity.getAuthor());
                    return dto;
                }).toList());
    }

    @Override
    public ResponseEntity<BookDto> booksPost(BookDto bookDto) {
        Book book = new Book();
        book.setBookName(bookDto.getName());
        book.setAuthor(bookDto.getAuthor());
        repository.save(book);
        Worker.Request request = Worker.Request.newBuilder().setBookname(bookDto.getName()).build();
        rabbitTemplate.convertAndSend(RabbitConfig.JOBS_QUEUE, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookDto);
    }
}
