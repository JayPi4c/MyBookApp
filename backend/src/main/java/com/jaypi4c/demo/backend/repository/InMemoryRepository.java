package com.jaypi4c.demo.backend.repository;

import com.jaypi4c.demo.backend.entitiy.Book;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.function.Function;

@Repository
public class InMemoryRepository implements JpaRepository<Book, UUID> {

    private final Map<UUID, Book> books;

    public InMemoryRepository() {
        books = new HashMap<>();
        Book book = new Book();
        book.setAuthor("Stephen King");
        book.setBookName("The Shining");
        UUID uuid = UUID.randomUUID();
        book.setId(uuid);
        books.put(uuid, book);


        Book book2 = new Book();
        book2.setAuthor("J.K. Rowling");
        book2.setBookName("Harry Potter and the Philosopher's Stone");
        UUID uuid2 = UUID.randomUUID();
        book2.setId(uuid2);
        books.put(uuid2, book2);
    }


    @Override
    public <S extends Book> S save(S entity) {
        UUID uuid = UUID.randomUUID();
        entity.setId(uuid);
        books.put(uuid, entity);
        return entity;
    }

    @Override
    public <S extends Book> List<S> saveAll(Iterable<S> entities) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(books.values());
    }

    @Override
    public Optional<Book> findById(UUID uuid) {
        return Optional.ofNullable(books.get(uuid));
    }

    @Override
    public boolean existsById(UUID uuid) {
        return books.containsKey(uuid);
    }

    @Override
    public List<Book> findAllById(Iterable<UUID> uuids) {
        return books.entrySet().stream().filter(entry -> {
            for (UUID uuid : uuids) {
                if (entry.getKey().equals(uuid)) {
                    return true;
                }
            }
            return false;
        }).map(Map.Entry::getValue).toList();
    }

    @Override
    public long count() {
        return books.size();
    }

    @Override
    public void deleteById(UUID uuid) {
        books.remove(uuid);
    }

    @Override
    public void delete(Book entity) {
        books.remove(entity.getId());
    }

    @Override
    public void deleteAllById(Iterable<? extends UUID> uuids) {
        for (UUID uuid : uuids) {
            books.remove(uuid);
        }
    }

    @Override
    public void deleteAll(Iterable<? extends Book> entities) {
        for (Book book : entities) {
            books.remove(book.getId());
        }
    }

    @Override
    public void deleteAll() {
        books.clear();
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Book> S saveAndFlush(S entity) {
        return save(entity);
    }

    @Override
    public <S extends Book> List<S> saveAllAndFlush(Iterable<S> entities) {
        return saveAll(entities);
    }

    @Override
    public void deleteAllInBatch(Iterable<Book> entities) {
        deleteAll(entities);
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<UUID> uuids) {
        deleteAllById(uuids);
    }

    @Override
    public void deleteAllInBatch() {
        deleteAll();
    }

    @Override
    public Book getOne(UUID uuid) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Book getById(UUID uuid) {
        return books.get(uuid);
    }

    @Override
    public Book getReferenceById(UUID uuid) {
        return books.get(uuid);
    }

    @Override
    public <S extends Book> Optional<S> findOne(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Book> List<S> findAll(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Book> List<S> findAll(Example<S> example, Sort sort) {
        throw new UnsupportedOperationException();

    }

    @Override
    public <S extends Book> Page<S> findAll(Example<S> example, Pageable pageable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Book> long count(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Book> boolean exists(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Book, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Book> findAll(Sort sort) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Page<Book> findAll(Pageable pageable) {
        throw new UnsupportedOperationException();
    }
}
