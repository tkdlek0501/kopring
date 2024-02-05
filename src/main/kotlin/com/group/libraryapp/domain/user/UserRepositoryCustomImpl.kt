package com.group.libraryapp.domain.user

import com.group.libraryapp.domain.user.QUser.user
import com.group.libraryapp.domain.user.loanhistory.QUserLoanHistory.userLoanHistory
import com.querydsl.jpa.impl.JPAQueryFactory

class UserRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : UserRepositoryCustom{

    // 기존 JPQL 로 만든 쿼리(findAllWithHistories)를 querydsl로 만들어본다.
    override fun findAllWithHistories(): List<User> {
        return queryFactory.select(user).distinct()
            .from(user)
//            .leftJoin(userLoanHistory).on(userLoanHistory.user.id.eq(user.id)).fetchJoin()
            .leftJoin(user.userLoanHistories, userLoanHistory)
            .fetch()
    }

}