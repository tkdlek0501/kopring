package com.group.libraryapp.repository.user.loanhistory

import com.group.libraryapp.domain.user.loanhistory.QUserLoanHistory.userLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component

@Component
class UserLoanHistoryQuerydslRepository(
    private val queryFactory: JPAQueryFactory
) {

    fun find(bookName: String, status: UserLoanStatus? = null): UserLoanHistory? {
        // default parameter에 null을 넣어 외부에서는 bookName만 쓸 수도, status까지 같이 쓸 수도 있다.
        return queryFactory.select(userLoanHistory)
            .from(userLoanHistory)
            .where(
                userLoanHistory.bookName.eq(bookName),
                status?.let { userLoanHistory.status.eq(status) }
            // let 으로 scope function을 쓰면 nullable한 필드를 처리하기 쉽다
            // status가 null이 아닌 경우에만 let 안의 함수가 실행된다.
            )
            .limit(1)
            .fetchOne()
    }

    fun count(status: UserLoanStatus): Long {
        return queryFactory.select(userLoanHistory.count()) // SQL의 count(id)로 변경된다!
            .from(userLoanHistory)
            .where(
                userLoanHistory.status.eq(status)
            )
            .fetchOne() ?: 0L
            // count의 결과는 숫자 1개 이므로 fetchOne()을 사용
            // null의 결과가 나올 경우 default 값으로 0L 을 지
    }
}