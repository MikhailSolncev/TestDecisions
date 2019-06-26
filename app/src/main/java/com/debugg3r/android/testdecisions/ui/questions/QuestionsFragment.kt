package com.debugg3r.android.testdecisions.ui.questions


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager

import com.debugg3r.android.testdecisions.R
import com.debugg3r.android.testdecisions.data.DataStoreDb
import kotlinx.android.synthetic.main.fragment_questions.*


class QuestionsFragment : Fragment() {

    lateinit var listAdapter: QuestionsAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_questions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listAdapter = QuestionsAdapter()
        with(recycler_questions) {
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
        }

        val job = Thread {
            val questions = context?.let { DataStoreDb(it).getQuestions() as Set }
            questions?.let { listAdapter.setData(it.toList()) }
        }

        job.run()//start()
    }
}
