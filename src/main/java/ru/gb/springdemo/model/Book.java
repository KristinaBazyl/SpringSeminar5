package ru.gb.springdemo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "books")
@Schema(name = "Книги")
public class Book {

//  public static long sequence = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(name = "Идентификатор")
  private  Long id;
  @Schema(name = "Название книги")
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
