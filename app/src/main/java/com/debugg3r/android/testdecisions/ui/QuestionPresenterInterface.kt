package com.debugg3r.android.testdecisions.ui

import com.debugg3r.android.testdecisions.data.Answer
import com.debugg3r.android.testdecisions.data.Question

interface QuestionPresenterInterface {
    fun bind(activity: QuestrionActivityInterface)
    fun unbind()
    fun getInstance(): QuestionPresenterInterface
    fun destroy()

    fun getQuestionCount(): Int
    fun getQuestion(position: Int): Question
    fun getQuestion(uid: String): Question
    fun addQuestion(question: Question)
    fun removeQuestion(question: Question)
    fun removeQuestion(uid: String)

    fun getAnswersCount(question: Question?): Int
    fun addAnswer(question: Question?, answer: Answer)
    fun removeAnswer(question: Question?, answer: Answer)
    fun getAnswer(question: Question?, position: Int): Answer
    fun removeAnswer(question: Question?, uid: String)
}