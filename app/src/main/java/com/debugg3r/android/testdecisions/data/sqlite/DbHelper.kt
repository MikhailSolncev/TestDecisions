package com.debugg3r.android.testdecisions.data.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(val context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        val DATABASE_NAME = "testdecision.db"
        val DATABASE_VERSION = 2
    }

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        var sqlQuery = getCreateQuestionSql()
        sqLiteDatabase.execSQL(sqlQuery)

        sqlQuery = getCreateAnswerSql()
        sqLiteDatabase.execSQL(sqlQuery)
    }

    private fun getCreateAnswerSql(): String {
        return "CREATE TABLE " + DbContract.Answers.TABLE_NAME + " (" +
                DbContract.Answers.COLUMN_ID + " TEXT PRIMARY KEY, " +
                DbContract.Answers.COLUMN_QUESTION + " TEXT NOT NULL, " +
                DbContract.Answers.COLUMN_TEXT + " TEXT NOT NULL, " +
                DbContract.Answers.COLUMN_ENABLED + " BOOL NOT NULL" +
                ");"
    }

    private fun getCreateQuestionSql(): String {
        return "CREATE TABLE " + DbContract.Questions.TABLE_NAME + " (" +
                DbContract.Questions.COLUMN_ID + " TEXT PRIMARY KEY, " +
                DbContract.Questions.COLUMN_TEXT + " TEXT NOT NULL" +
                ");"
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, p1: Int, p2: Int) {
        if (p2 == p1) return

        when (p1) {
            0 -> {
                val answersName = DbContract.Answers.TABLE_NAME
                val questionName = DbContract.Questions.TABLE_NAME
                with(sqLiteDatabase) {
                    execSQL("DROP TABLE IF EXISTS _$questionName")
                    execSQL("DROP TABLE IF EXISTS _$answersName")
                    onCreate(this)
                }
            }

            1 -> {
                val answersName = DbContract.Answers.TABLE_NAME
                val columnEnabled = DbContract.Answers.COLUMN_ENABLED
                var alterQuery = "ALTER TABLE $answersName ADD COLUMN $columnEnabled BOOL"
                sqLiteDatabase.execSQL(alterQuery)
                alterQuery = "UPDATE $answersName SET $columnEnabled = 'TRUE'"
                sqLiteDatabase.execSQL(alterQuery)
            }
            2 -> {}
        }
    }

}
