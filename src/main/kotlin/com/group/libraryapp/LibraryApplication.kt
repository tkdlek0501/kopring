package com.group.libraryapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LibraryApplication

// index 주소  http://localhost:8080/v1/index.html
// h2 주소  http://localhost:8080/h2-console
// JDBC URL  jdbc:h2:mem:library
// User Name  user

fun main(args: Array<String>) {
    runApplication<LibraryApplication>(*args) // springframework에서 제공하는 runApplication 확장함수
}