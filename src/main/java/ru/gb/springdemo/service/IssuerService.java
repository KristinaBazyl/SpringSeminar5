package ru.gb.springdemo.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import ru.gb.springdemo.model.Issue;
import ru.gb.springdemo.repository.BookRepository;
import ru.gb.springdemo.repository.IssueRepository;
import ru.gb.springdemo.repository.ReaderRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class IssuerService {

    @Value("${application.max-allowed-books}")
    private int limitBooks;

    // спринг это все заинжектит
    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;
    private final IssueRepository issueRepository;

    @Autowired
    public IssuerService(BookRepository bookRepository, ReaderRepository readerRepository, IssueRepository issueRepository) {
        this.bookRepository = bookRepository;
        this.readerRepository = readerRepository;
        this.issueRepository = issueRepository;
    }
    // создание завки на выдачу
    public Issue saveIssue(Issue issue) {
        if (bookRepository.findById(issue.getBookId()).isEmpty()) {
            throw new NoSuchElementException("Не найдена книга с идентификатором \"" + issue.getBookId() + "\"");
        }
        if (readerRepository.findById(issue.getReaderId()).isEmpty()) {
            throw new NoSuchElementException("Не найден читатель с идентификатором \"" + issue.getReaderId() + "\"");
        }
        // можно проверить, что у читателя нет книг на руках (или его лимит не превышает в Х книг)

        if (issueRepository.getAllIssueByReaderId(issue.getReaderId()).size() >= limitBooks) {
            throw new RuntimeException("пользователь взял допустимое колличество книг");
        }
        Issue newissue = new Issue(issue.getBookId(), issue.getReaderId());
        newissue.setIssuedAt(LocalDate.now());
        issueRepository.save(newissue);
        return newissue;
    }

    // показать список всех заявок
    public List<Issue> getIssues() {
        return issueRepository.findAll();
    }

    // получить информацию о запросе по id
    public Issue getIssueById(Long id) {
        return issueRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    // возврат книги
    @Transactional
    public Issue returnBooks(Long id) {
       Issue updateIssue = issueRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Issue not found"));
       updateIssue.setTimeReturn(LocalDate.now());
       return issueRepository.save(updateIssue);
    }
    // удаление запроса
    public void deleteIssue(Long id) {
        issueRepository.deleteById(id);
    }


}
