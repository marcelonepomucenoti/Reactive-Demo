package com.apirest.demo.application.builders

import com.apirest.demo.application.entity.Votes
import kotlin.properties.Delegates


class VotesBuilder {
    private lateinit var idAssociate: String
    private lateinit var idAgenda: String
    private var vote by Delegates.notNull<Boolean>()


    companion object {
        fun builder(): VotesBuilder {
            return VotesBuilder()
        }
    }

    fun idAgenda(idAgenda: String): VotesBuilder {
        this.idAgenda = idAgenda
        return this
    }

    fun idAssociate(idAssociate: String): VotesBuilder {
        this.idAssociate = idAssociate
        return this
    }

    fun vote(vote: Boolean): VotesBuilder {
        this.vote = vote
        return this
    }

    fun build(): Votes {
        return Votes(
            idAgenda = this.idAgenda,
            idAssociate = this.idAssociate,
            vote = this.vote
        )
    }
}