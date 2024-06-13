package ru.gb.springdemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.gb.springdemo.model.Reader;


@Repository
public interface ReaderRepository extends JpaRepository<Reader,Long> {

}
