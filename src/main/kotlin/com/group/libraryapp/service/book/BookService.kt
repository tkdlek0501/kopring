package com.group.libraryapp.service.book

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import com.group.libraryapp.dto.book.request.BookLoanRequest
import com.group.libraryapp.dto.book.request.BookRequest
import com.group.libraryapp.dto.book.request.BookReturnRequest
import com.group.libraryapp.dto.book.response.BookStatResponse
import com.group.libraryapp.repository.book.BookQuerydslRepository
import com.group.libraryapp.repository.user.loanhistory.UserLoanHistoryQuerydslRepository
import com.group.libraryapp.util.fail
import com.group.libraryapp.util.findByNameOrThrow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BookService(
        private val bookRepository: BookRepository,

        private val userRepository: UserRepository,

        private val userLoanHistoryRepository: UserLoanHistoryRepository,

        private val bookQuerydslRepository: BookQuerydslRepository,

        private val userLoanHistoryQuerydslRepository: UserLoanHistoryQuerydslRepository,
) {

    @Transactional
    fun saveBook(request: BookRequest) {
        val newBook = Book(request.name, request.type)
        bookRepository.save(newBook)
    }

    @Transactional
    fun loanBook(request: BookLoanRequest) {
//        val book = bookRepository.findByName(request.bookName) ?: fail() // <- Kt : throw java.lang.IllegalArgumentException() <- Java : orElseThrow(::IllegalArgumentException)
        // TODO: 아래처럼 확장함수 사용해도 되는지 확인해보기
         val book = bookRepository.findByNameOrThrow(request.bookName)

        if (userLoanHistoryQuerydslRepository.find(request.bookName, UserLoanStatus.LOANED) != null) {
            throw java.lang.IllegalArgumentException("진작 대출되어 있는 책입니다")
        }

//        val user = userRepository.findByName(request.userName) ?: fail()
        val user = userRepository.findByNameOrThrow(request.userName)
        user.loanBook(book)
    }

    @Transactional
    fun returnBook(request: BookReturnRequest) {
//        val user = userRepository.findByName(request.userName) ?: fail()
        val user = userRepository.findByNameOrThrow(request.userName)
        user.returnBook(request.bookName)
    }

    @Transactional(readOnly = true)
    fun countLoanedBook(): Int {
        return userLoanHistoryQuerydslRepository.count(UserLoanStatus.LOANED).toInt()
    }

    @Transactional(readOnly = true)
    fun getBookStatistics(): List<BookStatResponse> {
        // ver 4 : 애플리케이션이 아니라 쿼리에서 group by를 사용해서 조회
        return bookQuerydslRepository.getStatus()

        // ver 3 : 2차 리팩토링 ; type 별로 '묶을' 것이니까 groupBy 를 사용하는 게 좋다
//        return bookRepository.findAll()
//            .groupBy { book -> book.type }
//            .map { (type, books) -> BookStatResponse(type, books.size.toLong()) }

        // ver 1, 2 공통
//        val results = mutableListOf<BookStatResponse>() // 가변 리스트를 사용해서 테스트 시 잘못 건드릴 수 있음
//        val books = bookRepository.findAll()
//        books.map { book -> results.firstOrNull { dto -> book.type == dto.type}?.plusOne()
//            ?: results.add(BookStatResponse(book.type, 1))}

        // ver 2 : 1차 리팩토링 ; 콜체인이 길어서 유지보수하기 어렵다는 문제 있음 또한 수정하기 어려워짐
//        for (book in books) {
//            results.firstOrNull { dto -> book.type == dto.type }?.plusOne() // ?. : null 이 아닌 경우 실행
//                ?: results.add(BookStatResponse(book.type, 1)) // ?: : null 인 경우 실행

        // ver 1 : 리팩토링 하기 전
//            val targetDto = results.firstOrNull { dto -> book.type == dto.type  } // 이미 dto 로 만들어진 타입이 있는지
//            if (targetDto == null) { // 없으면 results 에 해당 타입 최초로 넣어준다
//                results.add(BookStatResponse(book.type, 1))
//            } else { // 있으면 count 1 증가
//                targetDto.plusOne()
//            }
//        }

//        return results
    }


}