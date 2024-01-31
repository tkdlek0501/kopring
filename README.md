<details>
  <summary>24.01.10 수 (~3강)</summary>
  <!-- 내용 -->
  **기존 자바 소스 살펴보기 및 코틀린 사용을 위한 세팅**

도서관리 애플리케이션 리팩토링 목표

1. Java로 작성된 도서관리 애플리케이션 이해
2. 테스트 코드의 필요성 이해, Junit5로 Spring Boot의 테스트 코드 작성
3. Kotlin으로 테스트를 작성하며 Kotlin 코드 작성에 익숙해진다.

코틀린을 사용하기 위해서는 plugin이 필요하다.

build.gradle을 열어 plugins 에 추가해야 한다.

```java
id 'org.jetbrains.kotlin.jvm' version '1.6.21'
```

또한 dependency도 추가해야 한다.

dependencies 부분에

```java
implementation 'org.jetbrains.kotlin:kotlin-stdlib-jdk8'
```

그리고 코틀린으로 컴파일 해야 하기 때문에

```java
compileKotlin {
    kotlinOptions {
        jvmTarget = "11"
    }
}

compileTestKotlin {
    kotlinOptions {
        jvmTarget = "11"
    }
}
```

JDK 버전에 맞춰 11로 세팅해준다.

위와 같이 build.gradle을 수정하고 refresh를 해준 뒤,

실제 코틀린 코드를 작성하기 위해 코틀린을 위한 패키지를 만들어준다.

src-main 경로에 java 패키지 아래 kotlin 디렉토리를 생성해준다.

마찬가지로 src-test 경로에도 kotlin 디렉토리를 생성해준다.

이후 자바의 패키지 구조와 동일하게 kotlin 디렉토리 안에도 패키지를 만들어준다.
</details>

