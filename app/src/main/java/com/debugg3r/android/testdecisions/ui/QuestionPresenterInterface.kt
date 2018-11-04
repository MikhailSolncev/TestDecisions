package com.debugg3r.android.testdecisions.ui

import com.debugg3r.android.testdecisions.data.Question

interface QuestionPresenterInterface {
    fun bind(activity: QuestrionActivityInterface)
    fun unbind()
    fun getInstance(): QuestionPresenterInterface
    fun destroy()
    fun getQuestionCount(): Int
    fun getQuestion(position: Int): Question
}