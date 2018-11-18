package com.debugg3r.android.testdecisions

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
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

        object: ItemTouchHelper(
                object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
                    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
                        return false
                    }

                    override fun getSwipeDirs(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int {
                        if ((viewHolder?.adapterPosition ?: 0) == (presenter?.getQuestionCount() ?: 0)) return 0
                        return super.getSwipeDirs(recyclerView, viewHolder)
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
                        val holder = viewHolder as QuestionListAdapter.ViewHolder
                        val uid = holder.tv_item?.getTag(R.integer.tag_uid) as String
                        presenter?.removeQuestion(uid)
                        questionAdapter?.notifyDataSetChanged()
                        answerAdapter?.notifyDataSetChanged()
                    }
                }
        ){}.attachToRecyclerView(rv_questions)

        object: ItemTouchHelper(
                object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
                    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
                        return false
                    }

                    override fun getSwipeDirs(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int {
                        if ((viewHolder?.adapterPosition ?: 0) == (presenter?.getAnswersCount(answerAdapter?.question) ?: 0)) return 0
                        return super.getSwipeDirs(recyclerView, viewHolder)
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
                        val holder = viewHolder as AnswerListAdapter.ViewHolder
                        val uid = holder.tv_item?.getTag(R.integer.tag_uid) as String
                        presenter?.removeAnswer(answerAdapter?.question, uid)
                        answerAdapter?.notifyDataSetChanged()
                    }
                }
        ){}.attachToRecyclerView(rv_answers)
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
