package com.github.dataanon.strategy.number

import com.github.dataanon.Field
import com.github.dataanon.Matchers
import com.github.dataanon.Record
import io.kotlintest.matchers.should
import io.kotlintest.specs.FunSpec

class RandomIntUnitTest : FunSpec(), Matchers {

    val emptyRecord: Record = Record(listOf(), 0)

    init {
        test("should generate random int given with default range") {

            val field       = Field("id", 14454)
            val anonymized  = RandomInt().anonymize(field, emptyRecord)

            anonymized should beIn(0..100)
        }

        test("should generate random int given a range") {

            val field       = Field("id", 0)
            val anonymized  = RandomInt(from = 100, to = 1000).anonymize(field, emptyRecord)

            anonymized should beIn(100..1000)
        }

        test("should generate random int given a negative range") {

            val field       = Field("price", -9090)
            val anonymized  = RandomInt(-1000, 0).anonymize(field, emptyRecord)

            anonymized should beIn(-1000..0)
        }
    }
}