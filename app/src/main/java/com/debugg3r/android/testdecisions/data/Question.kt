package com.debugg3r.android.testdecisions.data

import java.util.*
import kotlin.collections.ArrayList

data class Question(override var text: String): TextItem {
    private val answers: List<Answer> = ArrayList<Answer>()
    override val uid: String = UUID.randomUUID().toString()
    fun getAnswers(): List<Answer> = answers
}