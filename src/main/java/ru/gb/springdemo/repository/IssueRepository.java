package ru.gb.springdemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.gb.springdemo.model.Book;
import ru.gb.springdemo.model.Issue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public interface IssueRepository extends JpaRepository<Issue,Long> {

//    private final AtomicLong counter =new AtomicLong();
//    private final Map<Long, Issue> issues = new ConcurrentHashMap<>();
//
//
//    public void save(Issue issue) {
//        issue.setId(counter.incrementAndGet());
//        issues.put(issue.getId(), issue);
//    }
//    // показать список всех заявок
//    public List<Issue> getIssues (){ return new ArrayList<>(issues.values());}
//
//    // получить информацию о запросе по id
//    public Issue getIssueById(Long id) {
//        return issues.get(id);
//    }
//
    //получить список выдач по id читателя
    @Query("SELECT i FROM Issue i WHERE i.readerId = :id")
     List<Issue> getAllIssueByReaderId(Long id);
//       { return issues.values().stream()
//                .filter(issue -> issue.getReaderId().equals(id))
//                .filter(issue -> issue.getTimeReturn()==null)
//                .toList();
//    }
//
//    // возврат книги
//    @Modifying
//    @Query("UPDATE Issue SET i.timeReturn= CURRENT_TIMESTAMP WHERE i.id = :id")
//    Issue returnBooks(Long id);
//    {
//        Issue updateIssue = issues.get(id);
//        if(updateIssue!= null){
//            updateIssue.setTimeReturn(LocalDateTime.now());
//        }
//        return updateIssue;
//    }
//

}
