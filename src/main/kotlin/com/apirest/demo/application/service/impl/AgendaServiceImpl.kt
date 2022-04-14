package com.apirest.demo.application.service.impl

import com.apirest.demo.application.builders.AgendaBuilder
import com.apirest.demo.application.dto.AgendaRequestDTO
import com.apirest.demo.application.service.AgendaService
import com.apirest.demo.application.entity.Agenda
import com.apirest.demo.application.repository.AgendaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Service
class AgendaServiceImpl(@Autowired val agendaRepository: AgendaRepository) : AgendaService {
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
        return this.findByName(agendaRequestDTO.name).map { p -> Agenda(p.getName()) }.flatMap<Agenda> {
            throw Exception("Agenda já cadastrada!")
        }.switchIfEmpty {
            val agenda: Agenda = AgendaBuilder.builder().name(agendaRequestDTO.name).build()
            agendaRepository.save(agenda)
        }
    }
}