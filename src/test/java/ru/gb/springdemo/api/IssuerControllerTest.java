package ru.gb.springdemo.api;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.gb.springdemo.JUnitSpringBootBase;
import ru.gb.springdemo.model.Book;
import ru.gb.springdemo.model.Issue;
import ru.gb.springdemo.model.Reader;
import ru.gb.springdemo.repository.BookRepository;
import ru.gb.springdemo.repository.IssueRepository;
import ru.gb.springdemo.repository.ReaderRepository;
import ru.gb.springdemo.service.IssuerService;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;


import static org.junit.jupiter.api.Assertions.*;

class IssuerControllerTest extends JUnitSpringBootBase {

    @Autowired
    WebTestClient webTestClient;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    ReaderRepository readerRepository;
    @Autowired
    IssueRepository issueRepository;
    @Autowired
    IssuerService issuesService;


    @Data
    static class JUnitIssue {
        private Long id;
        private Long bookId;
        private Long readerId;
        private LocalDate issuedAt;
        private LocalDate timeReturn;

        public JUnitIssue(Long bookId, Long readerId) {
            this.bookId = bookId;
            this.readerId = readerId;
        }
    }

    @Test
    @DisplayName("Получение выдачи по ID")
    void getIssueById() {
        Book book = bookRepository.save(new Book("Java"));
        Reader reader = readerRepository.save(new Reader("Artem"));
        Issue requiredIssue = issuesService.saveIssue(new Issue(book.getId(), reader.getId()));

        JUnitIssue responseBody = webTestClient.get()
                .uri("/issues/" + requiredIssue.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(JUnitIssue.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals(requiredIssue.getId(), responseBody.getId());
        Assertions.assertEquals(requiredIssue.getBookId(), responseBody.getBookId());
        Assertions.assertEquals(requiredIssue.getReaderId(), responseBody.getReaderId());
        Assertions.assertEquals(requiredIssue.getIssuedAt(), responseBody.getIssuedAt());
        Assertions.assertNull(responseBody.getTimeReturn());
    }

    @Test
    @DisplayName("Получение выдачи по несуществующему ID")
    void getIssueByIdNotFound() {
        webTestClient.get().uri("/issues/" + Long.MAX_VALUE)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("Получение всех выдач")
    void testGetAllIssue() {
        Book book = bookRepository.save(new Book("Java"));
        Reader reader = readerRepository.save(new Reader("Artem"));
        Book book1 = bookRepository.save(new Book("Spring"));
        Reader reader2 = readerRepository.save(new Reader("Ivan"));
        issueRepository.saveAll(List.of(new Issue(book.getId(), reader.getId()),
        new Issue(book1.getId(), reader2.getId())));
        List<Issue> expected = issueRepository.findAll();

        List<JUnitIssue> responseBody = webTestClient.get()
                .uri("/issues")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<JUnitIssue>>() {})
                .returnResult()
                .getResponseBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals(expected.size(), responseBody.size());
        for (JUnitIssue issue : responseBody) {
            boolean found = expected.stream()
                    .filter(exp -> Objects.equals(exp.getId(), issue.getId()))
                    .filter(exp -> Objects.equals(exp.getReaderId(), issue.getReaderId()))
                    .filter(exp -> Objects.equals(exp.getBookId(), issue.getBookId()))
                    .anyMatch(exp -> Objects.equals(exp.getIssuedAt(), issue.getIssuedAt()));
            Assertions.assertTrue(found);
        }
    }

    @Test
    void testSaveIssue() {
        Book book = bookRepository.save(new Book("Java"));
        Reader reader = readerRepository.save(new Reader("Artem"));
        JUnitIssue savedIssue = new JUnitIssue(book.getId(), reader.getId());

        JUnitIssue responseBody = webTestClient.post()
                .uri("/issues")
                .bodyValue(savedIssue)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(JUnitIssue.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertNotNull(responseBody.getId());
        Assertions.assertEquals(responseBody.getBookId(), savedIssue.getBookId());
        Assertions.assertEquals(responseBody.getReaderId(), savedIssue.getReaderId());
        Assertions.assertTrue(issueRepository.findById(responseBody.getId()).isPresent());
    }

    @Test
    void testDeleteById() {
        Book book = bookRepository.save(new Book("Java"));
        Reader reader = readerRepository.save(new Reader("Artem"));
        Issue deletedIssue = issuesService.saveIssue(new Issue(book.getId(), reader.getId()));

        webTestClient.delete()
                .uri("/issues/" + deletedIssue.getId())
                .exchange()
                .expectStatus().isNoContent();

        Assertions.assertFalse(issueRepository.findById(deletedIssue.getId()).isPresent());
    }

    @Test
    void testUpdateIssue() {
        Book book = bookRepository.save(new Book("Java"));
        Reader reader = readerRepository.save(new Reader("Artem"));
        Issue saveIssue = issuesService.saveIssue(new Issue(book.getId(), reader.getId()));
        JUnitIssue updatedIssue = new JUnitIssue(book.getId(), reader.getId());


        JUnitIssue responseBody = webTestClient.put()
                .uri("/issues/" + saveIssue.getId())
                .bodyValue(updatedIssue)
                .exchange()
                .expectStatus().isOk()
                .expectBody(JUnitIssue.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals(responseBody.getId(), saveIssue.getId());
        Assertions.assertEquals(responseBody.getBookId(), saveIssue.getBookId());
        Assertions.assertEquals(responseBody.getReaderId(), saveIssue.getReaderId());
        Assertions.assertEquals(responseBody.getIssuedAt(), saveIssue.getIssuedAt());
        Assertions.assertEquals(responseBody.getTimeReturn(), LocalDate.now());
        Assertions.assertTrue(issueRepository.findById(responseBody.getId()).isPresent());
    }


}