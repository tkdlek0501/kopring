package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.dto.user.request.UserCreateRequest
import com.group.libraryapp.dto.user.request.UserUpdateRequest
import com.group.libraryapp.dto.user.response.BookHistoryResponse
import com.group.libraryapp.dto.user.response.UserLoanHistoryResponse
import com.group.libraryapp.dto.user.response.UserResponse
import com.group.libraryapp.util.fail
import com.group.libraryapp.util.findByIdOrThrow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
) {

    @Transactional
    fun saveUser(request: UserCreateRequest) {
        val newUser = User(request.name, request.age)
        // User 생성자에 디폴트 파라미터가 들어가있기 때문에 Java에서 처럼 null 등으로 값을 넣어줄 필요가 없다.
        userRepository.save(newUser)
    }

    @Transactional(readOnly = true)
    fun getUsers(): List<UserResponse> {
        return userRepository.findAll().map { user ->
            UserResponse.of(user)
//            UserResponse(it) user 를 안쓰고 it으로 만들어줄 수도 있다.
        }
        // java에서 stream().map() 을 위처럼 바꿔 쓸 수 있다.
    }

    @Transactional
    fun updateUserName(request: UserUpdateRequest) {
        val user = userRepository.findByIdOrThrow(request.id) ?: fail() // findByIdOrThrow : CrudRepository 의 확장 함수를 만듦 <- findByIdOrNull(id) : springframework 에서 코틀린을 위해 제공해주는 메서드
        user.updateName(request.name)
    }

    @Transactional
    fun deleteUser(name: String) {
        val user = userRepository.findByName(name) ?: fail() // orElseThrow(::IllegalArgumentException)
        userRepository.delete(user)
    }

    @Transactional(readOnly = true)
    fun getUserLoanHistories(): List<UserLoanHistoryResponse> {
        userRepository.findAll().map { user ->
            UserLoanHistoryResponse(
                    name = user.name,
                    books = user.userLoanHistories.map { history ->
                        BookHistoryResponse(
                                name = history.bookName,
                                isReturn = history.sta
                        )
                    }
            )
        }
    }
}