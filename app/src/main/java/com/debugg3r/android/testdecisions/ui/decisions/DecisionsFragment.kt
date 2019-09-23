package com.debugg3r.android.testdecisions.ui.decisions

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.LinearLayout
import android.widget.TableRow

import androidx.fragment.app.Fragment
import androidx.core.view.setPadding

import com.debugg3r.android.testdecisions.data.DataStoreDb
import com.debugg3r.android.testdecisions.R
import kotlinx.android.synthetic.main.fragment_decisions.*


class DecisionsFragment : Fragment() {

    var textviewBorder: Drawable? = null

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

        context?.let {
            showDecisions(it)
            showFilter(it)
        }

    }

    private fun showDecisions(context: Context) {
        context.let { textviewBorder = it.getDrawable(R.drawable.textview_border)}

        if (DataStoreDb.instance == null) return

        val decisions = DataStoreDb.instance!!.getDecisions()

        var max = 0
        if (decisions.isNotEmpty())
            max = decisions.getValue(decisions.keys.first()).size

        //  header
        var tableRow = createTableRow()
        tableRow.addText("Questions", R.style.TableHeader)
        for (cnt in 1..(max))
            tableRow.addText(cnt.toString(), R.style.TableHeader)
        decisions_table.addView(tableRow)

        //  rows
        for (pair in decisions) {
            //  row
            tableRow = createTableRow()
            tableRow.addText(pair.key.text, R.style.TableRow)
            for (answer in pair.value)
                tableRow.addText(answer.text, R.style.TableRow)

            decisions_table.addView(tableRow)
        }
    }

    private fun showFilter(context: Context) {
        context.let { textviewBorder = it.getDrawable(R.drawable.textview_border)}

        if (DataStoreDb.instance == null) return

        val questions = DataStoreDb.instance!!.getQuestions()
        var max = 0
        questions.map { max = Math.max(max, it.getAnswers().size) }

        //  header
        var tableRow = createTableRow()
        tableRow.addText("Questions", R.style.TableHeader)
        for (cnt in 1..(max))
            tableRow.addText(cnt.toString(), R.style.TableHeader)
        decisions_filter.addView(tableRow)

        for (question in questions) {
            tableRow = createTableRow()
            tableRow.addText(question.text, R.style.TableRow)
            for (answer in question.getAnswers()) {
                tableRow.addCheckbox(answer.text, answer.uid)
            }
            decisions_filter.addView(tableRow)
        }
    }

    private fun createTableRow(): TableRow {
        val tableRow = TableRow(context)
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
        params.topMargin = 9
        tableRow.layoutParams = params
        return tableRow
    }

    private fun TableRow.addText(text: String, style: Int) {
        val textView = TextView(context, null, 0, style)
        textView.text = text
        textView.background = textviewBorder
        textView.setPadding(4)
        val params = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.MATCH_PARENT)
        //params.setMargins(2, 2, 2, 2)
        this.addView(textView, params)
    }

    private fun TableRow.addCheckbox(text: String, uid: String) {
        val checkBoxView = CheckBox(context)
        checkBoxView.text = text
        checkBoxView.setPadding(4)
        checkBoxView.isChecked = true
        checkBoxView.tag = uid
        val params = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT)
        checkBoxView.setOnCheckedChangeListener { compoundButton, value ->
            DataStoreDb.instance?.changeAnswerEnabled(compoundButton.tag as String, value)
        }
        this.addView(checkBoxView, params)
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
