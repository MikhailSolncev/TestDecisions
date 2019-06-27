package com.debugg3r.android.testdecisions.ui.mainmenu


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.debugg3r.android.testdecisions.MainActivityActionListener
import com.debugg3r.android.testdecisions.R
import com.debugg3r.android.testdecisions.data.DataStoreDb
import kotlinx.android.synthetic.main.fragment_menu.*


class MenuFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val parentActivity = activity

        main_button_questions.setOnClickListener {
            if (parentActivity is MainActivityActionListener) {
                parentActivity.performAction("questions", "")
            }
        }

        val task = Thread{
            val dataStore = parentActivity?.let { DataStoreDb(it) }
            val count = dataStore?.getCount()
            parentActivity?.runOnUiThread{
                main_textview_questions.text = count.toString()
            }
        }
        task.start()
    }

}
