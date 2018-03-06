package com.github.dataanon.integration.blacklist

import com.github.dataanon.dsl.Blacklist
import com.github.dataanon.model.DbConfig
import com.github.dataanon.strategy.number.FixedInt
import com.github.dataanon.support.RatingsTable
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.FunSpec
import java.time.LocalDateTime
import java.time.ZoneOffset

class BlacklistRatingsCompositePrimaryKeyIntegrationTest : FunSpec() {

    init {
        test("should do blacklist anonymization for multiple record with composite primaryKey") {
            val dbConfig = DbConfig("jdbc:h2:mem:movies", "", "")
            val ratingsTable = RatingsTable(dbConfig)
                    .insert(1, 1, 4, LocalDateTime.ofEpochSecond(1509701304, 0, ZoneOffset.UTC))
                    .insert(1, 2, 5, LocalDateTime.ofEpochSecond(1509701310, 0, ZoneOffset.UTC))

            Blacklist(dbConfig)
                    .table("RATINGS", listOf("MOVIE_ID", "USER_ID")) {
                        anonymize("RATING").using(FixedInt(3))
                    }.execute(progressBarEnabled = false)

            val records = ratingsTable.findAll()

            records.size shouldBe 2
            records[0]["MOVIE_ID"] shouldBe 1
            records[0]["USER_ID"] shouldBe 1
            records[0]["RATING"] shouldBe 3
            records[0]["CREATED_AT"] shouldBe LocalDateTime.ofEpochSecond(1509701304, 0, ZoneOffset.UTC)

            records[1]["MOVIE_ID"] shouldBe 1
            records[1]["USER_ID"] shouldBe 2
            records[1]["RATING"] shouldBe 3
            records[1]["CREATED_AT"] shouldBe LocalDateTime.ofEpochSecond(1509701310, 0, ZoneOffset.UTC)

            ratingsTable.close()
        }
    }
}