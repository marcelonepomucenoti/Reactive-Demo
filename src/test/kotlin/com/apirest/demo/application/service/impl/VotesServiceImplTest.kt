package com.apirest.demo.application.service.impl

import com.apirest.demo.application.builders.AssociateBuilder
import com.apirest.demo.application.builders.SessionBuilder
import com.apirest.demo.application.builders.VotesBuilder
import com.apirest.demo.application.dto.VotesRequestDTO
import com.apirest.demo.application.repository.AssociateRepository
import com.apirest.demo.application.repository.SessionRepository
import com.apirest.demo.application.repository.VotesRepository
import com.apirest.demo.application.restClient.ValidateCpfWebClient
import com.apirest.demo.application.service.MensageriaService
import com.apirest.demo.application.service.VotesService
import com.apirest.demo.application.vo.ValidateCpfResponseVo
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.*
import org.mockito.Mock
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.util.*

@SpringBootTest
internal class VotesServiceImplTest {

    lateinit var votesService: VotesService

    @Mock
    private lateinit var votesRepository: VotesRepository

    @Mock
    private lateinit var associateRepository: AssociateRepository

    @Mock
    private lateinit var sessionRespository: SessionRepository

    @Mock
    private lateinit var validateCpfWebClient: ValidateCpfWebClient

    @Mock
    private lateinit var mensageriaService: MensageriaService

    @BeforeEach
    fun setup() {
        votesService = VotesServiceImpl(associateRepository, sessionRespository, votesRepository, validateCpfWebClient, mensageriaService)
    }

    @Test
    fun `should save thrown error if associate not found`() {
        Mockito.`when`(associateRepository.findById(anyString()))
            .thenReturn(Mono.empty())

        val votesRequestDTO = VotesRequestDTO("idAssociate", "idAgenda", true)

        val votes = votesService.save(votesRequestDTO)

        StepVerifier
            .create(votes)
            .expectError()
            .verify()
    }

    @Test
    fun `should save thrown error if session not found`() {
        val associate = AssociateBuilder.builder().name("name").cpf("cpf").build()

        Mockito.`when`(associateRepository.findById(anyString()))
            .thenReturn(Mono.just(associate))
        Mockito.`when`(sessionRespository.findByIdAgenda(anyString()))
            .thenReturn(Mono.empty())

        val votesRequestDTO = VotesRequestDTO("idAssociate", "idAgenda", true)

        val votes = votesService.save(votesRequestDTO)

        StepVerifier
            .create(votes)
            .expectError()
            .verify()
    }

    @Test
    fun `should save thrown error if voting is closed`() {
        val date = Date()
        val associate = AssociateBuilder.builder().name("name").cpf("cpf").build()
        val session = SessionBuilder.builder().idAgenda("idAgenda").nameAgenda("nameAgenda").validity(date).build()
        Mockito.`when`(associateRepository.findById(anyString()))
            .thenReturn(Mono.just(associate))
        Mockito.`when`(sessionRespository.findByIdAgenda(anyString()))
            .thenReturn(Mono.just(session))

        Mockito.`when`(validateCpfWebClient.validateCpf(anyString()))
            .thenThrow(ResponseStatusException(HttpStatus.NOT_FOUND))

        val votesRequestDTO = VotesRequestDTO("idAssociate", "idAgenda", true)

        val votes = votesService.save(votesRequestDTO)

        StepVerifier
            .create(votes)
            .expectError()
            .verify()
    }

    @Test
    fun `should save thrown error if cpf is invalid`() {
        val associate = AssociateBuilder.builder().name("name").cpf("cpf").build()
        val session = SessionBuilder.builder().idAgenda("idAgenda").nameAgenda("nameAgenda").build()
        Mockito.`when`(associateRepository.findById(anyString()))
            .thenReturn(Mono.just(associate))
        Mockito.`when`(sessionRespository.findByIdAgenda(anyString()))
            .thenReturn(Mono.just(session))
        Mockito.`when`(votesRepository.findByIdAgendaAndIdAssociate(anyString(), anyString()))
            .thenReturn(Mono.empty())
        Mockito.`when`(validateCpfWebClient.validateCpf(anyString()))
            .thenThrow(ResponseStatusException(HttpStatus.NOT_FOUND))

        val votesRequestDTO = VotesRequestDTO("idAssociate", "idAgenda", true)

        val votes = votesService.save(votesRequestDTO)

        StepVerifier
            .create(votes)
            .expectError()
            .verify()
    }