<details>
  <summary><b>24.01.15 월 (4~7강)</b></summary>
  <!-- 내용 -->
  - **사칙연산 계산기에 대해 테스트 코드 작성 (수동 테스트)**
    
    ```java
    package com.group.libraryapp.calculator
    
    // 계산기 요구사항
    // 1. 계산기는 정수만을 취급한다.
    // 2. 계산기가 생성될 때 숫자를 1개 받는다.
    // 3. 최초 숫자가 기록된 이후에는 연산자 함수를 통해
    // 숫자를 받아 지속적으로 계산한다.
    class Calculator(
        var number: Int
    ) {
    
        fun add(operand: Int) {
            this.number += operand
        }
    
        fun minus(operand: Int) {
            this.number -= operand
        }
    
        fun multiply(operand: Int) {
            this.number *= operand
        }
    
        fun divide(operand: Int) {
            if (operand == 0) {
                throw IllegalArgumentException("0으로 나눌 수 없습니다")
            }
            this.number /= operand
        }
    
    }
    ```
    
    ```java
    package com.group.libraryapp.calculator
    
    fun main() {
        val calculatorTest = CalculatorTest()
    //    calculatorTest.addTest()
        calculatorTest.addTest2()
        calculatorTest.minusTest()
        calculatorTest.multiplyTest()
        calculatorTest.divideTest()
        calculatorTest.divideExceptionTest()
    }
    
    class CalculatorTest {
    
        //data class Calculator ; Calculator를 data class로 만들었을 때 add() 테스트 코드
        fun addTest() {
            // given : 테스트 대상을 만들어 준비하는 과정
            val calculator = Calculator(5)
    
            // when : 실제 우리가 테스트 하고 싶은 기능을 호출하는 과정
            calculator.add(3)
    
            // then : 호출 이후 의도한대로 결과가 나왔는지 확인하는 과정
            val expectedCalculator = Calculator(8)
            if (calculator != expectedCalculator) {
                throw IllegalStateException()
            }
        }
    
        // Calculator의 number를 private이 아닌 public(기본) 으로 만들었을 때 add() 테스트 코드
        fun addTest2() {
            // given
            val calculator = Calculator(5)
    
            // when
            calculator.add(3)
    
            // number가 private이 아니라 public 이면 setter가 열려서 거부감이 들 수 있다.
            // -> Calculator 안에서 public 커스텀 getter를 만들어 활용하면 된다.
            // ex.
    //        class Calculator(
    //                private var _number: Int
    //        ) {
    //
    //            // getter만 열어주기 위해 다음과 같은 방법 사용
    //            val number: Int
    //                get() = this._number
            // but 추가적인 코드가 필요하다는 비용이 발생하기 때문에
            // setter 를 그냥 열어두고 사용하지 않음으로 약속하는 것으로 한다.
    
            // then
            if (calculator.number != 8) {
                throw IllegalStateException()
            }
        }
    
        fun minusTest() {
            // given
            val calculator = Calculator(5)
    
            // when
            calculator.minus(3)
    
            // then
            if (calculator.number != 2) {
                throw IllegalStateException()
            }
        }
    
        fun multiplyTest() {
            // given
            val calculator = Calculator(5)
    
            // when
            calculator.multiply(3)
    
            // then
            if (calculator.number != 15) {
                throw IllegalStateException()
            }
        }
    
        fun divideTest() {
            // given
            val calculator = Calculator(5)
    
            // when
            calculator.divide(2)
    
            // then
            if (calculator.number != 2) {
                throw IllegalStateException()
            }
        }
    
        // divide 함수에서는 0이 들어가면 exception을 발생하게 해놨으므로 이 부분도 테스트 한다.
        fun divideExceptionTest() {
            // given
            val calculator = Calculator(5)
    
            // when
            try {
                calculator.divide(0)
            } catch (e: IllegalArgumentException) {
                if (e.message != "0으로 나눌 수 없습니다") {
                    throw IllegalStateException("기대하는 예외 메시지가 아닙니다.")
                }
                return // 테스트 성공!
            } catch (e: Exception) {
                throw IllegalStateException()
            }
    
            throw IllegalStateException("기대하는 예외가 발생하지 않았습니다.")
        }
    
        // 위처럼 수동으로 만든 테스트 코드의 단점 -> JUnit5 프레임워크 사용하는 게 좋다
        // 1. 테스트 클래와 메소드가 생길 때마다 메인 메소드에 수동으로 코드를
        // 작성해주어야 하고, 메인 메소드가 아주 커진다.
        // 테스트 메소드를 개별적으로 실행하기도 어렵다.
    
        // 2. 테스트가 실패한 경우 무엇을 기대하였고, 어떤 잘못된 값이 들어와
        // 실패했는지 알려주지 않는다.
        // 예외를 던지거나, try catch 를 사용해야 하는 등 직접 구현해야할 부분이 많아 불편하다.
    
        // 3. 테스트 메소드별로 공통적으로 처리해야 하는 기능이 있다면,
        // 메소드마다 중복이 생긴다.
    }
    ```
    
