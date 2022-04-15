package com.apirest.demo.application.service

import com.apirest.demo.application.model.PostVoteMessage
import com.apirest.demo.application.model.ResultVotesMessage

interface MensageriaService {
    fun sendMessage(postVoteMessage: PostVoteMessage)
    fun sendMessage(resultVotesMessage: ResultVotesMessage)
}