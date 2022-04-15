package com.apirest.demo.application.model

import java.io.Serializable

class PostVoteMessage(
    val idAgenda: String,
    val idAssociado: String,
    val vote: Boolean
): Serializable