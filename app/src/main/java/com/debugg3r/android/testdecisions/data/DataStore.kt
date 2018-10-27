package com.debugg3r.android.testdecisions.data

interface DataStore {
    fun getQuestions(): List<Question>
    fun getQuestion(position: Int): Question
    fun addQuestion(question: Question)
    fun getCount(): Int
    fun findQuestion(question: Question): Boolean
    fun removeQuestion(question: Question)
    fun removeQuestion(position: Int)
}