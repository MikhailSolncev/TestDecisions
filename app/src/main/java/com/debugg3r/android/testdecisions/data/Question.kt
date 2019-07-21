package com.debugg3r.android.testdecisions.data

import java.util.*
import kotlin.collections.ArrayList

data class Question(override var text: String, override val uid: String) : TextItem {
    private val answers = mutableListOf<Answer>()
    constructor (text: String) : this(text, UUID.randomUUID().toString())

    fun getAnswers(): List<Answer> = answers
    fun addAnswer(answer: Answer) {
        if (answers.find { it.uid == answer.uid } == null)
            answers.add(answer)
    }

    fun removeAnswer(uid: String) {
        var answer = answers.find { it.uid == uid }
        if (answer != null) answers.remove(answer)
    }

    fun findAnswer(uid: String): Answer? {
        return answers.find { it.uid == uid }
    }
}