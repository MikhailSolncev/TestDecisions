package com.debugg3r.android.testdecisions.ui.questions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.debugg3r.android.testdecisions.MainActivityActionListener
import com.debugg3r.android.testdecisions.R
import com.debugg3r.android.testdecisions.data.Question
import com.debugg3r.android.testdecisions.data.TextItem
import kotlinx.android.synthetic.main.question_list_item.view.*

class TextItemAdapter(mListener: MainActivityActionListener): RecyclerView.Adapter<TextItemAdapter.TextItemViewHolder>() {

    private val items = mutableListOf<TextItem>()
    private val onEventListener = mListener

    init {
        this.onEventListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.question_list_item, parent, false)
        return TextItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: TextItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun setData(newItems: List<TextItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    inner class TextItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                val element = items[layoutPosition]
                onEventListener.performAction(if (element is Question) "question" else "answer", element.uid)
            }
        }
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