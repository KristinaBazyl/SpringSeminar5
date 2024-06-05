package ru.gb.springdemo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.gb.springdemo.model.Book;
import ru.gb.springdemo.service.BookService;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
      private final BookService bookService;

      @Autowired
      public BookController(BookService bookService) {
            this.bookService = bookService;
      }
      // получить книгу по id
    @GetMapping("/{id}")
      public ResponseEntity<Book> getBookById(@PathVariable Long id) {
          return ResponseEntity.ok(bookService.getBookById(id));
      }

      //получить список всех книг
    @GetMapping
      public ResponseEntity<List<Book>> getAllBooks() {
          return ResponseEntity.ok(bookService.getAllBooks()); }

    // создание книги
    @PostMapping
       public ResponseEntity<Book> addBook(@RequestBody Book book){
            return new ResponseEntity<>(bookService.addBook(book), HttpStatus.CREATED);
       }

    //обновление книг
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBooks(@PathVariable Long id, @RequestBody Book book){
        return ResponseEntity.ok(bookService.updateBooks(id, book));
    }

    // удаление книги
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id){
        bookService.deleteBook(id);
        return new ResponseEntity<>( HttpStatus.NO_CONTENT);
    }
}
