package com.dsm.collector.database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

object WordEntity : Table("word") {
    val id = integer(name = "id").autoIncrement().primaryKey()
    val englishWord = varchar(name = "english_word", length = 20)
    val koreanWord = varchar(name = "korean_word", length = 20).nullable()
    val count = integer(name = "count")
}

class DatabaseConnector {

    fun insertAll(words: List<Word>) {
        words.forEach {
            insert(
                english = it.englishWord,
                korean = it.koreanWord,
                wordCount = it.count,
            )
        }
    }

    private fun insert(english: String, korean: String?, wordCount: Int) {
        transaction {
            WordEntity.insert {
                it[englishWord] = english
                it[koreanWord] = korean
                it[count] = wordCount
            }
        }
    }

    fun reset() {
        transaction {
            WordEntity.deleteAll()
        }
    }

    fun connect() {
        Database.connect(DatabaseProperty().dataSource())
    }
}