package com.debugg3r.android.testdecisions.data.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(val context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        val DATABASE_NAME = "testdecision.db"
        val DATABASE_VERSION = 1
    }

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        var sqlQuery = "CREATE TABLE " + DbContract.Questions.TABLE_NAME + " (" +
                DbContract.Questions.COLUMN_ID + " TEXT PRIMARY KEY, " +
                DbContract.Questions.COLUMN_TEXT + " TEXT NOT NULL" +
                ");"
        sqLiteDatabase.execSQL(sqlQuery)

        sqlQuery = "CREATE TABLE " + DbContract.Answers.TABLE_NAME + " (" +
                DbContract.Answers.COLUMN_ID + " TEXT PRIMARY KEY, " +
                DbContract.Answers.COLUMN_QUESTION + " TEXT NOT NULL, " +
                DbContract.Answers.COLUMN_TEXT + " TEXT NOT NULL " +
                ");"
        sqLiteDatabase.execSQL(sqlQuery)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, p1: Int, p2: Int) {
        if (p1 == 1 && p2 > p1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DbContract.Questions.TABLE_NAME)
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DbContract.Answers.TABLE_NAME)
            onCreate(sqLiteDatabase)
        }
    }

}
