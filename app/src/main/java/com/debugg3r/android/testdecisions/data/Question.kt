package com.debugg3r.android.testdecisions.data

import java.util.*
import kotlin.collections.ArrayList

data class Question(override var text: String, override val uid: String) : TextItem {
    private val answers = mutableListOf<Answer>()
    //override val uid: String = UUID.randomUUID().toString()
    constructor (text: String) : this(text, UUID.randomUUID().toString())

    fun getAnswers(): List<Answer> = answers
    fun addAnswer(answer: Answer) {
        answers.add(answer)
    }

    fun removeAnswer(uid: String) {
        for (answer in answers) {
            if (answer.uid == uid) {
                answers.remove(answer)
                break
            }
        }
    }
}