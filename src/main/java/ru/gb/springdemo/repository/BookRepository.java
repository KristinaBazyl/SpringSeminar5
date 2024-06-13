package ru.gb.springdemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gb.springdemo.model.Book;


@Repository
public interface BookRepository extends JpaRepository<Book,Long> {

}
