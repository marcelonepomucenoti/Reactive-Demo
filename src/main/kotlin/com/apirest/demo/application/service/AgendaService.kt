package com.apirest.demo.application.service

import com.apirest.demo.application.dto.AgendaRequestDTO
import com.apirest.demo.application.dto.AgendaResponseDTO
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface AgendaService {
    fun findAll(): Flux<AgendaResponseDTO>
    fun findById(id: String): Mono<AgendaResponseDTO>
    fun save(agendaRequestDTO: AgendaRequestDTO): Mono<AgendaResponseDTO>
    fun calculateResult(idAgenda: String): Mono<AgendaResponseDTO>
}