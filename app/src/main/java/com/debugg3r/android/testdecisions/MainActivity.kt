package com.debugg3r.android.testdecisions

import android.content.Intent

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MenuFragment.MenuFragmentActionListener {
    private val LOG_TAG = this::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.inTransaction{
            replace(R.id.main_frame, MenuFragment())
        }
    }

    override fun performAction(action: String) {
        Toast.makeText(this, "Wow! Button pressed!", Toast.LENGTH_SHORT).show()
    }


}
