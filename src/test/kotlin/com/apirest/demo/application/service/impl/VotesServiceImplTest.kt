package com.apirest.demo.application.service.impl

import com.apirest.demo.application.builders.AssociateBuilder
import com.apirest.demo.application.builders.SessionBuilder
import com.apirest.demo.application.builders.VotesBuilder
import com.apirest.demo.application.dto.VotesRequestDTO
import com.apirest.demo.application.repository.VotesRepository
import com.apirest.demo.application.restClient.ValidateCpfWebClient
import com.apirest.demo.application.service.AssociateService
import com.apirest.demo.application.service.SessionService
import com.apirest.demo.application.service.VotesService
import com.apirest.demo.application.vo.ValidateCpfResponseVo
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
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
    private lateinit var associateService: AssociateService

    @Mock
    private lateinit var sessionService: SessionService

    @Mock
    private lateinit var validateCpfWebClient: ValidateCpfWebClient

    @BeforeEach
    fun setup() {
        votesService = VotesServiceImpl(associateService, sessionService, votesRepository, validateCpfWebClient)
    }

    @Test
    fun `should return Votes by idAgenda and IdAssociate vote true`() {
        val votesFindByIdAgendaAndIdAssociate =
            VotesBuilder.builder().idAssociate("idAssociate").idAgenda("idAgenda").vote(true).build()

        Mockito.`when`(votesRepository.findByIdAgendaAndIdAssociate(anyString(), anyString()))
            .thenReturn(Mono.just(votesFindByIdAgendaAndIdAssociate))

        val votes = votesService.findByIdAgendaAndIdAssociate("idAgenda", "idAssociate")

        StepVerifier
            .create(votes)
            .consumeNextWith {
                assertEquals(
                    it.getIdAgenda(),
                    votesFindByIdAgendaAndIdAssociate.getIdAgenda()
                )
                assertEquals(
                    it.getIdAssociate(),
                    votesFindByIdAgendaAndIdAssociate.getIdAssociate()
                )
                assertEquals(
                    it.getVote(),
                    votesFindByIdAgendaAndIdAssociate.getVote()
                )
            }
            .verifyComplete()
    }

    @Test
    fun `should return Votes by idAgenda and IdAssociate vote false`() {
        val votesFindByIdAgendaAndIdAssociate =
            VotesBuilder.builder().idAssociate("idAssociate").idAgenda("idAgenda").vote(false).build()

        Mockito.`when`(votesRepository.findByIdAgendaAndIdAssociate(anyString(), anyString()))
            .thenReturn(Mono.just(votesFindByIdAgendaAndIdAssociate))

        val votes = votesService.findByIdAgendaAndIdAssociate("idAgenda", "idAssociate")

        StepVerifier
            .create(votes)
            .consumeNextWith {
                assertEquals(
                    it.getIdAgenda(),
                    votesFindByIdAgendaAndIdAssociate.getIdAgenda()
                )
                assertEquals(
                    it.getIdAssociate(),
                    votesFindByIdAgendaAndIdAssociate.getIdAssociate()
                )
                assertEquals(
                    it.getVote(),
                    votesFindByIdAgendaAndIdAssociate.getVote()
                )
            }
            .verifyComplete()
    }

    @Test
    fun `should save thrown error if associate not found`() {
        Mockito.`when`(associateService.findById(anyString()))
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

        Mockito.`when`(associateService.findById(anyString()))
            .thenReturn(Mono.just(associate))
        Mockito.`when`(sessionService.findByIdAgenda(anyString()))
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
        Mockito.`when`(associateService.findById(anyString()))
            .thenReturn(Mono.just(associate))
        Mockito.`when`(sessionService.findByIdAgenda(anyString()))
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
        Mockito.`when`(associateService.findById(anyString()))
            .thenReturn(Mono.just(associate))
        Mockito.`when`(sessionService.findByIdAgenda(anyString()))
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

        Mockito.`when`(associateService.findById(anyString()))
            .thenReturn(Mono.just(associate))
        Mockito.`when`(sessionService.findByIdAgenda(anyString()))
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

        Mockito.`when`(associateService.findById(anyString()))
            .thenReturn(Mono.just(associate))
        Mockito.`when`(sessionService.findByIdAgenda(anyString()))
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

        Mockito.`when`(associateService.findById(anyString()))
            .thenReturn(Mono.just(associate))
        Mockito.`when`(sessionService.findByIdAgenda(anyString()))
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