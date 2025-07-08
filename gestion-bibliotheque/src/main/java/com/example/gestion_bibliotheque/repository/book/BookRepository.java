package com.example.gestion_bibliotheque.repository.book;

import com.example.gestion_bibliotheque.entity.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
  
    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByCategoryId(Long categoryId);

    List<Book> findByLanguageId(Long languageId);
}