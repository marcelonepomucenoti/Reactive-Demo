package com.apirest.demo.application.service.impl

import com.apirest.demo.application.builders.SessionBuilder
import com.apirest.demo.application.dto.SessionRequestDTO
import com.apirest.demo.application.dto.SessionResponseDTO
import com.apirest.demo.application.entity.Agenda
import com.apirest.demo.application.entity.Session
import com.apirest.demo.application.repository.AgendaRepository
import com.apirest.demo.application.repository.SessionRepository
import com.apirest.demo.application.service.SessionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Service
class SessionServiceImpl(
    @Autowired val sessionRepository: SessionRepository,
    @Autowired val agendaRepository: AgendaRepository
) : SessionService {

    override fun findAll(): Flux<SessionResponseDTO> {
        return sessionRepository.findAll().map { SessionResponseDTO().sessionToSessionResponseDTO(it) }
    }

    override fun findById(id: String): Mono<SessionResponseDTO> {
        return sessionRepository.findById(id).map { SessionResponseDTO().sessionToSessionResponseDTO(it) }
    }

    override fun save(sessionRequestDTO: SessionRequestDTO): Mono<SessionResponseDTO> {
        return sessionRepository.findByIdAgenda(sessionRequestDTO.idAgenda)
            .map { a -> Session(a.getIdAgenda(), a.getNameAgenda(), a.getValidity()) }.flatMap<Session?> {
                Mono.error(ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Agenda already has session!"))
            }.switchIfEmpty {
                agendaRepository.findById(sessionRequestDTO.idAgenda).map { p -> Agenda(p.getName()) }.flatMap {
                    val session: Session = SessionBuilder.builder()
                        .validity(sessionRequestDTO.validity)
                        .idAgenda(sessionRequestDTO.idAgenda)
                        .nameAgenda(it.getName())
                        .build()

                    sessionRepository.save(session)
                }.switchIfEmpty {
                    Mono.error(ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Unidentified agenda!"))
                }
            }.map { SessionResponseDTO().sessionToSessionResponseDTO(it) }
    }
}