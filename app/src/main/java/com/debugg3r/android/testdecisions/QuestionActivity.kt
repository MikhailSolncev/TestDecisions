package com.debugg3r.android.testdecisions

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.debugg3r.android.testdecisions.data.Answer
import com.debugg3r.android.testdecisions.data.Question
import com.debugg3r.android.testdecisions.data.TextItem
import com.debugg3r.android.testdecisions.ui.TextItemListAdapter
import kotlinx.android.synthetic.main.activity_question.*

class QuestionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        var layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        var itemList = ArrayList<TextItem>()
        itemList.add(Question("Do you have a cat?"))
        itemList.add(Question("Do you like your cat?"))
        itemList.add(Question("Do you obey your cat?"))
        itemList.add(Question("Are you panda?"))
        rv_questions.layoutManager = layoutManager
        rv_questions.adapter = TextItemListAdapter(itemList)

        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        itemList = ArrayList()
        itemList.add(Answer("Yes"))
        itemList.add(Answer("No"))
        rv_answers.layoutManager = layoutManager
        rv_answers.adapter = TextItemListAdapter(itemList)

    }
}
