package com.group.libraryapp.service.book

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import com.group.libraryapp.dto.book.request.BookLoanRequest
import com.group.libraryapp.dto.book.request.BookRequest
import com.group.libraryapp.dto.book.request.BookReturnRequest
import com.group.libraryapp.util.fail
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BookService(
        private val bookRepository: BookRepository,

        private val userRepository: UserRepository,

        private val userLoanHistoryRepository: UserLoanHistoryRepository
) {

    @Transactional
    fun saveBook(request: BookRequest) {
        val newBook = Book(request.name, request.type)
        bookRepository.save(newBook)
    }

    @Transactional
    fun loanBook(request: BookLoanRequest) {
        val book = bookRepository.findByName(request.bookName) ?:  fail() // <- Kt : throw java.lang.IllegalArgumentException() <- Java : orElseThrow(::IllegalArgumentException)
        if (userLoanHistoryRepository.findByBookNameAndStatus(request.bookName, UserLoanStatus.LOANED) != null) {
            throw java.lang.IllegalArgumentException("진작 대출되어 있는 책입니다")
        }

        val user = userRepository.findByName(request.userName) ?: fail()
        user.loanBook(book)
    }

    @Transactional
    fun returnBook(request: BookReturnRequest) {
        val user = userRepository.findByName(request.userName) ?: fail()
        user.returnBook(request.bookName)
    }

    // 첫 번째 요구사항 추가하기 - 책의 분야
    // 요구사항
    // 1. 책을 등록할 때에 '분야'를 선택해야 한다.
    //      분야에는 5가지 분야가 있다. - 컴퓨터 / 경제 / 사회 / 언어 / 과학
    // 목표
    // 1. Type, Status 등을 서버에서 관리하는 방법들을 살펴보고 장단점을 이해한다.
    // 2. Test Fixture의 필요성을 느끼고 구성하는 방법을 알아본다.
    // 3. Kotlin에서 Enum + JPA + Spring Boot를 활용하는 방법을 알아본다.


}