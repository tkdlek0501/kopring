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