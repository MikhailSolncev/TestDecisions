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
import com.debugg3r.android.testdecisions.data.Question
import kotlinx.android.synthetic.main.layout_list_item_addnew.view.*

enum class TextItemViewType(value: Int) {
    BUTTON(1), TEXT(2), EDITOR (3)
}

class QuestionListAdapter: RecyclerView.Adapter<QuestionListAdapter.ViewHolder> {
    var presenter: QuestionPresenterInterface?

    constructor(presenter: QuestionPresenterInterface) {
        this.presenter = presenter
    }

    val LOG_TAG = this::class.java.canonicalName
    var currentEditing: Int? = null
    var answerListAdapter: AnswerListAdapter? = null

    override fun onBindViewHolder(holder: QuestionListAdapter.ViewHolder, position: Int) {
        if (position < 0) return
        val questionCount: Int = presenter?.getQuestionCount() ?: 0//if (presenter == null) 0 else

        Log.d(LOG_TAG, "list position: ${position} with list size: ${questionCount}")

        var question: Question? = if (position < questionCount) presenter?.getQuestion(position) else null

        holder.tv_item.setTag(R.integer.tag_holder, holder)
        holder.tv_item.setTag(R.integer.tag_uid, question?.uid)
        holder.et_item.setTag(R.integer.tag_holder, holder)
        holder.et_item.setTag(R.integer.tag_uid, question?.uid)
        holder.btn_add.setTag(R.integer.tag_holder, holder)
        holder.btn_add.setTag(R.integer.tag_uid, question?.uid)

        var targetVisibility = if (position == questionCount) View.VISIBLE else View.GONE
        if (holder.btn_add.visibility != targetVisibility)
            holder.btn_add.visibility = targetVisibility

        if (holder.btn_add.visibility == View.VISIBLE) {
            holder.btn_add.setOnClickListener { view -> onBtnClick(view) }
        }

        targetVisibility = if (position < questionCount && position != currentEditing) View.VISIBLE else View.GONE
        if (holder.tv_item.visibility != targetVisibility)
            holder.tv_item.visibility = targetVisibility

        if (holder.tv_item.visibility == View.VISIBLE) {
            holder.tv_item.setOnClickListener { view -> onTvClick(view) }
            holder.tv_item.setOnLongClickListener { view -> onLongClick(view) }
        }

        targetVisibility = if (position == currentEditing) View.VISIBLE else View.GONE
        if (holder.et_item.visibility != targetVisibility)
            holder.et_item.visibility = targetVisibility

        if (holder.et_item.visibility == View.VISIBLE) {
            holder.et_item.setTag(R.integer.tag_onBind, true)
            holder.et_item.setOnEditorActionListener { view, actionId, event -> onEditorAction(view, actionId, event) }
            holder.et_item.setOnFocusChangeListener { view, hasFocus -> onFocusChange(view, hasFocus) }
            holder.et_item.setTag(R.integer.tag_onBind, false)
        }

        if (position < questionCount) {
            holder.et_item.setText(question?.text)
            holder.tv_item.setText(question?.text)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return TextItemViewType.TEXT.ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionListAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.layout_list_item_addnew, parent, false)

        return ViewHolder(view)
    }

    private fun onFocusChange(view: View, hasFocus: Boolean) {
        if (view.getTag(R.integer.tag_onBind) as Boolean == true) return

        val holder = view.getTag(R.integer.tag_holder) as ViewHolder
        if (view.visibility == View.VISIBLE && !hasFocus)
            notifyItemChanged(holder.adapterPosition)
    }

    override fun getItemCount(): Int {
        return 1 + (presenter?.getQuestionCount() ?: 0)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val btn_add = itemView.btn_add_text_item
        val tv_item = itemView.tv_text_item
        val et_item = itemView.et_text_item
    }

    fun onEditorAction(view: TextView, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            val position: Int? = currentEditing
            currentEditing = null

            if (position != null) {
                Log.d(LOG_TAG, "Item changed in position " + position)
                val question = presenter?.getQuestion(position)
                question?.text = view.text.toString()

                if (question?.text?.isEmpty() ?: false) {
                    view.setTag(R.integer.tag_onBind, true)
                    presenter?.removeQuestion(question!!)
                    notifyDataSetChanged()
                } else {
                    notifyItemChanged(position)
                }
            }
            return true
        }
        return false
    }

    fun onBtnClick(view: View) {
        dropEditing()

        var question = Question("")
        presenter?.addQuestion(question)

        answerListAdapter?.question = question
        answerListAdapter?.notifyDataSetChanged()

        val holder = view.getTag(R.integer.tag_holder) as ViewHolder
        currentEditing = holder.adapterPosition

        notifyDataSetChanged()
    }

    fun onTvClick(view: View) {
        dropEditing()

        var question = presenter?.getQuestion(view.getTag(R.integer.tag_uid) as String)
        answerListAdapter?.question = question
        answerListAdapter?.notifyDataSetChanged()
    }

    fun onLongClick(view: View): Boolean {
        dropEditing()

        var question = presenter?.getQuestion(view.getTag(R.integer.tag_uid) as String)
        answerListAdapter?.question = question
        answerListAdapter?.notifyDataSetChanged()

        val holder = view.getTag(R.integer.tag_holder) as ViewHolder
        currentEditing = holder.adapterPosition
        Log.d(LOG_TAG, "Long click performed")
        if (currentEditing != null)
            onBindViewHolder(holder, holder.adapterPosition)
        return true
    }

    fun dropEditing() {
        if (currentEditing != null) {
            val curr = currentEditing ?: 0
            currentEditing = null
            notifyItemChanged(curr)
        }
    }

}
