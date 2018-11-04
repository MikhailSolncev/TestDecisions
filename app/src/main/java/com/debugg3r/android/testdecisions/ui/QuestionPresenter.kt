package com.debugg3r.android.testdecisions.ui

import com.debugg3r.android.testdecisions.data.DataStoreProvider
import com.debugg3r.android.testdecisions.data.DataStoreSample
import com.debugg3r.android.testdecisions.data.Question

class QuestionPresenter : QuestionPresenterInterface{
    //var dataStore = DataStoreProvider.instance
    var dataStore = DataStoreSample.instance

    override fun getQuestion(position: Int): Question {
        return dataStore.getQuestion(position)
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

    override fun destroy() {

    }

}
