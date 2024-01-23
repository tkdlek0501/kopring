package com.group.libraryapp.dto.user.response

import com.group.libraryapp.domain.user.User

data class UserResponse( // dto는 웬만하면 data class 로 만드는 게 유용하다
        // 주생성자에서는 user를 바로 받지 않고 각각의 프로퍼티를 파라미터로 받게끔
        val id: Long,
        val name: String,
        val age: Int?
) {

    companion object { // 동행 객체
        // 정적 팩토리 메서드
        fun of(user: User): UserResponse {
            return UserResponse(
                    id = user.id!!, // 이 response에는 id: Long 이기 때문에 null 아님 단언
                    name = user.name,
                    age = user.age
            )
        }
    }

// 아래 방법 보다는 위 정적 팩토리 메서드가 좋은 방법
// 부생성자에서 user를 바로 받게 만듦
//    constructor(user: User): this( // this를 통해 주생성자를 부름
//        id = user.id!!, // 이 response에는 id: Long 이기 때문에 null 아님 단언
//        name = user.name,
//        age = user.age
//    )

//    init {
//        id = user.id!!
//        name = user.name
//        age = user.age
//    }
}