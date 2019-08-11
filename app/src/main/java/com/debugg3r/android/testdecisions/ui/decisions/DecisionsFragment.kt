package com.debugg3r.android.testdecisions.ui.decisions


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow

import com.debugg3r.android.testdecisions.data.DataStoreDb
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.core.view.marginTop
import com.debugg3r.android.testdecisions.R
import kotlinx.android.synthetic.main.fragment_decisions.*


class DecisionsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_decisions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (context == null) return

        val decisions = DataStoreDb(context!!).getDecisions()

        var max = 0
        if (decisions.isNotEmpty())
            max = decisions[decisions.keys.first()]!!.size

        //  header
        var tableRow = createTableRow()
        tableRow.addText("Questions")
        for (cnt in 1..(max+1))
            tableRow.addText(cnt.toString())
        decisions_table.addView(tableRow)

        //  rows
        for (pair in decisions) {
            tableRow = createTableRow()
            tableRow.addText(pair.key.text)
            for (answer in pair.value)
                tableRow.addText(answer.text)

            decisions_table.addView(tableRow)
        }
    }

    private fun createTableRow(): TableRow {
        val tableRow = TableRow(context)
        val params = ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT)
        params.topMargin = 9
        tableRow.layoutParams = params
        return tableRow
    }

    private fun TableRow.addText(text: String) {
        val textView = TextView(context)
        textView.text = text
        this.addView(textView)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                DecisionsFragment().apply {
                    arguments = Bundle().apply {

                    }
                }
    }
}
