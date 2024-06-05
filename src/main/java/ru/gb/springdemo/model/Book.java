package ru.gb.springdemo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@Entity
@Table(name = "books")
public class Book {

//  public static long sequence = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private  Long id;
  private String name;

  public Book() {
  }

  public Book(String name) {
    this.name = name;
  }

  public Book(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
