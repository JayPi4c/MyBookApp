package de.jaypi4c.demo.backend.repository;

import de.jaypi4c.demo.backend.entity.Book;
import jakarta.annotation.Nonnull;
import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.function.Function;

@Primary
@Profile("dev")
@Repository
public class InMemoryRepository implements BookRepository {

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

    @Nonnull
    @Override
    public <S extends Book> S save(S entity) {
        UUID uuid = UUID.randomUUID();
        entity.setId(uuid);
        books.put(uuid, entity);
        return entity;
    }

    @Nonnull
    @Override
    public <S extends Book> List<S> saveAll(@Nonnull Iterable<S> entities) {
        throw new UnsupportedOperationException();
    }

    @Nonnull
    @Override
    public List<Book> findAll() {
        return new ArrayList<>(books.values());
    }

    @Nonnull
    @Override
    public Optional<Book> findById(@Nonnull UUID uuid) {
        return Optional.ofNullable(books.get(uuid));
    }

    @Override
    public boolean existsById(@Nonnull UUID uuid) {
        return books.containsKey(uuid);
    }

    @Nonnull
    @Override
    public List<Book> findAllById(@Nonnull Iterable<UUID> uuids) {
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
    public void deleteById(@Nonnull UUID uuid) {
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

    @NonNull
    @Override
    public <S extends Book> S saveAndFlush(@Nonnull S entity) {
        return save(entity);
    }

    @Nonnull
    @Override
    public <S extends Book> List<S> saveAllAndFlush(@Nonnull Iterable<S> entities) {
        return saveAll(entities);
    }

    @Override
    public void deleteAllInBatch(@Nonnull Iterable<Book> entities) {
        deleteAll(entities);
    }

    @Override
    public void deleteAllByIdInBatch(@Nonnull Iterable<UUID> uuids) {
        deleteAllById(uuids);
    }

    @Override
    public void deleteAllInBatch() {
        deleteAll();
    }

    @Nonnull
    @Override
    public Book getOne(@Nonnull UUID uuid) {
        throw new UnsupportedOperationException();
    }

    @Nonnull
    @Override
    public Book getById(@Nonnull UUID uuid) {
        return books.get(uuid);
    }

    @Nonnull
    @Override
    public Book getReferenceById(@Nonnull UUID uuid) {
        return books.get(uuid);
    }

    @Nonnull
    @Override
    public <S extends Book> Optional<S> findOne(@Nonnull Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Nonnull
    @Override
    public <S extends Book> List<S> findAll(@Nonnull Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Nonnull
    @Override
    public <S extends Book> List<S> findAll(@Nonnull Example<S> example, @Nonnull Sort sort) {
        throw new UnsupportedOperationException();

    }

    @Nonnull
    @Override
    public <S extends Book> Page<S> findAll(@Nonnull Example<S> example, @Nonnull Pageable pageable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Book> long count(@Nonnull Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Book> boolean exists(@Nonnull Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Nonnull
    @Override
    public <S extends Book, R> R findBy(@Nonnull Example<S> example, @Nonnull Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        throw new UnsupportedOperationException();
    }

    @Nonnull
    @Override
    public List<Book> findAll(@Nonnull Sort sort) {
        throw new UnsupportedOperationException();
    }

    @Nonnull
    @Override
    public Page<Book> findAll(@Nonnull Pageable pageable) {
        throw new UnsupportedOperationException();
    }
}
