package com.debugg3r.android.testdecisions.ui.questions


import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.debugg3r.android.testdecisions.MainActivity

import com.debugg3r.android.testdecisions.R
import com.debugg3r.android.testdecisions.data.Answer
import com.debugg3r.android.testdecisions.data.DataStoreDb
import com.debugg3r.android.testdecisions.data.Question
import kotlinx.android.synthetic.main.fragment_question.*
import kotlinx.android.synthetic.main.fragment_question.question_text


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

        listAdapter = TextItemAdapter(activity as MainActivity)
        with(recycler_answers) {
            layoutManager = LinearLayoutManager(activity)
            adapter = listAdapter

            val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

                override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                    return false
                }
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                    val question = dataStoreDb?.getQuestion(param)
                    question?.let {
                        val uid = (viewHolder as TextItemAdapter.TextItemViewHolder).uid
                        it.findAnswer(uid)?.let { it1 -> dataStoreDb.removeAnswer(it, it1) }
                        it.removeAnswer(uid)
                        listAdapter.setData(it.getAnswers())
                    }
                }
            })
            itemTouchHelper.attachToRecyclerView(this)
        }

        if (param == "new") {
            context?.let {
                var question = Question(it.getString(R.string.new_answer_hint))
                param = question.uid
            }

        } else {
            val question = dataStoreDb?.getQuestion(param)
            question?.let {  listAdapter.setData(it.getAnswers()) }
            question_text.setText(question?.text)
        }

        answer_add.setOnClickListener{
            val builder = AlertDialog.Builder(this.context!!)
            val editText = EditText(context)
            editText.inputType = InputType.TYPE_CLASS_TEXT
            context?.let {editText.hint = it.resources.getString(R.string.new_answer_hint)}
            builder.setView(editText)
            builder.setPositiveButton("Save", { dialog, which ->
                val question = dataStoreDb?.getQuestion(param)
                dataStoreDb?.let { question?.let { it1 -> it.addAnswer(it1, Answer(editText.text.toString())) } }
                //question_text.text = editText.text
                //dataStoreDb?.changeQuestion(param, editText.text.toString())
                question?.let { listAdapter.setData(it.getAnswers())}
            })
            builder.setNegativeButton("Cancel") { dialog, which ->
            }
            builder.show()
        }
    }
}