- **Junit5 사용법과 테스트 코드 리팩토링**
    - Junit5 에서 사용되는 5가지 어노테이션
        
        **@Test** : 테스트 메소드를 지정한다. 테스트 메소드를 실행하는 과정에서 오류가 없으면 성공이다.
        
        **@BeforeEach** : 각 테스트 메소드가 수행되기 전에 실행되는 메소드를 지정한다.
        
        **@AfterEach** : 각 테스트가 수행된 후에 실행되는 메소드를 지정한다.
        
        **@BeforeAll** : 모든 테스트를 수행하기 전에 최초 1회 수행되는 메소드를 지정한다.
        
        **@AfterAll** : 모든 테스트를 수행한 후 최후 1회 수행되는 메소드를 지정한다.
        
    - Junit5에서 자주 사용되는 단언문 몇 가지
        
        ```kotlin
        val isNew = true
        assertThat(isNew).isTrue
        assertThat(isNew).isFalse
        // true/ false 확인
        
        val people = listOf(Person("A"), Person("B"))
        assertThat(people).hasSize(2)
        // size 확인
        
        val people = listOf(Person("A"), Person("B"))
        assertThat(people).extracting("name").containsExactlyInAnyOrder("A", "B")
        // 주어진 컬렉션 안의 item 들에서
        // name 이라는 프로퍼티를 추출한 후
        // 그 값이 A와 B인지 검증
        
        val people = listOf(Person("A"), Person("B"))
        assertThat(people).extracting("name").containsExactly("A", "B")
        // 주어진 컬렉션 안의 item 들에서
        // name 이라는 프로퍼티 추출한 후
        // 그 값이 A와 B인지 검증 (순서까지 확인)
        
        assertThrows<IllegalArgumentException> {
        	function1()
        }
        // function1 함수를 실행했을 때
        // IllegalArgumentException이 나오는지 검증
        
        val message = assertThrows<IllegalArgumentException> {
        	function1()
        }.message
        assertThat(message).isEqualTo("잘못된 값이 들어왔습니다")
        // message를 가져와 예외 메시지를 확인할 수 있다.
        ```
        
    - Junit5로 리팩토링
        
        ```kotlin
        package com.group.libraryapp.calculator
        
        import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
        import org.junit.jupiter.api.Test
        import org.junit.jupiter.api.assertThrows
        
        class JunitCalculatorTest {
        
            @Test
            fun addTest() {
                // given
                val calculator = Calculator(5)
        
                // when
                calculator.add(3)
        
                // then
                assertThat(calculator.number).isEqualTo(8)
                // 만약 기대값을 다르게 한다면 fail 이 나며 아래와 같이 나온다
        //        expected: 7
        //        but was: 8
            }
        
            @Test
            fun minusTest() {
                // given
                val calculator = Calculator(5)
        
                // when
                calculator.minus(3)
        
                // then
                assertThat(calculator.number).isEqualTo(2)
            }
        
            @Test
            fun multiplyTest() {
                // given
                val calculator = Calculator(5)
        
                // when
                calculator.multiply(3)
        
                // then
                assertThat(calculator.number).isEqualTo(15)
            }
        
            @Test
            fun divideTest() {
                // given
                val calculator = Calculator(5)
        
                // when
                calculator.divide(2)
        
                // then
                assertThat(calculator.number).isEqualTo(2)
            }
        
            @Test
            fun divideExceptionTest() {
                // given
                val calculator = Calculator(5)
        
                // when & then
                val message = assertThrows<IllegalArgumentException> {
                    calculator.divide(0)
                }.message
        
                assertThat(message).isEqualTo("0으로 나눌 수 없습니다")
            }
        
        		@Test
            fun divideExceptionTest2() {
                // given
                val calculator = Calculator(5)
        
                // when & then
                assertThrows<IllegalArgumentException> {
                    calculator.divide(0)
                }.apply {
                    assertThat(message).isEqualTo("0으로 나눌 수 없습니다")
                }
            }
        }
        ```
</details>

<details>
  <summary>24.01.16 화 (7~8강)</summary>
  <!-- 내용 -->
  - **Junit5로 Spring Boot 테스트 하기**
    
    어떤 계층을 어떻게 테스트 해야 할까?
    
    - Spring Boot의 Layered Architecture
        
        Controller : 스프링 컨텍스트에 의해 관리되는 Bean
        
        Service : 스프링 컨텍스트에 의해 관리되는 Bean
        
        Repository : 스프링 컨텍스트에 의해 관리되는 Bean
        
        Domain : 순수한 Java 객체(POJO)
        
    - Spring Boot 각 계층을 테스트 하는 방법
        
        Domain 계층 : 클래스를 테스트하는 것과 동일
        
        Service, Repository 계층 : 스프링 빈을 사용하는 테스트 방법 사용 (@SpringBootTest), 데이터 위주 검증
        
        Controller 계층 : 스프링 빈을 사용하는 테스트 방법 사용(@SpringBootTest), 응답받은 JSON을 비롯한 HTTP 위주의 검증
        
    - 어떤 계층을 테스트 해야 할까?
        
        당연히 best는 모든 계층에 대해 많은 case를 검증하는 것 but 현실적으로 유지 보수와 코딩 시간을 고려해 보통 Service 계층을 테스트 한다.(A를 보냈을 때 B가 잘 나오는지, 원하는 로직을 잘 수행 하는지 검증할 수 있기 때문)
        
    - 예시
        
        ```kotlin
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
        ```
</details>

<details>
  <summary><b>24.01.18 목 (9~11강)</b></summary>
  <!-- 내용 -->
  **BookServiceTest 만들기**

