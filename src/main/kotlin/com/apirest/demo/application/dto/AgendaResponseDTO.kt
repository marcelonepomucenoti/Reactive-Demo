package com.apirest.demo.application.dto

import com.apirest.demo.application.entity.Agenda

class AgendaResponseDTO(

) {
    private var id: String? = null
    private var name: String? = null
    private var receivedVotesTrue: Long? = null
    private var receivedVotesFalse: Long? = null
    private var isAccountedAgenda: Boolean? = null

    fun getId(): String? {
        return this.id
    }

    fun getName(): String? {
        return this.name
    }

    fun getReceivedVotesTrue(): Long? {
        return this.receivedVotesTrue
    }

    fun getReceivedVotesFalse(): Long? {
        return this.receivedVotesFalse
    }

    fun getIsAccountedAgenda(): Boolean? {
        return this.isAccountedAgenda
    }

    fun agendaToAgendaResponseDTO(agenda: Agenda) : AgendaResponseDTO{
        this.id = agenda.getId()
        this.name = agenda.getName()
        this.receivedVotesTrue = agenda.getReceivedVotesTrue()
        this.receivedVotesFalse = agenda.getReceivedVotesFalse()
        this.isAccountedAgenda = agenda.getIsAccountedAgenda()
        return this
    }
}