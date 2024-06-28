package ru.gb.springdemo.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gb.springdemo.model.Book;
import ru.gb.springdemo.model.Issue;
import ru.gb.springdemo.model.Reader;
import ru.gb.springdemo.service.ReaderService;

import java.util.List;

@RestController
@RequestMapping("/readers")
@Tag(name= "Readers")
public class ReaderController {
    private final ReaderService readerService;

    @Autowired
    public ReaderController(ReaderService readerService) {
        this.readerService = readerService;
    }
    // получить читателя по id
    @Operation(summary = "get reader by ID", description = "Показать читателя по ID")
//    @ApiResponse(responseCode = "код_ответа", description = "описание_ответа")
    @GetMapping("/{id}")
    public ResponseEntity<Reader> getReaderById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(readerService.getReaderById(id));
        } catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    //получить список всех читателей
    @Operation(summary = "get all readers", description = "Показать список всех читателей")
    @GetMapping
    public ResponseEntity<List<Reader>> getAllReaders() {
        return ResponseEntity.ok(readerService.getAllReaders()); }
    

    // создание читателя
    @Operation(summary = "create reader", description = "Добавления нового читателя в общий список системы")
    @PostMapping
    public ResponseEntity<Reader> addReader(@RequestBody Reader reader){
        return new ResponseEntity<>(readerService.addReader(reader),HttpStatus.CREATED);
    }

    //обновление читателей
    @Operation(summary = "update reader", description = "Обновление данных читателя по ID")
    @PutMapping("/{id}")
    public ResponseEntity<Reader> updateReaders(@PathVariable Long id, @RequestBody Reader reader){
        return ResponseEntity.ok(readerService.updateReaders(id, reader));
    }

    // удаление читателя
    @Operation(summary = "create book", description = "Удаление читателя из общего списка в системе")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReader(@PathVariable Long id){
        readerService.deleteReader(id);
        return new ResponseEntity<>( HttpStatus.NO_CONTENT);
    }
    //получить список выдач по id читателя
    @Operation(summary = "get all issue by reader ID", description = "Получение списка выдач читателя по ID читателя")
    @GetMapping("/{id}/issue")
    public List<Issue> getAllIssueByReaderId(@PathVariable Long id){
        return readerService.getAllIssueByReaderId(id);
    }
}