```kotlin
package com.group.libraryapp.service.book

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.dto.book.request.BookLoanRequest
import com.group.libraryapp.dto.book.request.BookRequest
import com.group.libraryapp.dto.book.request.BookReturnRequest
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class BookServiceTest @Autowired constructor(

        private val bookService: BookService,
        private val bookRepository: BookRepository,
        private val userRepository: UserRepository,
        private val userLoanHistoryRepository: UserLoanHistoryRepository,
){

    @AfterEach
    fun clean() {
        bookRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    @DisplayName(value = "책 등록이 정상 동작한다")
    fun saveBookTest() {
        // given
        val request = BookRequest("이상한 나라의 앨리스")

        // when
        bookService.saveBook(request)

        // then
        val books = bookRepository.findAll()
        assertThat(books).hasSize(1)
        assertThat(books[0].name).isEqualTo("이상한 나라의 앨리스")
    }

    @Test
    @DisplayName(value = "책 대출이 정상 동작한다")
    fun loanBookTest() {
        // given
        bookRepository.save(Book("이상한 나라의 앨리스"))
        val savedUser = userRepository.save(User("김현준", null))
        val request = BookLoanRequest("김현준", "이상한 나라의 앨리스")

        // when
        bookService.loanBook(request)

        // then
        val results = userLoanHistoryRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results[0].bookName).isEqualTo("이상한 나라의 앨리스")
        assertThat(results[0].user.id).isEqualTo(savedUser.id)
        assertThat(results[0].isReturn).isFalse()
    }

    @Test
    @DisplayName(value = "책이 진작 대출되어 있다면, 신규 대출이 실패한다")
    fun loanBookFailTest() {
        // given
        bookRepository.save(Book("이상한 나라의 앨리스"))
        val savedUser = userRepository.save(User("김현준", null))
        userLoanHistoryRepository.save(UserLoanHistory(savedUser, "이상한 나라의 앨리스", false))
        val request = BookLoanRequest("김현준", "이상한 나라의 앨리스")

        // when & then
        val message = assertThrows<IllegalArgumentException> {
            bookService.loanBook(request)
        }.message
        assertThat(message).isEqualTo("진작 대출되어 있는 책입니다")
    }

    @Test
    @DisplayName("책 반납이 정상 동작한다")
    fun returnBookTest() {
        // given
        bookRepository.save(Book("이상한 나라의 앨리스"))
        val savedUser = userRepository.save(User("김현준", null))
        userLoanHistoryRepository.save(UserLoanHistory(savedUser, "이상한 나라의 앨리스", false))
        val request = BookReturnRequest("김현준", "이상한 나라의 앨리스")

        // when
        bookService.returnBook(request)

        // then
        val results = us  erLoanHistoryRepository.findAll()
        assertThat(results).hasSize(1)
        assertTha  t(results[0].isReturn).isTrue()
    }

}
```
</details>

<details>
  <summary><b>24.01.22 월 (12~14강)</b></summary>
  <!-- 내용 -->
  - **도메인 계층을 Kotlin으로 변경하기 (12, 13강)**
    
    ```kotlin
    @Entity
    class User(
            var name: String,
    
            val age: Int?, // age는 null 허용해서 '?' 붙였다
    
            @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
            // cascade 설정시 java와 다르게 대괄호로 감싸야 한다. : [CascadeType.ALL]
            val userLoanHistories: MutableList<UserLoanHistory> = mutableListOf(),
    
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
            this.userLoanHistories.add(UserLoanHistory(this, book.name, false))
        }
    
        fun returnBook(bookName: String) {
            this.userLoanHistories.first() { history -> history.bookName == bookName }
    				.doReturn()
            // first : 조건에 만족하는 첫번째 요소 찾아옴
        }
    }
    ```
    
