package com.apirest.demo.application.model

import java.io.Serializable

class ResultVotesMessage(
    val idAgenda: String,
    val nameAgenda: String,
    val votesYes: Long,
    val votesNo: Long
) : Serializable