package com.example.gestion_bibliotheque.dto.book;

import com.example.gestion_bibliotheque.entity.book.Book;
import com.example.gestion_bibliotheque.entity.book.BookCopy;
import java.util.List;

public class BookWithCopiesDTO {
    private Book book;
    private List<BookCopy> copies;

    public BookWithCopiesDTO(Book book, List<BookCopy> copies) {
        this.book = book;
        this.copies = copies;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public List<BookCopy> getCopies() {
        return copies;
    }

    public void setCopies(List<BookCopy> copies) {
        this.copies = copies;
    }
}
