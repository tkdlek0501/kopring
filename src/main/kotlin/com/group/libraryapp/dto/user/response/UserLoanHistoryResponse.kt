package com.group.libraryapp.dto.user.response

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus

data class UserLoanHistoryResponse(
        val name: String, // 유저 이름
        val books: List<BookHistoryResponse>
) {
        companion object {
                fun of(user: User): UserLoanHistoryResponse {
                        return UserLoanHistoryResponse(
                                name = user.name,
                                books = user.userLoanHistories.map(BookHistoryResponse::of)
                        )
                }
        }
}

data class BookHistoryResponse(
        val name: String, // 책 이름
        val isReturn: Boolean,
) {
        companion object { // 정적 팩토리 메서드를 관리하기 위해 동행객체 사용
                fun of(history: UserLoanHistory): BookHistoryResponse {
                        return BookHistoryResponse(
                                name = history.bookName,
//                    isReturn = history.status == UserLoanStatus.RETURNED
                                isReturn = history.isReturn
                        )
                }
        }
}