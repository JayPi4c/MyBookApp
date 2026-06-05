package de.jaypi4c.demo.backend.controller;

import de.jaypi4c.demo.backend.api.BooksApiDelegate;
import de.jaypi4c.demo.backend.config.RabbitConfig;
import de.jaypi4c.demo.backend.dto.BookDto;
import de.jaypi4c.demo.backend.dto.BookTaskDto;
import de.jaypi4c.demo.backend.entitiy.Book;
import de.jaypi4c.demo.backend.registry.SseEmitterRegistry;
import de.jaypi4c.demo.backend.repository.BookRepository;
import de.jaypi4c.demo.worker.dto.Worker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookController implements BooksApiDelegate {

    private final BookRepository repository;
    private final RabbitTemplate rabbitTemplate;
    private final SseEmitterRegistry sseEmitterRegistry;

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
    public ResponseEntity<BookTaskDto> booksPost(BookDto bookDto) {
        UUID jobId = sseEmitterRegistry.register();

        Book book = new Book();
        book.setBookName(bookDto.getName());
        book.setAuthor(bookDto.getAuthor());
        repository.save(book);

        BookTaskDto bookTaskDto = new BookTaskDto();
        bookTaskDto.book(bookDto).jobId(jobId);

        Worker.Request request = Worker.Request
                .newBuilder()
                .setBookname(bookDto.getName())
                .setJobId(jobId.toString())
                .build();
        rabbitTemplate.convertAndSend(RabbitConfig.JOBS_QUEUE, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookTaskDto);
    }
}
