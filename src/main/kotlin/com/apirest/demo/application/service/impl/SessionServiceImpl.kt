package com.apirest.demo.application.service.impl

import com.apirest.demo.application.builders.SessionBuilder
import com.apirest.demo.application.dto.SessionRequestDTO
import com.apirest.demo.application.entity.Agenda
import com.apirest.demo.application.entity.Session
import com.apirest.demo.application.repository.SessionRepository
import com.apirest.demo.application.service.AgendaService
import com.apirest.demo.application.service.SessionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Service
class SessionServiceImpl(
    @Autowired val sessionRepository: SessionRepository,
    @Autowired val agendaService: AgendaService
) : SessionService {

    override fun findAll(): Flux<Session> {
        return sessionRepository.findAll()
    }

    override fun findById(id: String): Mono<Session> {
        return sessionRepository.findById(id)
    }

    override fun save(sessionRequestDTO: SessionRequestDTO): Mono<Session> {
        return agendaService.findById(sessionRequestDTO.idAgenda).map { p -> Agenda(p.getName()) }.flatMap {
            val session: Session = SessionBuilder.builder()
                .validity(sessionRequestDTO.validity)
                .idAgenda(sessionRequestDTO.idAgenda)
                .nameAgenda(it.getName())
                .build()

            sessionRepository.save(session)
        }.switchIfEmpty {
            throw Exception("Agenda não identificada!")
        }
    }
}