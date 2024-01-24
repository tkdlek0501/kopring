package com.group.libraryapp.domain.book

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Book(
    val name: String,

    val type: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
) {

    init {
        if (name.isBlank()) {
            throw IllegalArgumentException("이름은 비어 있을 수 없습니다.")
        }
    }

    companion object {

        // Object Model 패턴
        // 테스트를 위한 함수; 생성자를 직접 이용하지 않는게 좋다(테스트 코드 상에서 일일이 수정이 없어 편하다)
        fun fixture(
                name: String = "책 이름", // default 파라미터를 넣어 값을 안받아도 Book 객체를 생성할 수 있게
                type: String = "COMPUTER",
                id: Long? = null,
        ): Book {
            return Book(
                    name = name,
                    type = type,
                    id = id,
            )
        }
    }

}