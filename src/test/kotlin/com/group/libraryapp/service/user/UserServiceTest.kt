package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.dto.user.request.UserCreateRequest
import com.group.libraryapp.dto.user.request.UserUpdateRequest
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserServiceTest @Autowired constructor( // 생성자에 @Autowired 를 공통으로 붙인다
//        @Autowired
        private val userRepository: UserRepository,
//        @Autowired
        private val userService: UserService,
) {

    @AfterEach
    fun clean() {
        userRepository.deleteAll()
    }

    @Test
    @DisplayName("유저 저장이 정상 동작한다")
    fun saveUserTest() {
        // given
        val request = UserCreateRequest("김현준", null)

        // when
        userService.saveUser(request)
        // 하나를 저장하고

        // then
        val results = userRepository.findAll()

        assertThat(results).hasSize(1)
        // 생성된 유저가 진짜 1개인지 검증

        assertThat(results[0].name).isEqualTo("김현준")
        assertThat(results[0].age).isNull()
        // 생성된 유저의 정보가 저장하려는 값과 일치하는지 검증
        // -> results[0].age must not be null : NPE 발생
        // -> java 코드로 Integer로 돼있지만 코틀린에서는 null 허용인지 모르기 때문에
        // null이 안들어갈 것이라고 가정하고 가져옴 (플랫폼 타입)
        // => age getter에 @Nullable(jetbrains) 붙여주면 된다
    }

    @Test
    @DisplayName("유저 조회가 정상 동작한다")
    fun getUsersTest() {
        // given
        userRepository.saveAll(listOf(
            User("A", 20),
            User("B", null),
        ))

        // when
        val results = userService.getUsers()

        // then
        assertThat(results).hasSize(2) // 각각의 테스트를 할 때는 테스트가 성공하지만, 전체 테스트일 때는 실패(Expected size: 2 but was: 3 in:)한다.
        // -> 두 테스트가 Spring Context를 공유하기 때문에
        // => 테스트가 끝나면 공유 자원인 DB를 깨끗하게 해줘야 한다 : @AfterEach를 활용
        assertThat(results).extracting("name").containsExactlyInAnyOrder("A", "B")
        assertThat(results).extracting("age").containsExactlyInAnyOrder(20, null)
    }

    @Test
    @DisplayName("유저 이름 수정이 정상 동작한다")
    fun updateUserNameTest() {
        // given
        val savedUSer = userRepository.save(User("A", null))
        val request = UserUpdateRequest(savedUSer.id, "B")

        // when
        userService.updateUserName(request)

        // then
        val result = userRepository.findAll()[0]
        assertThat(result.name).isEqualTo("B")
    }

    @Test
    @DisplayName("유저 삭제가 정상 동작한다")
    fun deleteUserTest() {
        // given
        userRepository.save(User("A", null))

        // when
        userService.deleteUser("A")

        // then
        assertThat(userRepository.findAll()).isEmpty()
    }
}