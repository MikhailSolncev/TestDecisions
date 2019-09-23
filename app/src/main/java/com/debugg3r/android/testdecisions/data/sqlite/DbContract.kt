package com.debugg3r.android.testdecisions.data.sqlite

import android.provider.BaseColumns

class DbContract : BaseColumns {
    object Questions{
        val TABLE_NAME = "questions"
        val COLUMN_ID = "guid"
        val COLUMN_TEXT = "qtext"
    }
    object Answers{
        val TABLE_NAME = "answers"
        val COLUMN_ID = "guid"
        val COLUMN_QUESTION = "question"
        val COLUMN_TEXT = "atext"
        val COLUMN_ENABLED = "enabled"
    }
}