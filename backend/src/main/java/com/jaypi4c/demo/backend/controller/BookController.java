package com.jaypi4c.demo.backend.controller;

import com.jaypi4c.demo.backend.api.BooksApiDelegate;
import com.jaypi4c.demo.backend.config.RabbitConfig;
import com.jaypi4c.demo.backend.dto.BookDTODto;
import com.jaypi4c.demo.backend.repository.InMemoryRepository;
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

    private final InMemoryRepository repository;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public ResponseEntity<List<BookDTODto>> booksGet() {
        return ResponseEntity.ok(repository.findAll());
    }

    @Override
    public ResponseEntity<BookDTODto> booksPost(BookDTODto bookDTODto) {
        repository.save(bookDTODto);

        rabbitTemplate.convertAndSend(RabbitConfig.JOBS_QUEUE, bookDTODto.getName());

        return ResponseEntity.status(HttpStatus.CREATED).body(bookDTODto);
    }
}
