package com.apirest.demo.application.service.impl

import com.apirest.demo.application.builders.VotesBuilder
import com.apirest.demo.application.restClient.ValidateCpfWebClient
import com.apirest.demo.application.dto.VotesRequestDTO
import com.apirest.demo.application.entity.Associate
import com.apirest.demo.application.entity.Session
import com.apirest.demo.application.entity.Votes
import com.apirest.demo.application.model.PostVoteMessage
import com.apirest.demo.application.repository.VotesRepository
import com.apirest.demo.application.service.AssociateService
import com.apirest.demo.application.service.MensageriaService
import com.apirest.demo.application.service.SessionService
import com.apirest.demo.application.service.VotesService
import com.apirest.demo.application.vo.ValidateCpfResponseVo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import java.util.*

@Service
class VotesServiceImpl(
    @Autowired val associateService: AssociateService,
    @Autowired val sessionService: SessionService,
    @Autowired val votesRepository: VotesRepository,
    @Autowired val validateCpfWebClient: ValidateCpfWebClient,
    @Autowired val mensageriaService: MensageriaService
) : VotesService {

    override fun findByIdAgendaAndIdAssociate(idAgenda: String, idAssociate: String): Mono<Votes> {
        return votesRepository.findByIdAgendaAndIdAssociate(idAgenda, idAssociate)
    }

    override fun save(votesRequestDTO: VotesRequestDTO): Mono<Votes> {
        return this.getAssociate(votesRequestDTO.idAssociate).flatMap { associate ->
            getSessionOpen(votesRequestDTO.idAgenda).flatMap {session ->
                if (session.getValidity() < Date())
                    Mono.error(ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Voting closed!"))
                else {
                    this.findByIdAgendaAndIdAssociate(votesRequestDTO.idAgenda, votesRequestDTO.idAssociate)
                        .flatMap<Votes> { Mono.error(ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Vote already registered for this member!"))
                        }.switchIfEmpty {
                            validateCPF(associate.getCpf())
                                .flatMap {
                                    if (it.canVote()) {
                                        val votes: Votes = VotesBuilder
                                            .builder()
                                            .idAgenda(votesRequestDTO.idAgenda)
                                            .idAssociate(votesRequestDTO.idAssociate)
                                            .vote(votesRequestDTO.vote)
                                            .build()

                                        val monoVotes: Mono<Votes>  = votesRepository.save(votes)
                                        sendMessageVote(votesRequestDTO)
                                        monoVotes
                                    } else Mono.error(ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Unable to vote"))
                                }
                        }
                }
            }
        }
    }

    private fun sendMessageVote(votesRequestDTO: VotesRequestDTO){
        val postVoteMessage = PostVoteMessage(votesRequestDTO.idAgenda, votesRequestDTO.idAssociate, votesRequestDTO.vote)
        mensageriaService.sendMessage(postVoteMessage)
    }

    private fun getSessionOpen(idAgenda: String): Mono<Session> {
        return sessionService.findByIdAgenda(idAgenda).switchIfEmpty {
                Mono.error(
                    ResponseStatusException(
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        "polling station not open for this agenda!"
                    )
                )
            }
    }

    private fun validateCPF(cpf: String): Mono<ValidateCpfResponseVo> {
        return validateCpfWebClient.validateCpf(cpf).onErrorResume {
                Mono.error(ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Cpf not found!"))
            }
    }

    private fun getAssociate(idAssociate: String): Mono<Associate> {
        return associateService.findById(idAssociate).switchIfEmpty {
            Mono.error(ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Associate not found!"))
        }
    }
}

