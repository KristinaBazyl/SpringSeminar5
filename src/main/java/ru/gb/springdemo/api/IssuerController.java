package ru.gb.springdemo.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gb.springdemo.model.Issue;
import ru.gb.springdemo.service.IssuerService;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequestMapping("/issues")
@Tag(name= "Issues")
public class IssuerController {

    @Autowired
    private IssuerService service;

    // создание запроса
    @Operation(summary = "create issue", description = "Создание выдачи книг читателю")
    @PostMapping
    public ResponseEntity<Issue> issueBook(@RequestBody Issue issue) {
        log.info("Получен запрос на выдачу: readerId = {}, bookId = {}", issue.getReaderId(), issue.getBookId());

        try {
            issue = service.saveIssue(issue);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(issue);
    }

    // показать список всех заявок
    @Operation(summary = "get all issue", description = "Поиск всех выдач в системе")
    @GetMapping
    public ResponseEntity<List<Issue>> getIssues() {
        return ResponseEntity.ok(service.getIssues());
    }

    // получить информацию о запросе по id
    @Operation(summary = "get issue by ID", description = "Поиск выдачи по ID выдачи")
    @GetMapping("/{id}")
    public ResponseEntity<Issue> getIssueById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.getIssueById(id));
        } catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @Operation(summary = "return book", description = "Сдача книги в библиотеку")
    @PutMapping("/{id}")
    public ResponseEntity<Issue> returnBooks(@PathVariable Long id) {

        return ResponseEntity.ok(service.returnBooks(id));
    }
    // удаление выдачи
    @Operation(summary = "delete book", description = "Удаление выдачи из списка в системе")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIssue(@PathVariable Long id) {
        service.deleteIssue(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
