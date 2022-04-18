package com.apirest.demo.application.dto

import com.apirest.demo.application.entity.Session
import java.util.*

class SessionResponseDTO {
    private var id: String? = null
    private var idAgenda: String? = null
    private var nameAgenda: String? = null
    private var validity: Date? = null

    fun getId(): String? {
        return this.id
    }

    fun getIdAgenda(): String? {
        return this.idAgenda
    }

    fun getNameAgenda(): String? {
        return this.nameAgenda
    }

    fun getValidity(): Date? {
        return this.validity
    }

    fun sessionToSessionResponseDTO(session: Session) : SessionResponseDTO{
        this.id = session.getId()
        this.idAgenda = session.getIdAgenda()
        this.nameAgenda = session.getNameAgenda()
        this.validity = session.getValidity()
        return this
    }
}