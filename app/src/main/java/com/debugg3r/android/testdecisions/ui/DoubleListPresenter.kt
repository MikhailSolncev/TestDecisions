package com.debugg3r.android.testdecisions.ui

import android.content.Context
import com.debugg3r.android.testdecisions.data.*
import java.lang.IndexOutOfBoundsException

class DoubleListPresenter(mContext: Context): Command {
    companion object {
        var instance: DoubleListPresenter? = null
        fun getInstance(context: Context): DoubleListPresenter {
            if (instance == null) instance = DoubleListPresenter(context)
            return instance as DoubleListPresenter
        }

        fun exterminateInstance() = { instance = null}
    }

    var dataStore = DataStoreDb(mContext)
    var currentQuestion: Question? = null

    var adapter1: TextListAdapter? = null
    var adapter2: TextListAdapter? = null

    val masterList  = object : ListDataset {

        override fun getCount(): Int = dataStore.getCount()

        override fun deleteElement(position: Int): Int {
            var question = dataStore.getQuestion(position)
            dataStore.removeQuestion(question)
            if (dataStore.getCount() > 0)
                currentQuestion = dataStore.getQuestion(0)
            return 1
        }

        override fun getElement(position: Int): TextItem {
            currentQuestion = dataStore.getQuestion(position)
            return currentQuestion as Question
        }

        override fun newItem(text: String): TextItem {
            currentQuestion = dataStore.addQuestion(text)
            return currentQuestion as Question
        }

        override fun changeText(position: Int, text: String) {
            val question = dataStore.getQuestion(position)
            dataStore.changeQuestion(question, text)
        }
    }

    var slaveList  = object : ListDataset {

        override fun getCount(): Int {
            return currentQuestion?.getAnswers()?.size ?: 0
        }

        override fun deleteElement(position: Int): Int {
            val answer = getElement(position) as Answer
            dataStore.removeAnswer(currentQuestion!!, answer)
            return 1
        }

        override fun getElement(position: Int): TextItem {
            if (currentQuestion?.getAnswers()?.size ?:0 >= position)
                throw IndexOutOfBoundsException("Answer position out of bounds")
            return currentQuestion!!.getAnswers()[position]
        }

        override fun newItem(text: String): TextItem {
            if (currentQuestion == null)
                throw IllegalStateException("Question not setted")
            val answer = Answer(text)
            dataStore.addAnswer(currentQuestion!!, answer)
            return answer
        }

        override fun changeText(position: Int, text: String) {
            val answer = getElement(position) as Answer
            dataStore.changeAnswer(currentQuestion!!, answer, text)
        }
    }

    override fun execute() {
    }

    override fun execute(parameter: Int) {

    }

    fun bind(adapter1: TextListAdapter, adapter2: TextListAdapter) {
        this.adapter1 = adapter1
        this.adapter2 = adapter2
        //adapter1.dataset.changeList(dataStore.getQuestions() as List)
    }

    fun unbind() {
        adapter1 = null
        adapter2 = null
    }
}