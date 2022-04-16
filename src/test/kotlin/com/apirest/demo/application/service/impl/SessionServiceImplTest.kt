package com.apirest.demo.application.service.impl

import com.apirest.demo.application.builders.SessionBuilder
import com.apirest.demo.application.dto.SessionRequestDTO
import com.apirest.demo.application.entity.Agenda
import com.apirest.demo.application.entity.Session
import com.apirest.demo.application.repository.AgendaRepository
import com.apirest.demo.application.repository.SessionRepository
import com.apirest.demo.application.service.SessionService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.util.*

@SpringBootTest
internal class SessionServiceImplTest {
    lateinit var sessionService: SessionService

    @Mock
    private lateinit var sessionRepository: SessionRepository

    @Mock
    private lateinit var agendaRepository: AgendaRepository

    @BeforeEach
    fun setup() {
        sessionService = SessionServiceImpl(sessionRepository, agendaRepository)
    }

    @Test
    fun `should return all session`() {
        val date = Date()
        val sessionFindAll =
            SessionBuilder.builder().idAgenda("idAgenda").nameAgenda("nameAgenda").validity(date).build()
        Mockito.`when`(sessionRepository.findAll()).thenReturn(Flux.just(sessionFindAll))

        val agenda = sessionService.findAll()

        StepVerifier
            .create(agenda)
            .consumeNextWith {
                Assertions.assertEquals(
                    it.getIdAgenda(),
                    "idAgenda"
                )
                Assertions.assertEquals(
                    it.getNameAgenda(),
                    "nameAgenda"
                )
                Assertions.assertEquals(
                    it.getValidity(),
                    date
                )
            }
            .verifyComplete()
    }

    @Test
    fun `should return session by id`() {
        val date = Date()
        val sessionFindAll =
            SessionBuilder.builder().idAgenda("idAgenda").nameAgenda("nameAgenda").validity(date).build()
        Mockito.`when`(sessionRepository.findById(anyString())).thenReturn(Mono.just(sessionFindAll))

        val agenda = sessionService.findById("id")

        StepVerifier
            .create(agenda)
            .consumeNextWith {
                Assertions.assertEquals(
                    it.getIdAgenda(),
                    "idAgenda"
                )
                Assertions.assertEquals(
                    it.getNameAgenda(),
                    "nameAgenda"
                )
                Assertions.assertEquals(
                    it.getValidity(),
                    date
                )
            }
            .verifyComplete()
    }

    @Test
    fun `should return session by idAgenda`() {
        val date = Date()
        val sessionFindAll =
            SessionBuilder.builder().idAgenda("idAgenda").nameAgenda("nameAgenda").validity(date).build()
        Mockito.`when`(sessionRepository.findByIdAgenda(anyString())).thenReturn(Mono.just(sessionFindAll))

        val agenda = sessionService.findByIdAgenda("id")

        StepVerifier
            .create(agenda)
            .consumeNextWith {
                Assertions.assertEquals(
                    it.getIdAgenda(),
                    "idAgenda"
                )
                Assertions.assertEquals(
                    it.getNameAgenda(),
                    "nameAgenda"
                )
                Assertions.assertEquals(
                    it.getValidity(),
                    date
                )
            }
            .verifyComplete()
    }

    @Test
    fun `should thrown exception on try save with Agenda already has session`() {
        val date = Date()
        val sessionFindAll =
            SessionBuilder.builder().idAgenda("idAgenda").nameAgenda("nameAgenda").validity(date).build()
        Mockito.`when`(sessionRepository.findByIdAgenda(anyString())).thenReturn(Mono.just(sessionFindAll))

        val sessionRequestDTO = SessionRequestDTO("idAgenda", Date())

        val agenda = sessionService.save(sessionRequestDTO)

        StepVerifier
            .create(agenda)
            .expectError()
            .verify()
    }

    @Test
    fun `should thrown exception on try save with Unidentified agenda`() {
        Mockito.`when`(sessionRepository.findByIdAgenda(anyString())).thenReturn(Mono.empty())
        Mockito.`when`(agendaRepository.findById(anyString())).thenReturn(Mono.empty())

        val sessionRequestDTO = SessionRequestDTO("idAgenda", Date())

        val agenda = sessionService.save(sessionRequestDTO)

        StepVerifier
            .create(agenda)
            .expectError()
            .verify()
    }

    @Test
    fun `should save`() {
        val agendaFindById = Agenda("nameAgenda")
        val date = Date()
        val sessionSave = Session("idAgenda", "nameAgenda", date)

        Mockito.`when`(sessionRepository.findByIdAgenda(anyString())).thenReturn(Mono.empty())
        Mockito.`when`(agendaRepository.findById(anyString())).thenReturn(Mono.just(agendaFindById))
        Mockito.`when`(sessionRepository.save(any())).thenReturn(Mono.just(sessionSave))

        val sessionRequestDTO = SessionRequestDTO("idAgenda", date)

        val session = sessionService.save(sessionRequestDTO)

        StepVerifier
            .create(session)
            .consumeNextWith {
                Assertions.assertEquals(
                    it.getIdAgenda(),
                    sessionSave.getIdAgenda()
                )
                Assertions.assertEquals(
                    it.getNameAgenda(),
                    sessionSave.getNameAgenda()
                )
                Assertions.assertEquals(
                    it.getValidity(),
                    sessionSave.getValidity()
                )
            }
            .verifyComplete()
    }

    @Test
    fun `should save with null validity`() {
        val agendaFindById = Agenda("nameAgenda")
        val date = Date()
        val sessionSave = Session("idAgenda", "nameAgenda", date)

        Mockito.`when`(sessionRepository.findByIdAgenda(anyString())).thenReturn(Mono.empty())
        Mockito.`when`(agendaRepository.findById(anyString())).thenReturn(Mono.just(agendaFindById))
        Mockito.`when`(sessionRepository.save(any())).thenReturn(Mono.just(sessionSave))

        val sessionRequestDTO = SessionRequestDTO("idAgenda", null)

        val session = sessionService.save(sessionRequestDTO)

        StepVerifier
            .create(session)
            .consumeNextWith {
                Assertions.assertEquals(
                    it.getIdAgenda(),
                    sessionSave.getIdAgenda()
                )
                Assertions.assertEquals(
                    it.getNameAgenda(),
                    sessionSave.getNameAgenda()
                )
                assertNotNull(
                    it.getValidity(),
                )
            }
            .verifyComplete()
    }
}