package com.example.gestion_bibliotheque.service.book;
import java.util.List;

import com.example.gestion_bibliotheque.dto.book.BookCopyDTO;
import com.example.gestion_bibliotheque.entity.book.BookCopy;

public interface BookCopyService {

    List<BookCopy> getAllCopies();

    BookCopy createCopy(BookCopyDTO dto);

    BookCopy updateCopy(Long id, BookCopyDTO dto);

    void deleteCopy(Long id);

    long countTotalCopiesByBookId(Long bookId);

    long countAvailableCopiesByBookId(Long bookId);

    BookCopy getCopyById(Long id);

    List<BookCopy> getAllCopyByIdBook(Long bookId);

}