package com.apirest.demo.application.repository

import com.apirest.demo.application.entity.Associate
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface AssociateRepository : ReactiveMongoRepository<Associate, String>