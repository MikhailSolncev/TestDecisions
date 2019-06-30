package com.debugg3r.android.testdecisions.ui.questions

import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.debugg3r.android.testdecisions.MainActivityActionListener
import com.debugg3r.android.testdecisions.R
import com.debugg3r.android.testdecisions.data.Answer
import com.debugg3r.android.testdecisions.data.DataStoreDb
import com.debugg3r.android.testdecisions.data.Question
import com.debugg3r.android.testdecisions.data.TextItem
import kotlinx.android.synthetic.main.fragment_question.*
import kotlinx.android.synthetic.main.question_list_item.view.*
import kotlinx.android.synthetic.main.question_list_item.view.question_text

class TextItemAdapter(mListener: MainActivityActionListener) : RecyclerView.Adapter<TextItemAdapter.TextItemViewHolder>() {

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
                if (element is Question)
                    onEventListener.performAction("question", element.uid)
                else {
                    itemView.setOnClickListener {
                        //if (this.context == null) return@setOnClickListener
                        val builder = AlertDialog.Builder(itemView.context)
                        val editText = EditText(itemView.context)
                        editText.inputType = InputType.TYPE_CLASS_TEXT
                        editText.setText(element.text)
                        builder.setView(editText)
                        builder.setPositiveButton("Save", { dialog, which ->
                            val text = editText.text.toString()
                            element.text = text
                            DataStoreDb(itemView.context).changeAnswer("", element.uid, element.text)
                            //dataStoreDb?.changeQuestion(param, )
                            bind(element)
                            //question_text.text = editText.text
                        })
                        builder.setNegativeButton("Cancel") { dialog, which ->
                        }
                        builder.show()
                    }
                }
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