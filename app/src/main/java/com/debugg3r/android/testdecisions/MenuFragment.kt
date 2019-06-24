package com.debugg3r.android.testdecisions


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.debugg3r.android.testdecisions.data.DataStoreDb
import kotlinx.android.synthetic.main.fragment_menu.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class MenuFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var parentActivity = activity

        main_button_questions.setOnClickListener {
            if (parentActivity is MenuFragmentActionListener) {
                parentActivity.performAction("questions")
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

    override fun onStart() {
        super.onStart()

    }

    interface MenuFragmentActionListener {
        fun performAction(action: String)
    }

}
