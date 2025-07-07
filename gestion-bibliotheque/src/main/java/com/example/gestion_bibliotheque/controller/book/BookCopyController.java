package com.example.gestion_bibliotheque.controller.book;

import com.example.gestion_bibliotheque.dto.book.BookCopyDTO;
import com.example.gestion_bibliotheque.entity.book.BookCopy;
import com.example.gestion_bibliotheque.service.book.impl.BookCopyServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/copies")
public class BookCopyController {

    private final BookCopyServiceImpl copyService;

    public BookCopyController(BookCopyServiceImpl copyService) {
        this.copyService = copyService;
    }

    @GetMapping
    public List<BookCopy> getAll() {
        return copyService.getAllCopies();
    }

    @PostMapping
    public ResponseEntity<BookCopy> create(@RequestBody BookCopyDTO dto) {
        return ResponseEntity.ok(copyService.createCopy(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookCopy> getCopyById(@PathVariable Long id) {
        BookCopy copy = copyService.getCopyById(id);
        if (copy == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(copy);
    }


    @PutMapping("/{id}")
    public ResponseEntity<BookCopy> update(@PathVariable Long id, @RequestBody BookCopyDTO dto) {
        return ResponseEntity.ok(copyService.updateCopy(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        copyService.deleteCopy(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count/total/{bookId}")
    public ResponseEntity<Long> countTotalCopies(@PathVariable Long bookId) {
        long count = copyService.countTotalCopiesByBookId(bookId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/available/{bookId}")
    public ResponseEntity<Long> countAvailableCopies(@PathVariable Long bookId) {
        long count = copyService.countAvailableCopiesByBookId(bookId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/bybook/{bookId}")
    public ResponseEntity<List<BookCopy>> getAllCopiesByBookId(@PathVariable Long bookId) {
        List<BookCopy> copies = copyService.getAllCopyByIdBook(bookId);
        if (copies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(copies);
    }

}