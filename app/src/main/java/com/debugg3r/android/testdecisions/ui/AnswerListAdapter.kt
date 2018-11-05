package com.debugg3r.android.testdecisions.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.debugg3r.android.testdecisions.R
import com.debugg3r.android.testdecisions.data.Answer
import com.debugg3r.android.testdecisions.data.Question
import kotlinx.android.synthetic.main.layout_list_item_addnew.view.*
import java.lang.Exception
import android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager


class AnswerListAdapter: RecyclerView.Adapter<AnswerListAdapter.ViewHolder> {
    var presenter: QuestionPresenterInterface?

    constructor(presenter: QuestionPresenterInterface) {
        this.presenter = presenter
    }

    val LOG_TAG = this::class.java.canonicalName
    var currentEditing: Int? = null
    var question: Question? = null
    var context: Context? = null

    override fun onBindViewHolder(holder: AnswerListAdapter.ViewHolder, position: Int) {
        if (position < 0) return
        val answersCount: Int = presenter?.getAnswersCount(question) ?: 0//if (presenter == null) 0 else

        Log.d(LOG_TAG, "list position: ${position} with list size: ${answersCount}")

        val answer: Answer? = if (position < answersCount) presenter?.getAnswer(question, position) else null

        holder.tv_item.setTag(R.integer.tag_holder, holder)
        holder.tv_item.setTag(R.integer.tag_uid, answer?.uid)
        holder.et_item.setTag(R.integer.tag_holder, holder)
        holder.et_item.setTag(R.integer.tag_uid, answer?.uid)
        holder.btn_add.setTag(R.integer.tag_holder, holder)
        holder.btn_add.setTag(R.integer.tag_uid, answer?.uid)

        var targetVisibility = if (position == answersCount) View.VISIBLE else View.GONE
        if (holder.btn_add.visibility != targetVisibility)
            holder.btn_add.visibility = targetVisibility

        if (holder.btn_add.visibility == View.VISIBLE) {
            holder.btn_add.setOnClickListener { view -> onBtnClick(view) }
        }

        targetVisibility = if (position < answersCount && position != currentEditing) View.VISIBLE else View.GONE
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
            holder.et_item.requestFocus()
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm?.showSoftInput(holder.et_item, InputMethodManager.SHOW_IMPLICIT)

            holder.et_item.setOnEditorActionListener { view, actionId, event -> onEditorAction(view, actionId, event) }
            holder.et_item.setOnFocusChangeListener { view, hasFocus -> onFocusChange(view, hasFocus) }
            holder.et_item.setTag(R.integer.tag_onBind, false)
        }

        if (position < answersCount) {
            holder.et_item.setText(answer?.text)
            holder.tv_item.setText(answer?.text)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return TextItemViewType.TEXT.ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerListAdapter.ViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.layout_list_item_addnew, parent, false)

        return ViewHolder(view)
    }

    private fun onFocusChange(view: View, hasFocus: Boolean) {
        if (view.getTag(R.integer.tag_onBind) as Boolean == true) return

        val holder = view.getTag(R.integer.tag_holder) as ViewHolder
        if (view.visibility == View.VISIBLE && !hasFocus) {
            try {
                notifyItemChanged(holder.adapterPosition)
            } catch (e: Exception) {
                // it's ok
            }
        }
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
                val answer = presenter?.getAnswer(question, position)
                answer?.text = view.text.toString()

                if (answer?.text?.isEmpty() ?: false) {
                    view.setTag(R.integer.tag_onBind, true)
                    presenter?.removeAnswer(question, answer!!)
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

        var answer = Answer("")
        presenter?.addAnswer(question, answer)

        val holder = view.getTag(R.integer.tag_holder) as ViewHolder
        currentEditing = holder.adapterPosition

        notifyDataSetChanged()
    }

    fun onTvClick(view: View) {
        dropEditing()
    }

    fun onLongClick(view: View): Boolean {
        dropEditing()
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
