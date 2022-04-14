package com.apirest.demo.application.service.impl

import com.apirest.demo.application.builders.AssociateBuilder
import com.apirest.demo.application.dto.AssociateRequestDTO
import com.apirest.demo.application.repository.AssociateRepository
import com.apirest.demo.application.service.AssociateService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@SpringBootTest
internal class AssociateServiceImplTest {
    lateinit var associateService: AssociateService

    @Mock
    private lateinit var associateRepository: AssociateRepository

    @BeforeEach
    fun setup() {
        associateService = AssociateServiceImpl(associateRepository)
    }

    @Test
    fun `should return associate by id`() {
        val associateFindById = AssociateBuilder.builder().name("name").cpf("cpf").build()
        Mockito.`when`(associateRepository.findById(ArgumentMatchers.anyString()))
            .thenReturn(Mono.just(associateFindById))

        val associate = associateService.findById("id")

        StepVerifier
            .create(associate)
            .consumeNextWith {
                Assertions.assertEquals(
                    it.getName(),
                    "name"
                )
                Assertions.assertEquals(
                    it.getCpf(),
                    "cpf"
                )
            }
            .verifyComplete()
    }

    @Test
    fun `should return associate by cpf`() {
        val associateFindById = AssociateBuilder.builder().name("name").cpf("cpf").build()
        Mockito.`when`(associateRepository.findByCpf(ArgumentMatchers.anyString()))
            .thenReturn(Mono.just(associateFindById))

        val associate = associateService.findByCpf("cpf")

        StepVerifier
            .create(associate)
            .consumeNextWith {
                Assertions.assertEquals(
                    it.getCpf(),
                    "cpf"
                )
                Assertions.assertEquals(
                    it.getCpf(),
                    "cpf"
                )
            }
            .verifyComplete()
    }

    @Test
    fun `should save new associate`() {
        val associateSave = AssociateBuilder.builder().name("name").cpf("cpf").build()
        Mockito.`when`(associateRepository.findByCpf(ArgumentMatchers.anyString()))
            .thenReturn(Mono.empty())
        Mockito.`when`(associateRepository.save(any()))
            .thenReturn(Mono.just(associateSave))

        val associateRequestDTO = AssociateRequestDTO("name", "cpf")

        val associate = associateService.save(associateRequestDTO)

        StepVerifier
            .create(associate)
            .consumeNextWith {
                Assertions.assertEquals(
                    it.getCpf(),
                    "cpf"
                )
                Assertions.assertEquals(
                    it.getCpf(),
                    "cpf"
                )
            }
            .verifyComplete()
    }

    @Test
    fun `should  thrown exception if associate exist`() {
        val associateSave = AssociateBuilder.builder().name("name").cpf("cpf").build()
        Mockito.`when`(associateRepository.findByCpf(ArgumentMatchers.anyString()))
            .thenReturn(Mono.just(associateSave))

        val associateRequestDTO = AssociateRequestDTO("name", "cpf")

        val associate = associateService.save(associateRequestDTO)

        StepVerifier
            .create(associate)
            .expectError()
            .verify()
    }
}