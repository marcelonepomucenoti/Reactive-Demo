package com.apirest.demo.application.service.impl

import com.apirest.demo.application.builders.AgendaBuilder
import com.apirest.demo.application.dto.AgendaRequestDTO
import com.apirest.demo.application.entity.Agenda
import com.apirest.demo.application.model.ResultVotesMessage
import com.apirest.demo.application.repository.AgendaRepository
import com.apirest.demo.application.repository.SessionRepository
import com.apirest.demo.application.repository.VotesRepository
import com.apirest.demo.application.service.AgendaService
import com.apirest.demo.application.service.MensageriaService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import java.util.*

@Service
class AgendaServiceImpl(
    @Autowired val agendaRepository: AgendaRepository,
    @Autowired val sessionRepository: SessionRepository,
    @Autowired val votesRepository: VotesRepository,
    @Autowired val mensageriaService: MensageriaService
) : AgendaService {
    override fun findAll(): Flux<Agenda> {
        return agendaRepository.findAll()
    }

    override fun findById(id: String): Mono<Agenda> {
        return agendaRepository.findById(id)
    }

    override fun findByName(name: String): Mono<Agenda> {
        return agendaRepository.findByName(name)
    }

    override fun save(agendaRequestDTO: AgendaRequestDTO): Mono<Agenda> {
        return this.findByName(agendaRequestDTO.name).mapNotNull { p -> Agenda(p.getName()) }.flatMap<Agenda> {
            Mono.error(ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Agenda already registered!"))
        }.switchIfEmpty {
            val agenda: Agenda = AgendaBuilder.builder().name(agendaRequestDTO.name).build()
            agendaRepository.save(agenda)
        }
    }

    override fun calculateResult(idAgenda: String): Mono<Agenda> {
        return agendaRepository.findById(idAgenda).flatMap { agenda ->
            if(agenda.getIsAccountedAgenda())
                Mono.error(ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Agenda has already been accounted for"))
            else
                sessionRepository.findByIdAgenda(idAgenda).flatMap {
                    if(it.getValidity() > Date())
                        Mono.error(ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Agenda can still receive votes"))
                    else
                        votesRepository.findByIdAgenda(idAgenda).flatMap {
                            agenda.addVotes(it.getVote())
                            Flux.just(it)
                        }.doOnComplete {
                            agenda.setIsAccountedAgenda(true)
                            val resultVotesMessage = ResultVotesMessage(idAgenda, agenda.getName(), agenda.getReceivedVotesTrue(), agenda.getReceivedVotesFalse() )
                            mensageriaService.sendMessage(resultVotesMessage)
                        }.then(agendaRepository.save(agenda))
            }
        }
    }
}