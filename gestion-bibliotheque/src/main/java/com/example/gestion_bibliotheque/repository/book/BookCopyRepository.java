package com.example.gestion_bibliotheque.repository.book;

import com.example.gestion_bibliotheque.entity.book.Book;
import com.example.gestion_bibliotheque.entity.book.BookCopy;
import com.example.gestion_bibliotheque.enums.CopyStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookCopyRepository extends JpaRepository<BookCopy, Long> {
   
    List<BookCopy> findByBookIdAndStatus(Long bookId, CopyStatus status);

    long countByBookId(Long bookId);

    long countByBookIdAndStatus(Long bookId, CopyStatus status);

    Collection<Object> findByBookAndStatus(Book book, CopyStatus copyStatus);

    List<BookCopy> findByBook(Book book);

    List<BookCopy> findByBookId(Long bookId);

}