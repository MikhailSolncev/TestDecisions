package com.debugg3r.android.testdecisions.data

import java.util.*

data class Answer(override var text: String, override val uid:String, val enabled: Boolean = true): TextItem {
    constructor(text: String):this(text, UUID.randomUUID().toString(), true)
}