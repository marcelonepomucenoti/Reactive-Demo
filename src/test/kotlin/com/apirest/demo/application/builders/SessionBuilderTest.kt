package com.apirest.demo.application.builders

import com.apirest.demo.application.entity.Session
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
internal class SessionBuilderTest{
    @Test
    fun `should return session with correct idAgenda and nameAgenda and validity`() {
        val dateValidity = Date()
        val session = Session("idAgenda", "nameAgenda", dateValidity)
        val sessionWithBuilder = SessionBuilder.builder().idAgenda("idAgenda").nameAgenda("nameAgenda").validity(dateValidity).build()
        MatcherAssert.assertThat(session.getIdAgenda(), Matchers.`is`(sessionWithBuilder.getIdAgenda()))
        MatcherAssert.assertThat(session.getNameAgenda(), Matchers.`is`(sessionWithBuilder.getNameAgenda()))
        MatcherAssert.assertThat(session.getValidity(), Matchers.`is`(sessionWithBuilder.getValidity()))
    }

    @Test
    fun `should throw exception when session dont have nameAgenda`() {
        Assertions.assertThrows(UninitializedPropertyAccessException::class.java) {
            SessionBuilder.builder().idAgenda("idAgenda").build()
        }
    }

    @Test
    fun `should throw exception when session dont have idagenda`() {
        Assertions.assertThrows(UninitializedPropertyAccessException::class.java) {
            SessionBuilder.builder().nameAgenda("nameAgenda").build()
        }
    }

    @Test
    fun `should return session with correct idAgenda and nameAgenda and null validity`() {
        val session = Session("idAgenda", "nameAgenda", Date())
        val sessionWithBuilder = SessionBuilder.builder().idAgenda("idAgenda").nameAgenda("nameAgenda").build()
        MatcherAssert.assertThat(session.getIdAgenda(), Matchers.`is`(sessionWithBuilder.getIdAgenda()))
        MatcherAssert.assertThat(session.getNameAgenda(), Matchers.`is`(sessionWithBuilder.getNameAgenda()))
        assertNotNull(sessionWithBuilder.getValidity())
    }
}