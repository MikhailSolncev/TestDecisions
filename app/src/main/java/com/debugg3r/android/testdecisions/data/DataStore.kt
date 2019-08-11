package com.debugg3r.android.testdecisions.data

interface DataStore {
    fun getQuestions(): Collection<Question>
    fun getQuestion(uid: String): Question
    fun getQuestion(position: Int): Question
    fun addQuestion(question: Question): String
    fun addQuestion(text: String): Question
    fun getCount(): Int
    fun findQuestion(question: Question): Boolean
    fun removeQuestion(question: Question)
    fun removeQuestion(uid: String)
    fun changeQuestion(question: Question, text: String)
    fun changeQuestion(uid: String, text: String)
    fun changeAnswer(question: Question, answer: Answer, text: String)
    fun changeAnswer(uidQuestion: String, uidAnswer: String, text: String)

    fun getDecisions(): Map<Question, List<Answer>>
}