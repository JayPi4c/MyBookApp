package com.jaypi4c.demo.backend.repository;

import com.jaypi4c.demo.backend.entitiy.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {
}
