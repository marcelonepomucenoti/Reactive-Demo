package com.apirest.demo.application.service.impl

import com.apirest.demo.application.builders.VotesBuilder
import com.apirest.demo.application.restClient.ValidateCpfWebClient
import com.apirest.demo.application.dto.VotesRequestDTO
import com.apirest.demo.application.entity.Associate
import com.apirest.demo.application.entity.Votes
import com.apirest.demo.application.repository.VotesRepository
import com.apirest.demo.application.service.AssociateService
import com.apirest.demo.application.service.VotesService
import com.apirest.demo.application.vo.ValidateCpfResponseVo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Service
class VotesServiceImpl(
    @Autowired val associateService: AssociateService,
    @Autowired val votesRepository: VotesRepository,
    @Autowired val validateCpfWebClient: ValidateCpfWebClient
) : VotesService {

    override fun findByIdAgendaAndIdAssociate(idAgenda: String, idAssociate: String): Mono<Votes> {
        return votesRepository.findByIdAgendaAndIdAssociate(idAgenda, idAssociate)
    }

    override fun save(votesRequestDTO: VotesRequestDTO): Mono<Votes> {
        return this.findByIdAgendaAndIdAssociate(votesRequestDTO.idAgenda, votesRequestDTO.idAssociate)
            .map { p -> Votes(p.getIdAgenda(), p.getIdSession(), p.getIdAssociate(), p.getVote()) }.flatMap<Votes> {
                throw Exception("Voto já registrado para este associado!")
            }.switchIfEmpty {
                associateService.findById(votesRequestDTO.idAssociate).map { p -> Associate(p.getName(), p.getCpf()) }
                    .flatMap { associate ->
                        validateCpfWebClient.validateCpf(associate.getCpf())
                            .map { p -> ValidateCpfResponseVo(p.getStatus()) }
                            .flatMap {
                                if (it.canVote()) {
                                    val votes: Votes = VotesBuilder
                                        .builder()
                                        .idAgenda(votesRequestDTO.idAgenda)
                                        .idAssociate(votesRequestDTO.idAssociate)
                                        .idSession(votesRequestDTO.idSession)
                                        .vote(votesRequestDTO.vote)
                                        .build()

                                    votesRepository.save(votes)
                                } else Mono.empty()
                            }
                    }
            }
    }
}

