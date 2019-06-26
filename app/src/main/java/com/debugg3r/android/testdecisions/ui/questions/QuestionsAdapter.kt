package com.debugg3r.android.testdecisions.ui.questions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.debugg3r.android.testdecisions.R
import com.debugg3r.android.testdecisions.data.DataStoreDb
import com.debugg3r.android.testdecisions.data.Question
import com.debugg3r.android.testdecisions.data.TextItem
import kotlinx.android.synthetic.main.question_list_item.view.*

class QuestionsAdapter: RecyclerView.Adapter<QuestionsAdapter.QuestionViewHolder>() {

    private val items = mutableListOf<TextItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.question_list_item, parent, false)
        return QuestionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun setData(newItems: List<TextItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(element: TextItem) {
            with(itemView) {
                question_text.text = element.text
                if (element is Question) {
                    question_answers_count.text = element.getAnswers().size.toString()
                }
            }
        }
    }
}