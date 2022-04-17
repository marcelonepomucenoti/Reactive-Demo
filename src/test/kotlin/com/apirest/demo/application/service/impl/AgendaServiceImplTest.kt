package com.apirest.demo.application.service.impl

import com.apirest.demo.application.builders.AgendaBuilder
import com.apirest.demo.application.builders.SessionBuilder
import com.apirest.demo.application.dto.AgendaRequestDTO
import com.apirest.demo.application.repository.AgendaRepository
import com.apirest.demo.application.repository.SessionRepository
import com.apirest.demo.application.repository.VotesRepository
import com.apirest.demo.application.service.AgendaService
import com.apirest.demo.application.service.MensageriaService
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
import java.util.*

@SpringBootTest
internal class AgendaServiceImplTest {

    lateinit var agendaService: AgendaService

    @Mock
    private lateinit var agendaRepository: AgendaRepository

    @Mock
    private lateinit var votesRepository: VotesRepository

    @Mock
    private lateinit var sessionRepository: SessionRepository

    @Mock
    private lateinit var mensageriaService: MensageriaService

    @BeforeEach
    fun setup() {
        agendaService = AgendaServiceImpl(agendaRepository, sessionRepository, votesRepository, mensageriaService)
    }

    @Test
    fun `should return all agenda`() {
        val agendaFindAll = AgendaBuilder.builder().name("findAll").build()
        agendaFindAll.setId("IdAgenda")

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
        agendaFindById.setId("IdAgenda")

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
    fun `should save agenda`() {
        val agendaSave = AgendaBuilder.builder().name("saveAgenda").build()
        agendaSave.setId("IdAgenda")
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

    @Test
    fun `should thrown exception on calculate result if Agenda has already been accounted for`() {
        val agendaFindById = AgendaBuilder.builder().name("saveAgenda").build()
        agendaFindById.setIsAccountedAgenda(true)
        `when`(agendaRepository.findById(anyString())).thenReturn(Mono.just(agendaFindById))

        val agenda = agendaService.calculateResult("IdAgenda")

        StepVerifier
            .create(agenda)
            .expectError()
            .verify()
    }

    @Test
    fun `should thrown exception on calculate result if Session not opened for this agenda`() {
        val agendaFindById = AgendaBuilder.builder().name("saveAgenda").build()
        agendaFindById.setIsAccountedAgenda(false)
        `when`(agendaRepository.findById(anyString())).thenReturn(Mono.just(agendaFindById))
        `when`(sessionRepository.findByIdAgenda(anyString())).thenReturn(Mono.empty())

        val agenda = agendaService.calculateResult("IdAgenda")

        StepVerifier
            .create(agenda)
            .expectError()
            .verify()
    }

    @Test
    fun `should thrown exception on calculate result if Agenda can still receive votes`() {
        val date = Date()
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.HOUR, 5)
        val dateValidity = calendar.time

        val agendaFindById = AgendaBuilder.builder().name("saveAgenda").build()
        agendaFindById.setIsAccountedAgenda(false)
        val session = SessionBuilder.builder().idAgenda("idAgenda").nameAgenda("nameAgenda").validity(dateValidity).build()
        `when`(agendaRepository.findById(anyString())).thenReturn(Mono.just(agendaFindById))
        `when`(sessionRepository.findByIdAgenda(anyString())).thenReturn(Mono.just(session))

        val agenda = agendaService.calculateResult("IdAgenda")

        StepVerifier
            .create(agenda)
            .expectError()
            .verify()
    }
}