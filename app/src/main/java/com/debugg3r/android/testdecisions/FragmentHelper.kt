package com.debugg3r.android.testdecisions

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
    val fragmentTransaction = beginTransaction()
    fragmentTransaction.func()
    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
    fragmentTransaction.commit()
}