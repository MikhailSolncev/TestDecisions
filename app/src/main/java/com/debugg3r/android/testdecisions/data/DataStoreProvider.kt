package com.debugg3r.android.testdecisions.data

import java.util.*
import kotlin.IndexOutOfBoundsException
import kotlin.collections.HashMap

open class DataStoreProvider() : DataStore  {
    private val questionList = HashMap<String, Question>()

    private object Holder {val instance = DataStoreProvider() }

    companion object {

        val instance: DataStore by lazy { Holder.instance }
    }

    override fun getQuestions(): Collection<Question> {
        return questionList.values //as Collection<Question>
    }

    override fun getQuestion(uid: String): Question {
        if (!questionList.containsKey(uid))
            throw IndexOutOfBoundsException("Can't find question by uid '{$uid}'")
        return questionList.get(uid)!!
    }

    override fun getQuestion(position: Int): Question {
        if (position >= questionList.count())
            throw IndexOutOfBoundsException("Can't find question on position '{$position}'")
        return questionList.values.elementAt(position)
    }

    override fun addQuestion(question: Question): String {
        if (!findQuestion(question))
            questionList.put(question.uid, question)
        return question.uid
    }

    override fun addQuestion(text: String): Question {
        val question = Question(text)
        questionList.put(question.uid, question)
        return question
    }

    override fun findQuestion(question: Question): Boolean {
        return questionList.containsKey(question.uid)
    }

    override fun findQuestion(uid: String): Boolean {
        return questionList.containsKey(uid)
    }

    override fun getCount(): Int {
        return questionList.count()
    }

    override fun removeQuestion(question: Question) {
        if (!questionList.containsKey(question.uid))
            throw IndexOutOfBoundsException("Can't find question by uid '{$question.uid}'")
        questionList.remove(question.uid)
    }

    override fun removeQuestion(uid: String) {
        questionList.remove(uid)
    }

}