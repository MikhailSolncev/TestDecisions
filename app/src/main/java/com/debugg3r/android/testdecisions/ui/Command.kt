package com.debugg3r.android.testdecisions.ui

interface Command {
    fun execute()
    fun execute(parameter: Int)
}
