package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.dto.user.request.UserCreateRequest
import com.group.libraryapp.dto.user.request.UserUpdateRequest
import com.group.libraryapp.dto.user.response.UserLoanHistoryResponse
import com.group.libraryapp.dto.user.response.UserResponse
import com.group.libraryapp.util.findByIdOrThrow
import com.group.libraryapp.util.findByNameOrThrow
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
        val user = userRepository.findByIdOrThrow(request.id) // findByIdOrThrow : exception 처리를 위해 CrudRepository 의 확장 함수를 만듦 <- findByIdOrNull(id) : springframework 에서 코틀린을 위해 제공해주는 메서드
        user.updateName(request.name)
    }

    @Transactional
    fun deleteUser(name: String) {
//        val user = userRepository.findByName(name) ?: fail() // orElseThrow(::IllegalArgumentException) : Java 에서 orElseThrow 로 보통 처리하는 것을 Kotlin 에서는 ?: 을 이용해서 처리할 수 있다
        val user= userRepository.findByNameOrThrow(name)
        userRepository.delete(user)
    }

    @Transactional(readOnly = true)
    fun getUserLoanHistories(): List<UserLoanHistoryResponse> {
        return userRepository.findAllWithHistories().map(UserLoanHistoryResponse::of)
        // response DTO 에 만든 정적 팩토리 메서드 (of()) 를 이용해서 entity 를 dto 로 변환하는 코드를 리팩토링 할 수 있다.(서비스 코드가 굉장히 심플해졌다.)
// N+1 이 발생하는 코드
//        return userRepository.findAll().map { user ->
//            // 여러 user는 한 번의 쿼리에서 가져오지만,
//            UserLoanHistoryResponse(
//                    name = user.name,
//                    books = user.userLoanHistories.map { history ->
//                        // userLoanHistories 를 get 하는 순간
//                        // select * from user_loan_history where user_id = ? 쿼리가 user_id 의 개수만큼 발생한다 (N+1)
//                        BookHistoryResponse(
//                                name = history.bookName,
//                                isReturn = history.status == UserLoanStatus.RETURNED
//                        )
//                    }
//            )
//        }
    }
}