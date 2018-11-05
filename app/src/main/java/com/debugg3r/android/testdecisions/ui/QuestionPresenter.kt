package com.debugg3r.android.testdecisions.ui

import com.debugg3r.android.testdecisions.data.Answer
import com.debugg3r.android.testdecisions.data.DataStoreProvider
import com.debugg3r.android.testdecisions.data.DataStoreSample
import com.debugg3r.android.testdecisions.data.Question

class QuestionPresenter : QuestionPresenterInterface{
    //var dataStore = DataStoreProvider.instance
    var dataStore = DataStoreSample.instance

    override fun getQuestion(position: Int): Question {
        return dataStore.getQuestion(position)
    }

    override fun getQuestion(uid: String): Question {
        return dataStore.getQuestion(uid)
    }

    override fun getQuestionCount(): Int = dataStore.getCount()

    var activity: QuestrionActivityInterface? = null

    override fun bind(activity: QuestrionActivityInterface) {
        this.activity = activity
    }

    override fun unbind() {
        activity = null
    }

    private object Holder {val instance = QuestionPresenter() }
    companion object {
        val instance: QuestionPresenter by lazy { Holder.instance }
    }
    override fun getInstance(): QuestionPresenterInterface {
        return instance
    }

    override fun addQuestion(question: Question) {
        dataStore.addQuestion(question)
    }

    override fun removeQuestion(question: Question) {
        dataStore.removeQuestion(question)
    }

    override fun getAnswersCount(question: Question?): Int {
        return question?.getAnswers()?.size ?: 0
    }

    override fun addAnswer(question: Question?, answer: Answer) {
        question?.addAnswer(answer)
    }

    override fun removeAnswer(question: Question?, answer: Answer) {
        val answers = question?.getAnswers() as ArrayList<Answer>
        answers.remove(answer)
    }

    override fun getAnswer(question: Question?, position: Int): Answer {
        return question?.getAnswers()?.get(position) ?: Answer("")
    }

    override fun destroy() {

    }

}
