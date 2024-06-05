package ru.gb.springdemo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.gb.springdemo.model.Book;
import ru.gb.springdemo.model.Issue;
import ru.gb.springdemo.model.Reader;
import ru.gb.springdemo.service.BookService;
import ru.gb.springdemo.service.IssuerService;
import ru.gb.springdemo.service.ReaderService;

import java.util.List;

@Controller
@RequestMapping("/ui")
public class UIController {
    private final BookService bookService;
    private final ReaderService readerService;
    private final IssuerService issuerService;
    @Autowired
    public UIController(BookService bookService, ReaderService readerService, IssuerService issuerService) {
        this.bookService = bookService;
        this.readerService = readerService;
        this.issuerService = issuerService;
    }

    @GetMapping("/books")
    public String getAllBooks(Model model){
        List<Book> bookList = bookService.getAllBooks();
        model.addAttribute("books", bookList);
        return "books.html";
    }
    @GetMapping("/readers")
    public String getAllReaders(Model model){
        List<Reader> readerList = readerService.getAllReaders();
        model.addAttribute("readers", readerList);
        return "readers.html";
    }
    @GetMapping("/issues")
    public String getAllIssues(Model model){
        List<Issue> issuesList = issuerService.getIssues();
        model.addAttribute("issues", issuesList);
        return "issues.html";
    }
    @GetMapping("/reader/{id}")
    public String getBookByReaderId(@PathVariable Long id, Model model){
        List<Issue> listBookReader = readerService.getAllIssueByReaderId(id);
        String nameReader = readerService.getReaderById(id).getName();
        model.addAttribute("nameReader",nameReader);
        model.addAttribute("issuesReader", listBookReader);
        return "issues-reader.html";
    }

    @GetMapping("/issue/new")
    public String newIssue(@ModelAttribute("issue") Issue issue){
//        model.addAttribute("issue", new Issue());
        return "new-issue.html";
    }

    @PostMapping("/issue")
    public String createIssue(@ModelAttribute("issue") Issue issue){
        issuerService.saveIssue(issue);
        return "redirect:/issues";
    }

}
