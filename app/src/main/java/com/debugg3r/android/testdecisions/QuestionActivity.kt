package com.debugg3r.android.testdecisions

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.debugg3r.android.testdecisions.data.Answer
import com.debugg3r.android.testdecisions.data.Question
import com.debugg3r.android.testdecisions.data.TextItem
import com.debugg3r.android.testdecisions.ui.QuestionPresenter
import com.debugg3r.android.testdecisions.ui.QuestionPresenterInterface
import com.debugg3r.android.testdecisions.ui.QuestrionActivityInterface
import com.debugg3r.android.testdecisions.ui.TextItemListAdapter
import kotlinx.android.synthetic.main.activity_question.*

class QuestionActivity : AppCompatActivity(), QuestrionActivityInterface {

    var presenter: QuestionPresenterInterface? = null
    var questionAdapter: TextItemListAdapter? = null
    var answerAdapter: TextItemListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        var layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        presenter = QuestionPresenter.instance

        rv_questions.layoutManager = layoutManager
        questionAdapter = TextItemListAdapter(presenter ?: QuestionPresenter())
        rv_questions.adapter = questionAdapter

//        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//
//        itemList = ArrayList()
//        itemList.add(Answer("Yes"))
//        itemList.add(Answer("No"))
//        rv_answers.layoutManager = layoutManager
//        answerAdapter = TextItemListAdapter(itemList)
//        rv_answers.adapter = answerAdapter

    }

    override fun onPause() {
        super.onPause()
        presenter = null
    }

    override fun onResume() {
        super.onResume()
        presenter = QuestionPresenter.instance
        presenter?.bind(this)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun updateQuestions() {

    }

    override fun updateAnswers() {

    }
}