- **Kotlin과 JPA를 함께 사용할 때 주의점**
    1. **setter**
    
    ```kotlin
    @Entity
    class User(
    	var name: String,
    	
    	val age: Int?,
    )
    ```
    
    ```kotlin
    fun updateName(name: String) {
    	this.name = name
    }
    ```
    
    setter 대신 좋은 이름의 함수를 사용하는 것이 훨씬 clean하다.
    
    var 프로퍼티가 퍼블릭으로 열려있기 때문에 setter를 쓸 수도 있지만 setter 대신에 좋은 이름의 함수를 사용하는 것이 훨씬 좋기 때문에 위처럼 사용
    
    but, name에 대한 setter는 public 이기 때문에 updateName 메서드를 사용하는 대신 setter를 사용할수도 있다.
    
    근데 Java 코드에서는 애초에 setter를 안 만들어주는 것을 지향하기 때문에 코드 상 setter를 열린 상태로 두는 것이 불편할 수 있다.
    
    public getter는 꼭 필요하기 때문에 setter만 private하게 만드는 것이 최선이다!
    
    → setter를 private 하게 만드는 방법 2 가지
    
    1. backing property(관례상 ‘_’를 붙인 프로퍼티)를 이용하기
    
    ```kotlin
    class User(
    	private var _name: String
    ) {
    	
    	val name: String
    		get() = this._name
    }
    ```
    
    1. custom setter 이용하기
    
    ```kotlin
    class User(
    	name: String
    ) {
    	
    	var name = name
    		private set
    }
    ```
    
    하지만 위 두 방법 모두 클래스에 필드(프로퍼티)가 많아질수록 번거롭다는 단점이 있다.
    
    > 지식공유자님 개인적으로는 setter를 public으로 열어 두지만, 사용하지 않는 방법을 선호
    자바 개발자라면 보통 setter 사용 지양
    어떻게 보면 Trade-Off의 영역, 팀 컨벤션을 잘 맞춘 다면 setter를 열어줘도 된다.
    > 
    
    1. **생성자 안의 프로퍼티. 클래스 body 안의 프로퍼티**
    
    ```kotlin
    @Entity
    class User(
    	var name: String,
    	
    	val age: Int?,
    
    	@OneToMany(mappedBy = "user", cascade = [CascadeType.ALL],
    		orphanRemoval = true)
    	val userLoanHistories: MutableList<UserLoanHistory> = mutableListOf(),
    
    	@Id
    	@GeneratedValue(strategy = GenerationType.IDENTITY)
    	val id: Long? = null,
    )
    ```
    
    꼭 primary constructor 안에 모든 프로퍼티를 넣어야 할까?
    
    ```kotlin
    @Entity
    class User(
    	var name: String,
    	
    	val age: Int?,
    ) {
    
    	@OneToMany(mappedBy = "user", cascade = [CascadeType.ALL],
    		orphanRemoval = true)
    	val userLoanHistories: MutableList<UserLoanHistory> = mutableListOf()
    
    	@Id
    	@GeneratedValue(strategy = GenerationType.IDENTITY)
    	val id: Long? = null,
    ```
    
    위 처럼 만들면 생성자는 name, age만 갖게 되고 실제 body 안에 userLoanHistories와 id 가 들어가게 할 수 있다. 단지 User를 만드는 과정에서 userLoanHistories를 바로 넣어줄 수 없게 된다. 위 예시코드를 포함하고 있는 프로젝트에서는 비즈니스 로직상 유저가 따로 생기고 생겨있는 유저에 대해서 대출현황(userLoanHistories)을 넣어주는 코드가 별도로 존재하기 때문에 body에 넣어도 무방하다.
    
    > 1. 모든 프로퍼티를 생성자에 넣는다.
    2. 프로퍼티를 생성자 혹은 클래스 body 안에 구분해서 넣을 때 명확한 기준이 있다.
    ?그냥 1번 방법을 사용하면 크게 생각하지 않아도 될 것 같다.
    > 
    
    1. **JPA와 data class**
        
        data class는 equals, hashCode, toString 등의 함수들을 자동으로 만들어주는 역할
        
        근데 Entity는 data class를 피하는 것이 좋다. 왜냐하면 equals, hashCode, toString 모두 JPA Entity와는 100% 어울리지 않는 메소드라서
        
        ex. User와 UserLoanHistory의 관계가 1:N 일 때
        
        User의 equals 가 호출된다면, User의 equals가 UserLoanHistory의 equals를 부르게 되고 UserLoanHistory도 User가 있으므로 user의 equals 를 부르게 되어 서로 호출하다가 stackOverFlow가 터질 수 있고, 
        
    
    <aside>
    💡 **TIP**
    Entity가 생성되는 로직을 추적하고 싶다면, constructor 지시어를 명시적으로 작성하자!
    
    </aside>
    
