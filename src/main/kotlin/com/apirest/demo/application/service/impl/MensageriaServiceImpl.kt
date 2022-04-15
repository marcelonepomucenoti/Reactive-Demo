package com.apirest.demo.application.service.impl

import com.apirest.demo.application.model.PostVoteMessage
import com.apirest.demo.application.model.ResultVotesMessage
import com.apirest.demo.application.properties.RabbitMQProperties
import com.apirest.demo.application.service.MensageriaService
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MensageriaServiceImpl(@Autowired val rabbitTemplate: RabbitTemplate,
                            @Autowired val rabbitMQProperties: RabbitMQProperties) : MensageriaService {
    override fun sendMessage(postVoteMessage: PostVoteMessage) {
        rabbitTemplate.convertAndSend(rabbitMQProperties.postVoteQueue, postVoteMessage)
    }

    override fun sendMessage(resultVotesMessage: ResultVotesMessage) {
        rabbitTemplate.convertAndSend(rabbitMQProperties.resultVotesQueue, resultVotesMessage)
    }
}