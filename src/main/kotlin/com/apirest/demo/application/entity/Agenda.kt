package com.apirest.demo.application.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class Agenda(
    private var name: String,
) {
    @Id
    private lateinit var id: String

    private var receivedVotesTrue: Long = 0
    private var receivedVotesFalse: Long = 0
    private var isAccountedAgenda: Boolean = false

    fun getId(): String {
        return this.id
    }

    fun getName(): String {
        return this.name
    }

    fun getReceivedVotesTrue(): Long {
        return this.receivedVotesTrue
    }

    fun getReceivedVotesFalse(): Long {
        return this.receivedVotesFalse
    }

    fun getIsAccountedAgenda(): Boolean {
        return this.isAccountedAgenda
    }

    fun setIsAccountedAgenda(isAccountedAgenda: Boolean){
        this.isAccountedAgenda = isAccountedAgenda
    }

    fun addVotes(vote: Boolean) {
        if (vote)
            this.receivedVotesTrue++
        else
            this.receivedVotesFalse++
    }
}