- **Kotlin과 JPA를 함께 사용할 때 추가적으로 고려해야 할 내용 (@ManyToOne Lazy Fetching 적용 방법)**
    
    build.gradle에 아래와 같은 스크립트를 추가해주시면 됩니다!!
    
    ```java
    plugins {
      id "org.jetbrains.kotlin.plugin.allopen" version "1.6.21"
    }
    
    // plugins, dependencies와 같은 Level (즉 build.gradle 최상단)
    allOpen {
      annotation("javax.persistence.Entity")
      annotation("javax.persistence.MappedSuperclass")
      annotation("javax.persistence.Embeddable")
    }
    ```
    
    그 이유는 다음과 같습니다!
    
    아시다시피, Kotlin은 기본적으로 Class도 final, 함수도 final입니다!! (즉, 상속과 오버라이드를 막아두었습니다!)
    
    하지만 JPA를 사용할 때 Proxy Lazy Fetching을 완전히 이용하려면  클래스가 상속 가능해야 합니다!! 제가 확인해본 바로는 @OneToMany에 있어서는 Lazy Fetching이 동작하지만 @ManyToOne에 대해서는 Lazy Fetching 옵션을 명시적으로 주더라도 동작하지 않았습니다.
    
    그래서 all-open 기능을 통해 @Entity 클래스들은 Decompile을 했을 때도 class가 열려 있게끔 처리해주어야 하고, 위의 스크립트가 바로 그런 역할을 수행하게 됩니다.
</details>

<details>
  <summary>24.01.23 화 (15~20강)</summary>
  <!-- 내용 -->
  리포지토리, 서비스, DTO, 컨트롤러를 코틀린으로 리팩토링

```kotlin
interface UserRepository : JpaRepository<User, Long> {

    fun findByName(name: String): User? // java 에서 Optional<User>
}
```

```kotlin
@Transactional
fun saveUser(request: UserCreateRequest) {
    val newUser = User(request.name, request.age)
    // User 생성자에 디폴트 파라미터가 들어가있기 때문에 Java에서 처럼 null 등으로 값을 넣어줄 필요가 없다.
    userRepository.save(newUser)
}
```

```kotlin
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
```

```kotlin
@GetMapping("/user")
fun getUsers(): List<UserResponse> = userService.getUsers() // 이런 형태도 사용 가능; 이 형태가 더 좋다라는 것은 아님

@DeleteMapping("/user")
fun deleteUser(@RequestParam name: String) { // 만약 name이 nullable 하려면(required = false) 'String?' 을 쓰면 스프링이 인식한다
    userService.deleteUser(name)
}
```

코틀린에서 할 수 있는 리팩토링 예시(util 파일 활용)

```kotlin
@Transactional
fun updateUserName(request: UserUpdateRequest) {
    val user = userRepository.findByIdOrThrow(request.id) ?: fail() // findByIdOrThrow : CrudRepository 의 확장 함수를 만듦 <- findByIdOrNull(id) : springframework 에서 코틀린을 위해 제공해주는 메서드
    user.updateName(request.name)
}
```

```kotlin
package com.group.libraryapp.util

import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.findByIdOrNull

// exception 공통 처리 부분(throw IllegalArgumentException())을 메서드로 리팩토링
fun fail(): Nothing {
    throw IllegalArgumentException()
}

fun <T, ID> CrudRepository<T, ID>.findByIdOrThrow(id: ID): T {
    return this.findByIdOrNull(id) ?: fail()
}
// CrudRepository 의 확장 함수를 커스텀
// null 로 찾아왔을 때 exception 을 발생 시키는 메서드
```

```kotlin
implementation 'com.fasterxml.jackson.module.jakson-module-kotlin:2.13.3'
// 코틀린에서도 자바에서처럼 json을 객체로 mapping 할 수 있게 해주는 의존성 (없으면 parsing 에러)
```
</details>

<details>
  <summary><b>24.01.24 수 (21강)</b></summary>
  <!-- 내용 -->
  추가 요구 사항 : 책의 분야 추가하기

```java
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

////////////////////////////////
// test 클래스
Book.fixture("이상한 나라의 앨리스")
```
</details>

<details>
  <summary><b>24.01.26 금 (22~24강)</b></summary>
  <!-- 내용 -->
  **Enum Class를 활용해 책의 분야 리팩토링 하기**

기존에 Book 클래스에 type 필드를 String 타입으로 만들었는데, 이 때 생기는 문제점이 몇가지 있다.

