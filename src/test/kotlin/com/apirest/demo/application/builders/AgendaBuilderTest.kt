package com.apirest.demo.application.builders

import com.apirest.demo.application.entity.Agenda
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class AgendaBuilderTest {

    @Test
    fun `should return agenda with correct name`() {
        val agenda = Agenda("test")
        val agendaWithBuilder = AgendaBuilder.builder().name("test").build()
        MatcherAssert.assertThat(agenda.getName(), Matchers.`is`(agendaWithBuilder.getName()))
    }

    @Test
    fun `should throw exception when agenda dont have name`() {
        Assertions.assertThrows(UninitializedPropertyAccessException::class.java) {
            AgendaBuilder.builder().build()
        }
    }
}