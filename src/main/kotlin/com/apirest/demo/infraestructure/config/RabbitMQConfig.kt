package com.apirest.demo.infraestructure.config

import com.apirest.demo.application.properties.RabbitMQProperties
import org.springframework.amqp.core.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component

class RabbitMQConfig(
    @Autowired val rabbitMQProperties: RabbitMQProperties,
    @Autowired val amqpAdmin: AmqpAdmin
) {
    private fun queue(nomeFila: String): Queue {
        return Queue(nomeFila, true, false, false)
    }

    private fun directExchange(): DirectExchange {
        return DirectExchange(rabbitMQProperties.directExchange)
    }

    private fun binding(queue: Queue, exchange: DirectExchange): Binding {
        return BindingBuilder.bind(queue).to(exchange).with(queue.name)
    }

    @PostConstruct
    private fun addPostVoteQueue() {
        val postVoteQueue: Queue = this.queue(rabbitMQProperties.postVoteQueue)

        val directExchange: DirectExchange = this.directExchange()

        val binding: Binding = this.binding(postVoteQueue, directExchange)

        this.amqpAdmin.declareQueue(postVoteQueue)
        this.amqpAdmin.declareExchange(directExchange)
        this.amqpAdmin.declareBinding(binding)

    }

    @PostConstruct
    private fun resultVotesQueue() {
        val resultVotesQueue: Queue = this.queue(rabbitMQProperties.resultVotesQueue)

        val directExchange: DirectExchange = this.directExchange()

        val binding: Binding = this.binding(resultVotesQueue, directExchange)

        this.amqpAdmin.declareQueue(resultVotesQueue)
        this.amqpAdmin.declareExchange(directExchange)
        this.amqpAdmin.declareBinding(binding)
    }
}