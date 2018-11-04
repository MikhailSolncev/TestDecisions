package com.debugg3r.android.testdecisions.ui

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.debugg3r.android.testdecisions.R
import com.debugg3r.android.testdecisions.data.TextItem
import kotlinx.android.synthetic.main.layout_list_item_addnew.view.*

enum class TextItemViewType(value: Int) {
    BUTTON(1), TEXT(2), EDITOR (3)
}

class TextItemListAdapter: RecyclerView.Adapter<TextItemListAdapter.ViewHolder> {
    var presenter:QuestionPresenterInterface?

    constructor(presenter: QuestionPresenterInterface) {

        this.presenter = presenter
    }

    val LOG_TAG = this::class.java.canonicalName

    var currentEditing: Int? = null

    override fun onBindViewHolder(holder: TextItemListAdapter.ViewHolder, position: Int) {
        val questionCount: Int = presenter?.getQuestionCount() ?: 0//if (presenter == null) 0 else

        Log.d(LOG_TAG, "list position: ${position} with list size: ${questionCount}")

        var targetVisibility = if (position == presenter?.getQuestionCount()) View.VISIBLE else View.GONE
        if (holder.btn_add.visibility != targetVisibility)
                holder.btn_add.visibility = targetVisibility

        targetVisibility = if (position < questionCount && position != currentEditing) View.VISIBLE else View.GONE
        if (holder.tv_item.visibility != targetVisibility)
            holder.tv_item.visibility = targetVisibility

        targetVisibility = if (position == currentEditing) View.VISIBLE else View.GONE
        if (holder.et_item.visibility != targetVisibility)
            holder.et_item.visibility = targetVisibility

        if (position < questionCount) {
            holder.et_item.setText(presenter?.getQuestion(position)?.text)
            holder.tv_item.setText(presenter?.getQuestion(position)?.text)
        }
    }

    override fun getItemViewType(position: Int): Int {
//        if (position == itemList.size)
//            return TextItemViewType.BUTTON.ordinal

        return TextItemViewType.TEXT.ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemListAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.layout_list_item_addnew, parent, false)

        view.et_text_item.setOnEditorActionListener{ view, actionId, event -> onEditorAction(view, actionId, event) }
        view.tv_text_item.setOnClickListener{ view -> onClick(view) }

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
//        if (presenter == null)
//            return 1
        return 1 + (presenter?.getQuestionCount() ?: 0)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val btn_add = itemView.btn_add_text_item
        val tv_item = itemView.tv_text_item
        val et_item = itemView.et_text_item
    }

    fun onEditorAction(view: TextView, actionId: Int, event: KeyEvent): Boolean {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            val position: Int? = currentEditing
            currentEditing = null

            if (position != null) {
                val question = presenter?.getQuestion(position)
                question?.text = view.text as String
                notifyItemChanged(position)
            }
            return true
        }
        return false
    }

    fun onClick(view: View) {

    }

}
