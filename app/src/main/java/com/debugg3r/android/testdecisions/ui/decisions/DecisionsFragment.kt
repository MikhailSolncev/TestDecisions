package com.debugg3r.android.testdecisions.ui.decisions

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TableRow

import com.debugg3r.android.testdecisions.data.DataStoreDb
import android.widget.TextView
import androidx.core.view.setPadding
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

        if (context == null) return

        context?.let { textviewBorder = it.getDrawable(R.drawable.textview_border)}

        val decisions = DataStoreDb(context!!).getDecisions()

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

        //  horizontal separator
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
                TableRow.LayoutParams.WRAP_CONTENT)
        //params.setMargins(2, 2, 2, 2)
        this.addView(textView, params)
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
