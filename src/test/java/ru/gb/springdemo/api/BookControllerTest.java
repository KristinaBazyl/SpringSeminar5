package ru.gb.springdemo.api;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.gb.springdemo.JUnitSpringBootBase;
import ru.gb.springdemo.model.Book;
import ru.gb.springdemo.repository.BookRepository;

import java.util.List;
import java.util.Objects;


class BookControllerTest extends JUnitSpringBootBase {
    @Autowired
    WebTestClient webTestClient;
    @Autowired
    BookRepository bookRepository;


    @Data
    static class JUnitBook {
        private Long id;
        private String name;
    }

    @Test
    void getBookById() {
        Book testBook = bookRepository.save(new Book("война и мир"));
        JUnitBook responseBody = webTestClient.get()
                .uri("/books/" + testBook.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(JUnitBook.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals(testBook.getId(), responseBody.getId());
        Assertions.assertEquals(testBook.getName(), responseBody.getName());
    }

    @Test
    void getBookByIdNotFound() {

        webTestClient.get().uri("/books/" + Long.MAX_VALUE)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testGetAll() {
        bookRepository.saveAll(List.of(new Book("aaaa"), new Book("bbb")));
        List<Book> expected = bookRepository.findAll();

        List<JUnitBook> responseBody = webTestClient.get()
                .uri("/books")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<JUnitBook>>() {})
                .returnResult()
                .getResponseBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals(expected.size(), responseBody.size());
        for (JUnitBook book : responseBody) {
            boolean found = expected.stream()
                    .filter(exp -> Objects.equals(exp.getId(), book.getId()))
                    .anyMatch(exp -> Objects.equals(exp.getName(), book.getName()));
            Assertions.assertTrue(found);
        }
    }

    @Test
    void testSave() {
        JUnitBook newBook = new JUnitBook();
        newBook.setName("Java");

        JUnitBook responseBody = webTestClient.post()
                .uri("/books")
                .bodyValue(newBook)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(JUnitBook.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertNotNull(responseBody.getId());
        Assertions.assertTrue(bookRepository.findById(responseBody.getId()).isPresent());
        Assertions.assertEquals(newBook.getName(), responseBody.getName());
    }

    @Test
    void testDeleteById() {
        Book deletedBook = bookRepository.save(new Book("Spring"));

        webTestClient.delete()
                .uri("/books/" + deletedBook.getId())
                .exchange()
                .expectStatus().isNoContent();

        Assertions.assertFalse(bookRepository.findById(deletedBook.getId()).isPresent());
    }

    @Test
    void testUpdateBook() {
        Book updatedBook = bookRepository.save(new Book("voina i mir"));
        JUnitBook testUpdateBook = new JUnitBook();
        testUpdateBook.setName("May");

        JUnitBook bookRes = webTestClient.put()
                .uri("/books/" + updatedBook.getId())
                .bodyValue(testUpdateBook)
                .exchange()
                .expectStatus().isOk()
                .expectBody(JUnitBook.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(bookRes);
        Assertions.assertEquals(updatedBook.getId(), bookRes.getId());
        Assertions.assertEquals(testUpdateBook.getName(), bookRes.getName());
    }

}