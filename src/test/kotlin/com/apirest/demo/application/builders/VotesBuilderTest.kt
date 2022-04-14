package com.apirest.demo.application.builders

import com.apirest.demo.application.entity.Votes
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class VotesBuilderTest{
    @Test
    fun `should return votes with correct idAgenda and idSession and idAssociate and vote true`() {
        val votes = Votes("idAgenda", "idSession", "idAssociate", true)
        val votesWithBuilder = VotesBuilder.builder().idAgenda("idAgenda").idSession("idSession").idAssociate("idAssociate").vote(true).build()
        MatcherAssert.assertThat(votes.getIdAgenda(), Matchers.`is`(votesWithBuilder.getIdAgenda()))
        MatcherAssert.assertThat(votes.getIdSession(), Matchers.`is`(votesWithBuilder.getIdSession()))
        MatcherAssert.assertThat(votes.getIdAssociate(), Matchers.`is`(votesWithBuilder.getIdAssociate()))
        MatcherAssert.assertThat(votes.getVote(), Matchers.`is`(votesWithBuilder.getVote()))
    }

    @Test
    fun `should return votes with correct idAgenda and idSession and idAssociate and vote false`() {
        val votes = Votes("idAgenda", "idSession", "idAssociate", false)
        val votesWithBuilder = VotesBuilder.builder().idAgenda("idAgenda").idSession("idSession").idAssociate("idAssociate").vote(false).build()
        MatcherAssert.assertThat(votes.getIdAgenda(), Matchers.`is`(votesWithBuilder.getIdAgenda()))
        MatcherAssert.assertThat(votes.getIdSession(), Matchers.`is`(votesWithBuilder.getIdSession()))
        MatcherAssert.assertThat(votes.getIdAssociate(), Matchers.`is`(votesWithBuilder.getIdAssociate()))
        MatcherAssert.assertThat(votes.getVote(), Matchers.`is`(votesWithBuilder.getVote()))
    }

    @Test
    fun `should throw exception when votes dont have idAgenda`() {
        Assertions.assertThrows(UninitializedPropertyAccessException::class.java) {
            VotesBuilder.builder().idSession("idSession").idAssociate("idAssociate").vote(true).build()
        }
    }

    @Test
    fun `should throw exception when votes dont have idSession`() {
        Assertions.assertThrows(UninitializedPropertyAccessException::class.java) {
            VotesBuilder.builder().idAgenda("idSession").idAssociate("idAssociate").vote(true).build()
        }
    }

    @Test
    fun `should throw exception when votes dont have idAssociate`() {
        Assertions.assertThrows(UninitializedPropertyAccessException::class.java) {
            VotesBuilder.builder().idAgenda("idSession").idSession("idSession").vote(true).build()
        }
    }

    @Test
    fun `should throw exception when votes dont have vote`() {
        Assertions.assertThrows(IllegalStateException::class.java) {
            VotesBuilder.builder().idAgenda("idSession").idSession("idSession").idAssociate("idAssociate").build()
        }
    }
}