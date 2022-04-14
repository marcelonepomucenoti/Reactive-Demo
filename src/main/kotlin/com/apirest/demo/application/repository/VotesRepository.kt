package com.apirest.demo.application.repository

import com.apirest.demo.application.entity.Votes
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono

interface VotesRepository : ReactiveMongoRepository<Votes, String>{
    fun findByIdAgendaAndIdAssociate(idAgenda: String, idAssociate:String): Mono<Votes>
}