package com.debugg3r.android.testdecisions.ui.questions


import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.debugg3r.android.testdecisions.MainActivity

import com.debugg3r.android.testdecisions.R
import com.debugg3r.android.testdecisions.data.DataStoreDb
import kotlinx.android.synthetic.main.fragment_question.*
import kotlinx.android.synthetic.main.fragment_question.question_text
import kotlinx.android.synthetic.main.question_list_item.*


class QuestionDetailFragment : Fragment() {

    lateinit var listAdapter: TextItemAdapter
    var param: String = ""

    companion object {
        @JvmStatic
        fun newInstance(param: String) =
                QuestionDetailFragment().apply {
                    arguments = Bundle().apply {
                        putString("id", param)
                    }
                }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param = it.getString("id")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_question, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listAdapter = TextItemAdapter(activity as MainActivity)
        with(recycler_answers) {
            layoutManager = LinearLayoutManager(activity)
            adapter = listAdapter
        }

        val dataStoreDb = context?.let { DataStoreDb(it) }

        //question_text.setOnClickListener(OnTextItemEditTextListener(activity as MainActivity))
        question_text.setOnClickListener {
            //if (this.context == null) return@setOnClickListener
            val builder = AlertDialog.Builder(this.context!!)
            val editText = EditText(context)
            editText.inputType = InputType.TYPE_CLASS_TEXT
            editText.setText(question_text.text)
            builder.setView(editText)
            builder.setPositiveButton("Save", { dialog, which ->
                question_text.text = editText.text
                dataStoreDb?.changeQuestion(param, editText.text.toString())
            })
            builder.setNegativeButton("Cancel") { dialog, which ->
            }
            builder.show()
        }

        if (param == "new") {

        } else {
            val question = dataStoreDb?.getQuestion(param)
            question?.let {  listAdapter.setData(it.getAnswers()) }
            question_text.setText(question?.text)
        }
    }

}
