package com.debugg3r.android.testdecisions.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.debugg3r.android.testdecisions.R
import com.debugg3r.android.testdecisions.data.Answer
import com.debugg3r.android.testdecisions.data.ListDataset
import com.debugg3r.android.testdecisions.data.TextItem
import kotlinx.android.synthetic.main.layout_list_item_addnew.view.*

class TextListAdapterInterface(private var dataset: ListDataset) : RecyclerView.Adapter<TextListAdapterInterface.ViewHolder>() {

    val LOG_TAG: String = this::class.java.name

    var currentEditing: Int = -1
    var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.layout_list_item_addnew, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 1 + dataset.getCount()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val btn_add = itemView.btn_add_text_item
        val tv_item = itemView.tv_text_item
        val et_item = itemView.et_text_item
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position < 0) return

        Log.d(LOG_TAG, "List position ${position} from ${dataset.getCount()} list size")

        var textItem : TextItem? = if (position < dataset.getCount()) dataset.getElement(position) else null

        holder.tv_item.setTag(R.integer.tag_holder, holder)
        holder.tv_item.setTag(R.integer.tag_uid, textItem?.uid)
        holder.et_item.setTag(R.integer.tag_holder, holder)
        holder.et_item.setTag(R.integer.tag_uid, textItem?.uid)
        holder.btn_add.setTag(R.integer.tag_holder, holder)
        holder.btn_add.setTag(R.integer.tag_uid, textItem?.uid)

        updateVisibility(holder)
    }

    private fun updateVisibility(holder: ViewHolder) {
        val position = holder.adapterPosition

        //  button add
        var targetVisibility = if (position == dataset.getCount()) View.VISIBLE else View.GONE
        if (holder.btn_add.visibility != targetVisibility)
            holder.btn_add.visibility = targetVisibility

        if (holder.btn_add.visibility == View.VISIBLE) {
            holder.btn_add.setOnClickListener { view -> onBtnClick(view) }
        }

        //  text view
        targetVisibility = if (position < dataset.getCount() && position != currentEditing) View.VISIBLE else View.GONE
        if (holder.tv_item.visibility != targetVisibility)
            holder.tv_item.visibility = targetVisibility

        if (holder.tv_item.visibility == View.VISIBLE) {
            holder.tv_item.setOnClickListener { view -> onTvClick(view) }
            holder.tv_item.setOnLongClickListener { view -> onLongClick(view) }
        }

        //  edit text
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
    }

    fun dropEditing() {
        if (currentEditing >= 0) {
            val curr = currentEditing
            currentEditing = -1
            notifyItemChanged(curr)
        }
    }

    fun onBtnClick(view: View) {
        dropEditing()

        var element: TextItem = dataset.newItem("")
        //presenter?.addAnswer(question, answer)

        val holder = view.getTag(R.integer.tag_holder) as AnswerListAdapter.ViewHolder
        currentEditing = holder.adapterPosition

        notifyDataSetChanged()
    }

    fun onTvClick(view: View) {
        dropEditing()
    }

    fun onLongClick(view: View): Boolean {
        dropEditing()
        val holder = view.getTag(R.integer.tag_holder) as TextListAdapterInterface.ViewHolder
        currentEditing = holder.adapterPosition
        Log.d(LOG_TAG, "Long click performed")
        if (currentEditing >= 0)
            onBindViewHolder(holder, holder.adapterPosition)
        return true
    }

    fun onEditorAction(view: TextView, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            val position = currentEditing
            currentEditing = -1

            if (position >= 0) {
                Log.d(LOG_TAG, "Item changed in position " + position)
                val element = dataset.getElement(position)
                element.text = view.text.toString()

                if (element.text.isEmpty()) {
                    view.setTag(R.integer.tag_onBind, true)
                    dataset.deleteElement(position)
                    notifyDataSetChanged()
                } else {
                    notifyItemChanged(position)
                }
            }
            return true
        }
        return false
    }

    private fun onFocusChange(view: View, hasFocus: Boolean) {
        if (view.getTag(R.integer.tag_onBind) as Boolean == true) return

        val holder = view.getTag(R.integer.tag_holder) as TextListAdapterInterface.ViewHolder
        if (view.visibility == View.VISIBLE && !hasFocus) {
            updateVisibility(holder)
        }
    }

}