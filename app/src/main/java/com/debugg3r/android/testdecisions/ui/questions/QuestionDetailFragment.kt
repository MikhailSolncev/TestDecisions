package com.debugg3r.android.testdecisions.ui.questions


import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.debugg3r.android.testdecisions.MainActivity

import com.debugg3r.android.testdecisions.R
import com.debugg3r.android.testdecisions.data.DataStoreDb
import kotlinx.android.synthetic.main.fragment_question.*


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

        if (param == "new") {

        } else {
            val dataStoreDb = context?.let { DataStoreDb(it) }
            var question = dataStoreDb?.let { it.getQuestion(param) }
            question?.let {  listAdapter.setData(it.getAnswers()) }
            question_text.setText(question?.text)
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

    }
}
