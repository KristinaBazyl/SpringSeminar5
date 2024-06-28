package ru.gb.springdemo.api;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.gb.springdemo.JUnitSpringBootBase;
import ru.gb.springdemo.model.Reader;
import ru.gb.springdemo.repository.ReaderRepository;

import java.util.List;
import java.util.Objects;

class ReaderControllerTest extends JUnitSpringBootBase {

    @Autowired
    WebTestClient webTestClient;
    @Autowired
    ReaderRepository readerRepository;

    @Data
    static class JUnitReader {
        private Long id;
        private String name;
    }

    @Test
    void testGetReaderById() {
        Reader testReader = readerRepository.save(new Reader ("Ivan"));

        JUnitReader reader = webTestClient.get()
                .uri("/readers/" + testReader.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(JUnitReader.class)
                .returnResult().getResponseBody();
        Assertions.assertNotNull(reader);
        Assertions.assertEquals(testReader.getId(), testReader.getId());
        Assertions.assertEquals(testReader.getName(), testReader.getName());
    }

    @Test
    void testFindIdNotFound() {
        Long maxId = Long.MAX_VALUE;

        webTestClient.get()
                .uri("/readers/" + maxId)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testGetAll() {
        readerRepository.saveAll(List.of(new Reader("Vika"),
                new Reader("Javokhir")));
        List<Reader> testReaders = readerRepository.findAll();

        List<JUnitReader> allReaders = webTestClient.get()
                .uri("/readers")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<JUnitReader>>(){})
                .returnResult().getResponseBody();

        Assertions.assertNotNull(allReaders);
        Assertions.assertEquals(testReaders.size(), allReaders.size());
        for (JUnitReader reader : allReaders) {
            boolean found = testReaders.stream()
                    .filter(it -> Objects.equals(it.getId(), reader.getId()))
                    .anyMatch(it -> Objects.equals(it.getName(), reader.getName()));
            Assertions.assertTrue(found);
        }
    }

    @Test
    void testSaveReader() {
        JUnitReader testReader = new JUnitReader();
        testReader.setName("Kris");

        JUnitReader reader = webTestClient.post()
                .uri("/readers")
                .bodyValue(testReader)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(JUnitReader.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(reader);
        Assertions.assertNotNull(reader.id);
        Assertions.assertTrue(readerRepository.findById(reader.getId()).isPresent());
        Assertions.assertEquals(testReader.getName(), reader.getName());
    }

    @Test
    void testDeleteByIdReader() {
        Reader deletedReader = readerRepository.save(new Reader("Gusik"));

        webTestClient.delete()
                .uri("/readers/" + deletedReader.getId())
                .exchange()
                .expectStatus().isNoContent();

        Assertions.assertFalse(readerRepository.findById(deletedReader.getId()).isPresent());
    }

    @Test
    void testUpdateReader() {
        Reader updatedReader = readerRepository.save(new Reader("Podosinovik"));
        JUnitReader testUpdateReader = new JUnitReader();
        testUpdateReader.setName("Haokin");

        JUnitReader readerRes = webTestClient.put()
                .uri("/readers/" + updatedReader.getId())
                .bodyValue(testUpdateReader)
                .exchange()
                .expectStatus().isOk()
                .expectBody(JUnitReader.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(readerRes);
        Assertions.assertEquals(updatedReader.getId(), readerRes.getId());
        Assertions.assertEquals(testUpdateReader.getName(), readerRes.getName());
    }
}
