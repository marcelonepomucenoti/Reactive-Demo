package com.apirest.demo.application.builders

import com.apirest.demo.application.entity.Agenda

class AgendaBuilder {
    private lateinit var name: String

    companion object {
        fun builder(): AgendaBuilder {
            return AgendaBuilder()
        }
    }

    fun name(name: String): AgendaBuilder {
        this.name = name
        return this
    }

    fun build(): Agenda {
        return Agenda(
            name = this.name
        )
    }
}