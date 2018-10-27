package com.debugg3r.android.testdecisions.data

import kotlin.collections.ArrayList

data class Question(override var text: String): TextItem {
    private val answers: List<Answer> = ArrayList<Answer>()

    fun getAnswers(): List<Answer> = answers
}