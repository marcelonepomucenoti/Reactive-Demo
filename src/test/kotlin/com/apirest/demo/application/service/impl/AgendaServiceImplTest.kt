package com.apirest.demo.application.service.impl

import com.apirest.demo.application.builders.AgendaBuilder
import com.apirest.demo.application.dto.AgendaRequestDTO
import com.apirest.demo.application.repository.AgendaRepository
import com.apirest.demo.application.service.AgendaService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.springframework.boot.test.context.SpringBootTest
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@SpringBootTest
internal class AgendaServiceImplTest {

    lateinit var agendaService: AgendaService

    @Mock
    private lateinit var agendaRepository: AgendaRepository

    @BeforeEach
    fun setup() {
        agendaService = AgendaServiceImpl(agendaRepository)
    }

    @Test
    fun `should return all agenda`() {
        val agendaFindAll = AgendaBuilder.builder().name("findAll").build()
        `when`(agendaRepository.findAll()).thenReturn(Flux.just(agendaFindAll))

        val agenda = agendaService.findAll()

        StepVerifier
            .create(agenda)
            .consumeNextWith {
                assertEquals(
                    it.getName(),
                    "findAll"
                )
            }
            .verifyComplete()
    }

    @Test
    fun `should return agenda by id`() {
        val agendaFindById = AgendaBuilder.builder().name("findById").build()
        `when`(agendaRepository.findById(anyString())).thenReturn(Mono.just(agendaFindById))

        val agenda = agendaService.findById("id")

        StepVerifier
            .create(agenda)
            .consumeNextWith {
                assertEquals(
                    it.getName(),
                    "findById"
                )
            }
            .verifyComplete()
    }

    @Test
    fun `should return agenda by name`() {
        val agendaFindByName = AgendaBuilder.builder().name("findByName").build()
        `when`(agendaRepository.findByName(anyString())).thenReturn(Mono.just(agendaFindByName))

        val agenda = agendaService.findByName("name")

        StepVerifier
            .create(agenda)
            .consumeNextWith {
                assertEquals(
                    it.getName(),
                    "findByName"
                )
            }
            .verifyComplete()
    }

    @Test
    fun `should save agenda`() {
        val agendaSave = AgendaBuilder.builder().name("saveAgenda").build()
        `when`(agendaRepository.save(any())).thenReturn(Mono.just(agendaSave))
        `when`(agendaRepository.findByName(anyString())).thenReturn(Mono.empty())

        val agendaRequestDTO = AgendaRequestDTO("saveAgenda")

        val agenda = agendaService.save(agendaRequestDTO)

        StepVerifier
            .create(agenda)
            .consumeNextWith {
                assertEquals(
                    it.getName(),
                    "saveAgenda"
                )
            }
            .verifyComplete()
    }

    @Test
    fun `should thrown exception if agenda exist`() {
        val agendaSave = AgendaBuilder.builder().name("saveAgenda").build()
        `when`(agendaRepository.findByName(anyString())).thenReturn(Mono.just(agendaSave))

        val agendaRequestDTO = AgendaRequestDTO("saveAgenda")

        val agenda = agendaService.save(agendaRequestDTO)

        StepVerifier
            .create(agenda)
            .expectError()
            .verify()
    }
}