package com.debugg3r.android.testdecisions

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.debugg3r.android.testdecisions.ui.*
import kotlinx.android.synthetic.main.activity_question.*

class QuestionActivity : AppCompatActivity(), QuestrionActivityInterface {

    var presenter: QuestionPresenterInterface? = null
    var questionAdapter: QuestionListAdapter? = null
    var answerAdapter: AnswerListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        var layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        presenter = QuestionPresenter.instance

        rv_questions.layoutManager = layoutManager
        questionAdapter = QuestionListAdapter(presenter ?: QuestionPresenter())
        rv_questions.adapter = questionAdapter

        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_answers.layoutManager = layoutManager
        answerAdapter = AnswerListAdapter(presenter ?: QuestionPresenter())
        rv_answers.adapter = answerAdapter

        questionAdapter?.answerListAdapter = answerAdapter
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
