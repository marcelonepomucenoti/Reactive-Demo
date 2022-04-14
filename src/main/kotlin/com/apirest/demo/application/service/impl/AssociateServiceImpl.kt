package com.apirest.demo.application.service.impl

import com.apirest.demo.application.builders.AssociateBuilder
import com.apirest.demo.application.dto.AssociateRequestDTO
import com.apirest.demo.application.entity.Associate
import com.apirest.demo.application.repository.AssociateRepository
import com.apirest.demo.application.service.AssociateService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class AssociateServiceImpl(@Autowired val associateRepository: AssociateRepository) : AssociateService {

    override fun findById(id: String): Mono<Associate> {
        return associateRepository.findById(id)
    }

    override fun save(associateRequestDTO: AssociateRequestDTO): Mono<Associate> {
        val associate: Associate =
            AssociateBuilder.builder()
                .name(associateRequestDTO.name)
                .cpf(associateRequestDTO.cpf)
                .build()
        return associateRepository.save(associate)
    }
}