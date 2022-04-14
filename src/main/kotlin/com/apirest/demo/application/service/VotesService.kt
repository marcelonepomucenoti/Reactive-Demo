package com.apirest.demo.application.service

import com.apirest.demo.application.dto.VotesRequestDTO
import com.apirest.demo.application.entity.Votes
import reactor.core.publisher.Mono

interface VotesService {
    fun findByIdAgendaAndIdAssociate(idAgenda: String, idAssociate: String): Mono<Votes>
    fun save(votesRequestDTO: VotesRequestDTO): Mono<Votes>
}