    @Test
    fun `should save thrown error if votes already exist`() {
        val associate = AssociateBuilder.builder().name("name").cpf("cpf").build()
        val session = SessionBuilder.builder().idAgenda("idAgenda").nameAgenda("nameAgenda").build()
        val votesFindByIdAgendaAndIdAssociate =
            VotesBuilder.builder().vote(true).idAgenda("idAgenda").idAssociate("idAssociate").build()

        Mockito.`when`(associateRepository.findById(anyString()))
            .thenReturn(Mono.just(associate))
        Mockito.`when`(sessionRespository.findByIdAgenda(anyString()))
            .thenReturn(Mono.just(session))
        Mockito.`when`(votesRepository.findByIdAgendaAndIdAssociate(anyString(), anyString()))
            .thenReturn(Mono.just(votesFindByIdAgendaAndIdAssociate))

        val votesRequestDTO = VotesRequestDTO("idAssociate", "idAgenda", true)

        val votes = votesService.save(votesRequestDTO)

        StepVerifier
            .create(votes)
            .expectError()
            .verify()
    }

    @Test
    fun `should save thrown erro if Unable to vote`() {
        val associate = AssociateBuilder.builder().name("name").cpf("cpf").build()
        val session = SessionBuilder.builder().idAgenda("idAgenda").nameAgenda("nameAgenda").build()
        val validateCpfResponseVo = ValidateCpfResponseVo("UNABLE_TO_VOTE")

        Mockito.`when`(associateRepository.findById(anyString()))
            .thenReturn(Mono.just(associate))
        Mockito.`when`(sessionRespository.findByIdAgenda(anyString()))
            .thenReturn(Mono.just(session))
        Mockito.`when`(votesRepository.findByIdAgendaAndIdAssociate(anyString(), anyString()))
            .thenReturn(Mono.empty())
        Mockito.`when`(validateCpfWebClient.validateCpf(anyString()))
            .thenReturn(Mono.just(validateCpfResponseVo))

        val votesRequestDTO = VotesRequestDTO("idAssociate", "idAgenda", true)

        val votes = votesService.save(votesRequestDTO)

        StepVerifier
            .create(votes)
            .expectError()
            .verify()
    }

    @Test
    fun `should save`() {
        val associate = AssociateBuilder.builder().name("name").cpf("cpf").build()
        val session = SessionBuilder.builder().idAgenda("idAgenda").nameAgenda("nameAgenda").build()
        val validateCpfResponseVo = ValidateCpfResponseVo("ABLE_TO_VOTE")
        val votesSave = VotesBuilder.builder().idAgenda("idAgenda").vote(true).idAssociate("idAssociate").build()

        Mockito.`when`(associateRepository.findById(anyString()))
            .thenReturn(Mono.just(associate))
        Mockito.`when`(sessionRespository.findByIdAgenda(anyString()))
            .thenReturn(Mono.just(session))
        Mockito.`when`(votesRepository.findByIdAgendaAndIdAssociate(anyString(), anyString()))
            .thenReturn(Mono.empty())
        Mockito.`when`(validateCpfWebClient.validateCpf(anyString()))
            .thenReturn(Mono.just(validateCpfResponseVo))
        Mockito.`when`(votesRepository.save(any()))
            .thenReturn(Mono.just(votesSave))

        val votesRequestDTO = VotesRequestDTO("idAssociate", "idAgenda", true)

        val votes = votesService.save(votesRequestDTO)

        StepVerifier
            .create(votes)
            .consumeNextWith {
                assertEquals(
                    it.getIdAgenda(),
                    votesSave.getIdAgenda()
                )
                assertEquals(
                    it.getIdAssociate(),
                    votesSave.getIdAssociate()
                )
                assertEquals(
                    it.getVote(),
                    votesSave.getVote()
                )
            }
            .verifyComplete()
    }
}