1. 요청을 검증하고 있지 않아 type으로 받을 값이 아니어도 들어온다. 검증을 추가할 수 있지만 번거롭다.
2. 코드만 보았을 때, DB 테이블에 실제로 어떤 값이 들어가는지 알 수 없다. 
3. type과 관련된 새로운 로직을 작성할 때 번거롭다.
    1. 예를 들어, 책을 대출할 때마다 분야별로 ‘이벤트 점수’를 준다면? when 절을 이용해서 분기처리 해야된다..
        
        ```kotlin
        fun getEventScore(): Int {
        	return when (type) {
        		"COMPUTER" -> 10
        		"ECONOMY" -> 8
        		"SOCIETY", "LANGUAGE", "SCIENCE" -> 5
        		// 코드에 분기가 들어가고
        		else -> throw IllegalArgumentException("잘못된 타입입니다")
        		// 실행되지 않을 else문이 존재
        	}	
        }
        
        // 문자열 타이핑은 실수할 여지가 많고
        // 새로운 type이 생기는 경우 로직 추가를 놓칠 수 있다.
        ```
        

→ Enum Class를 만들어 해결

```kotlin
enum class BookType(val score: Int) {
	COMPUTER(10),
	ECONOMY(8),
	SOCIETY(5),
	LANGUAGE(5),
	SCIENCE(5),
}
```

```kotlin
fun getEventScore(): Int {
	return type.score
}
```

but, 이렇게만 만들어 놓으면 DB에 숫자로 저장이 된다.

이 때 생기는 문제점은

1. 기존 Enum의 순서가 바뀌면 안된다.
2. 기존 Enum을 삭제하고 새로운 Enum 타입을 추가하는 것이 제한적이다.

→ DB에도 숫자가 아닌 문자열로 들어가게 하기 위해서 엔티티에서 해당 프로퍼티에 어노테이션을 달아줘야 한다.

```kotlin
@Enumerated(EnumType.STRING)
val type: BookType,
```

**Boolean에도 Enum 활용하기**

예를 들어 User 테이블에 유저의 휴면 여부를 파악하기 위해 유저의 활성 여부를 isActive 라는 Boolean 타입의 프로퍼티로 추가했다고 가정해보자. 이 때는 휴면 여부에 따라 yes or no 로 명확하므로 문제가 없지만, 다음과 같은 추가 요구 사항이 생겼다고 생각하자.

‘유저의 탈퇴 여부를 soft 하게 관리해주세요 : 탈퇴는 휴면을 해제하여 로그인 한 후 이루어진다.’ 

*soft : 실제 DB에는 데이터가 남아있지만, 시스템 상으로는 삭제된 것처럼 관리하는 방식

그래서 isActive가 아닌 또 다른 프로퍼티인 isDeleted라는 Boolean 타입의 프로퍼티를 추가할 수 있다.

이렇게 하나의 테이블에 서로 영향을 끼치는 Boolean이 2개가 되면 문제가 생긴다.

1. Boolean이 2개가 있기 때문에 코드를 이해하기 어려워진다.
    - 한 객체가 여러 상태를 표현할 수록 이해하기 어렵다.
    - 현재 경우의 수는 2^2, 즉 4가지이다.
    - 4가지도 충분히 어렵지만, 여기서 Boolean 이 더 늘어나면 경우의 수는 기하급수적으로 늘어난다.
2. Boolean 2개로 표현되는 4가지 상태가 모두 유의미하지 않다.
    - (isActive, isDeleted)는 총 4가지 경우가 있다.
        - (false, false) - 휴면 상태인 유저
        - (false, true) - 휴면이면서 탈퇴한 유저; 이런 상태는 불가능 하다.
        - (true, false) - 휴면이 아닌 활성화된 유저이다.
        - (true, true) - 탈퇴한 유저이다.
    
    → 실제로 불가능한 상태이지만 코드 상에서는 가능해서 유지 보수를 어렵게 만든다.
    

→ Enum을 써서 서로 관련된 Boolean 파라미터를 한 번에 관리하는 것으로 해결

```kotlin
enum class UserStatus {
    ACTIVE,
    IN_ACTIVE,
    DELETED,
}
```

1. 필드 1개로 여러 상태를 표현할 수 있기 때문에 코드의 이해가 쉬워진다.
2. 정확하게 유의미한 상태만 나타낼 수 있기 때문에 코드의 유지보수가 용이해진다.
</details>
