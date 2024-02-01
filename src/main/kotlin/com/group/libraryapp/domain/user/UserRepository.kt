package com.group.libraryapp.domain.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserRepository : JpaRepository<User, Long> {

    fun findByName(name: String): User? // java 에서 Optional<User> 을 Kotlin 에서는 ? 로 null 허용할 수 있다.

    // N+1 해결을 위한 메서드 1
    // user와 userLoanHistories 는 1:N 연관관계임을 생각
    // 대출기록이 없는 user 도 다 가져올 것이니까 LEFT JOIN 사용
    // user 가 userLoanHistory 와 join 하면서 여러 row 를 가져오는 것을 방지하기 위해 DISTINCT 사용
    // N+1 쿼리를 없애기 위해 fetch join 사용
    @Query("SELECT DISTINCT u FROM User u " +
            "LEFT JOIN FETCH u.userLoanHistories")
    fun findAllWithHistories(): List<User>
    // DISTINCT 를 사용하면 페이징에는 제한이 생긴다.
    // * 1:N 관계에서는 이것보다 batch size 를 설정해서 1+1 쿼리를 사용하는 게 나을 것 같다.
}