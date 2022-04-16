package com.apirest.demo.application.service

import com.apirest.demo.application.dto.AgendaRequestDTO
import com.apirest.demo.application.entity.Agenda
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface AgendaService {
    fun findAll(): Flux<Agenda>
    fun findById(id: String): Mono<Agenda>
    fun findByName(name: String): Mono<Agenda>
    fun save(agendaRequestDTO: AgendaRequestDTO): Mono<Agenda>
    fun calculateResult(idAgenda: String): Mono<Agenda>
}