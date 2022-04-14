package com.apirest.demo.application.builders

import com.apirest.demo.application.entity.Associate
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class AssociateBuilderTest{

    @Test
    fun `should return associate with correct name and cpf`() {
        val associate = Associate("associate1", "11111111111")
        val associateWithBuilder = AssociateBuilder.builder().name("associate1").cpf("11111111111").build()
        MatcherAssert.assertThat(associate.getName(), Matchers.`is`(associateWithBuilder.getName()))
        MatcherAssert.assertThat(associate.getCpf(), Matchers.`is`(associateWithBuilder.getCpf()))
    }

    @Test
    fun `should throw exception when associate dont have name`() {
        Assertions.assertThrows(UninitializedPropertyAccessException::class.java) {
            AssociateBuilder.builder().cpf("cpf").build()
        }
    }

    @Test
    fun `should throw exception when associate dont have cpf`() {
        Assertions.assertThrows(UninitializedPropertyAccessException::class.java) {
            AssociateBuilder.builder().name("name").build()
        }
    }
}