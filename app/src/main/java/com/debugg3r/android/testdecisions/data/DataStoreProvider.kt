package com.debugg3r.android.testdecisions.data

import java.util.*
import kotlin.IndexOutOfBoundsException
import kotlin.collections.HashMap

open class DataStoreProvider() : DataStore  {
    private val questionMap = HashMap<String, Question>()
    private val questionList = LinkedList<Question>()

    private object Holder {val instance = DataStoreProvider() }

    companion object {
        val instance: DataStore by lazy { Holder.instance }
    }

    override fun getQuestions(): Collection<Question> {
        return questionMap.values //as Collection<Question>
    }

    override fun getQuestion(uid: String): Question {
        if (!questionMap.containsKey(uid))
            throw IndexOutOfBoundsException("Can't find question by uid '{$uid}'")
        return questionMap.get(uid)!!
    }

    override fun getQuestion(position: Int): Question {
        if (position >= questionList.size)
            throw IndexOutOfBoundsException("Can't find question on position '{$position}'")
        return questionList.get(position)
    }

    override fun addQuestion(question: Question): String {
        if (!findQuestion(question)) {
            questionMap.put(question.uid, question)
            questionList.add(question)
        }
        return question.uid
    }

    override fun addQuestion(text: String): Question {
        val question = Question(text)
        questionMap.put(question.uid, question)
        questionList.add(question)
        return question
    }

    override fun findQuestion(question: Question): Boolean {
        return questionMap.containsKey(question.uid)
    }

    override fun findQuestion(uid: String): Boolean {
        return questionMap.containsKey(uid)
    }

    override fun getCount(): Int {
        return questionList.size
    }

    override fun removeQuestion(question: Question) {
        if (!questionMap.containsKey(question.uid))
            throw IndexOutOfBoundsException("Can't find question by uid '{$question.uid}'")
        questionMap.remove(question.uid)
        questionList.remove(question)
    }

    override fun removeQuestion(uid: String) {
        questionMap.remove(uid)
        for (question in questionList)
            if (question.uid == uid) {
                questionList.remove(question)
                break
            }
    }

}