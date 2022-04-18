package com.apirest.demo.application.dto

import com.apirest.demo.application.entity.Votes

class VotesResponseDTO {
    private var idAgenda: String? = null
    private var idAssociate: String? = null
    private var vote: Boolean? = null

    fun getIdAgenda(): String? {
        return this.idAgenda
    }

    fun getIdAssociate(): String? {
        return this.idAssociate
    }

    fun getVote(): Boolean? {
        return this.vote
    }

    fun votesToVotesResponseDTO(votes: Votes) : VotesResponseDTO{
        this.idAgenda = votes.getIdAgenda()
        this.idAssociate = votes.getIdAssociate()
        this.vote = votes.getVote()
        return this
    }
}