package com.debugg3r.android.testdecisions

import android.content.Intent

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val LOG_TAG = this::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_questions.setOnClickListener { view -> questionsOnClick(view)}
    }

    fun questionsOnClick(view: View) {
        var intent = Intent(this, QuestionActivity::class.java)
        startActivity(intent)
    }
}
