package com.debugg3r.android.testdecisions

import android.content.Intent

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val LOG_TAG = this::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_questions.setOnClickListener { view -> questionsOnClick(view)}
    }

    fun questionsOnClick(view: View) {
        var intent = Intent(this, QuestionActivity::class.java)
        startActivity(intent)
    }
}
