package com.debugg3r.android.testdecisions

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.debugg3r.android.testdecisions.ui.mainmenu.MenuFragment
import com.debugg3r.android.testdecisions.ui.questions.QuestionDetailFragment
import com.debugg3r.android.testdecisions.ui.questions.QuestionsFragment
import com.debugg3r.android.testdecisions.ui.decisions.DecisionsFragment

class MainActivity : AppCompatActivity(), MainActivityActionListener {
    private val LOG_TAG = this::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportFragmentManager.inTransaction{
            replace(R.id.main_frame, MenuFragment())
        }
    }

    override fun onNavigateUp(): Boolean {
        super.onNavigateUp()

        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onSupportNavigateUp()
        if (supportFragmentManager.backStackEntryCount > 0)
            supportFragmentManager.popBackStack()
        else
            finish()
        return true
    }

    override fun performAction(action: String, parameter: String) {
        //Toast.makeText(this, "Wow! Button \"$action\" pressed!", Toast.LENGTH_SHORT).show()

        when (action) {
            "questions" -> {
                supportFragmentManager.inTransaction {
                    addToBackStack("main")
                    replace(R.id.main_frame, QuestionsFragment())
                }
            }
            "question" -> {
                supportFragmentManager.inTransaction {
                    addToBackStack("questions")
                    replace(R.id.main_frame, QuestionDetailFragment.newInstance(parameter))
                }
            }
            "answer" -> {

            }
            "decisions" -> {
                supportFragmentManager.inTransaction {
                    addToBackStack("main")
                    replace(R.id.main_frame, DecisionsFragment.newInstance())
                }
            }
            else -> {}
        }
    }
}

interface MainActivityActionListener {
    fun performAction(action: String, parameter: String)
}