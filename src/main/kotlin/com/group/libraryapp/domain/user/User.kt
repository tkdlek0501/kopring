package com.group.libraryapp.domain.user

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import javax.persistence.*

@Entity
class User(
        var name: String,

        val age: Int?,

        @Enumerated(EnumType.STRING)
        val status: UserStatus = UserStatus.ACTIVE,

        @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
        // cascade 설정시 java와 다르게 대괄호로 감싸야 한다. : [CascadeType.ALL]
        val userLoanHistories: MutableList<UserLoanHistory> = mutableListOf(),
        // loanBook() 에서 수정(add)이 가능해야 하므로 MutableList 로 만들어줌

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,
) {

    init {
        if(name.isBlank()) {
            throw IllegalArgumentException("이름은 비어 있을 수 없습니다")
        }
    }

    fun updateName(name: String) {
        this.name = name
    }

    fun loanBook(book: Book) {
        this.userLoanHistories.add(UserLoanHistory(this, book.name))
        // cascade persist 에 의해 User insert 시 UserLoanHistory 같이 insert
    }

    fun returnBook(bookName: String) {
        this.userLoanHistories.first { history -> history.bookName == bookName }.doReturn()
        // 조건에 만족하는 첫번째 요소 찾아옴
    }
}