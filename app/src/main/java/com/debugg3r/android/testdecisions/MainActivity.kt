package com.debugg3r.android.testdecisions

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.debugg3r.android.testdecisions.ui.mainmenu.MenuFragment
import com.debugg3r.android.testdecisions.ui.questions.QuestionsFragment

class MainActivity : AppCompatActivity(), MainActivityActionListener {
    private val LOG_TAG = this::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.inTransaction{
            replace(R.id.main_frame, MenuFragment())
        }
    }

    override fun performAction(action: String) {
        Toast.makeText(this, "Wow! Button \"$action\" pressed!", Toast.LENGTH_SHORT).show()
        if (action == "questions") {
            supportFragmentManager.inTransaction {
                addToBackStack("main")
                replace(R.id.main_frame, QuestionsFragment())
            }

        }
    }
}

interface MainActivityActionListener {
    fun performAction(action: String)
}