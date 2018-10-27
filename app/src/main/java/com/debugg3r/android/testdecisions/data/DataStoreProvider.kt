package com.debugg3r.android.testdecisions.data

import java.util.*
import kotlin.IndexOutOfBoundsException

class DataStoreProvider(): DataStore {

    private val questionList = LinkedList<Question>()

    override fun getQuestions(): List<Question> {
        return questionList.clone() as List<Question>
    }

    override fun getQuestion(position: Int): Question {
        if (position > questionList.count())
            throw IndexOutOfBoundsException()
        return questionList.get(position)
    }

    override fun addQuestion(question: Question) {
        if (findQuestion(question)) return
        questionList.add(question)
    }

    override fun findQuestion(question: Question): Boolean {
        var result = false
        for (element in questionList) {
            if (question.text == element.text)
                result = true
        }
        return result
    }

    override fun getCount(): Int {
        return questionList.count()
    }

    override fun removeQuestion(question: Question) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeQuestion(position: Int) {
        if (position > questionList.count())
            throw IndexOutOfBoundsException()
    }

}