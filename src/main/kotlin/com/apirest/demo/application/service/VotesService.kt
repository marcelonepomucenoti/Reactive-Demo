package com.apirest.demo.application.service

import com.apirest.demo.application.dto.VotesRequestDTO
import com.apirest.demo.application.dto.VotesResponseDTO
import reactor.core.publisher.Mono

interface VotesService {
    fun save(votesRequestDTO: VotesRequestDTO): Mono<VotesResponseDTO>
}