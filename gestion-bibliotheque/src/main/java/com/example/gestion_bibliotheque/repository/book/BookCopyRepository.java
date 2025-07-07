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
    // Trouver tous les exemplaires disponibles d’un livre donné
    List<BookCopy> findByBookIdAndStatus(Long bookId, CopyStatus status);

    // Trouver tous les exemplaires empruntés par un utilisateur (via prêts)
    // Cette méthode nécessite un join personnalisé, donc elle sera plutôt dans un service ou repository custom

    // 🔢 Nombre total d'exemplaires pour un livre donné
    long countByBookId(Long bookId);

    // Compter les exemplaires disponibles
    long countByBookIdAndStatus(Long bookId, CopyStatus status);

    Collection<Object> findByBookAndStatus(Book book, CopyStatus copyStatus);

    List<BookCopy> findByBook(Book book);

    // Nouvelle méthode pour récupérer toutes les copies d’un livre (sans filtre sur le statut)
    List<BookCopy> findByBookId(Long bookId);

}