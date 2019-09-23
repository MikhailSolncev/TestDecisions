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
import com.debugg3r.android.testdecisions.MainActivity

import com.debugg3r.android.testdecisions.R
import com.debugg3r.android.testdecisions.data.Answer
import com.debugg3r.android.testdecisions.data.DataStoreDb
import kotlinx.android.synthetic.main.fragment_questions.*
import androidx.recyclerview.widget.RecyclerView

class QuestionsFragment : Fragment() {

    lateinit var listAdapter: TextItemAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_questions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        val dataStoreDb = DataStoreDb.instance

        listAdapter = TextItemAdapter(context as MainActivity)
        with(recycler_questions) {
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter

            val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                    context?.let { dataStoreDb?.removeQuestion((viewHolder as TextItemAdapter.TextItemViewHolder).uid) }

                    val questions = context?.let { dataStoreDb?.getQuestions() as Set }
                    questions?.let { listAdapter.setData(it.toList()) }
                }
            })
            itemTouchHelper.attachToRecyclerView(this)
        }

        val questions = context?.let { dataStoreDb?.getQuestions() as Set }
        questions?.let { listAdapter.setData(it.toList()) }

        question_add.setOnClickListener {
            val builder = AlertDialog.Builder(this.context!!)
            val editText = EditText(context)
            editText.inputType = InputType.TYPE_CLASS_TEXT
            context?.let {editText.hint = it.resources.getString(R.string.new_question_hint)}
            builder.setView(editText)
            builder.setPositiveButton("Save", { dialog, which ->
                context?.let {
                    val question = dataStoreDb?.addQuestion(editText.text.toString())
                    (it as MainActivity).performAction("question", question!!.uid)
                }
            })
            builder.setNegativeButton("Cancel") { dialog, which ->
            }
            builder.show()
        }

    }
}
