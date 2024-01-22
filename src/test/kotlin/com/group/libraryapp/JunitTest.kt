package com.group.libraryapp

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class JunitTest {

    companion object {
        @BeforeAll // 모든 테스트를 수행하기 전에 최초 1회 수행되는 메소드를 지정
        @JvmStatic // BeforeAll, AfterAll에는 이 어노테이션을 붙여야 한다
        fun beforeAll() {
            println("모든 테스트 시작 전 실행")
        }

        @AfterAll // 모든 테스트를 수행한 후 최후 1회 수행되는 메소드를 지정
        @JvmStatic
        fun afterAll() {
            println("모든 테스트 종료 후 실행")
        }
    }

    @BeforeEach
    fun beforeEach() {
        println("각 테스트 시작 전 실행")
    }

    @AfterEach
    fun afterEach() {
        println("각 테스트 종료 후 실행")
    }

    @Test
    fun test1() {
        println("테스트 1")
    }

    @Test
    fun test2() {
        println("테스트 2")
    }
}