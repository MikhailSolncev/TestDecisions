package com.debugg3r.android.testdecisions.data

import java.util.*

data class Answer(override var text: String): TextItem {
    override val uid: String = UUID.randomUUID().toString()
}