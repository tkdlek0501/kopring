package com.group.libraryapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LibraryAppApplication {
// index 주소  http://localhost:8080/v1/index.html
// h2 주소  http://localhost:8080/h2-console
// JDBC URL  jdbc:h2:mem:library
// User Name  user


  public static void main(String[] args) {
    SpringApplication.run(LibraryAppApplication.class, args);
  }

}
