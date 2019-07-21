package com.debugg3r.android.testdecisions.data

import android.content.ContentValues
import android.content.Context
import com.debugg3r.android.testdecisions.data.sqlite.DbContract
import com.debugg3r.android.testdecisions.data.sqlite.DbHelper

class DataStoreDb(var mContext: Context): DataStore {
    val sqLiteDatabase = DbHelper(mContext).writableDatabase
    var questions = mutableListOf<Question>()

    private fun readFromDb() : MutableList<Question> {
        questions = mutableListOf<Question>()
        var cursorQ = sqLiteDatabase.rawQuery(
                "select " +
                        "${DbContract.Questions.COLUMN_ID}," +
                        "${DbContract.Questions.COLUMN_TEXT} " +
                        "from ${DbContract.Questions.TABLE_NAME}", null)
        if (cursorQ.moveToFirst())
            do {

                var question = Question(cursorQ.getString(1), cursorQ.getString(0))
                questions.add(question)

                val cursorA = sqLiteDatabase.query(DbContract.Answers.TABLE_NAME,
                        arrayOf(DbContract.Answers.COLUMN_ID, DbContract.Answers.COLUMN_TEXT),
                        "${DbContract.Answers.COLUMN_QUESTION} = ?",
                        arrayOf(question.uid),
                        null,
                        null,
                        null)

                if (cursorA.moveToFirst())
                    do {
                        var answer = Answer(cursorA.getString(1), cursorA.getString(0))
                        question.addAnswer(answer)
                    } while (cursorA.moveToNext())

            } while (cursorQ.moveToNext())
        return questions
    }

    override fun getQuestions(): Collection<Question> {
        if (questions.isEmpty()) readFromDb()
        return questions.toSet()
    }

    override fun getQuestion(uid: String): Question {
        if (questions.isEmpty()) readFromDb()

        val result = questions.filter { it.uid == uid }
        if (result.isEmpty())
            throw NoSuchElementException("Element with uid $uid not found")
        return result[0]
    }

    override fun getQuestion(position: Int): Question {
        if (position > questions.size)
            throw IndexOutOfBoundsException("Index $position out of bounds")
        return questions[position]
    }

    override fun addQuestion(question: Question): String {
        val values = ContentValues()
        values.put(DbContract.Questions.COLUMN_ID, question.uid)
        values.put(DbContract.Questions.COLUMN_TEXT, question.text)
        sqLiteDatabase.insert(DbContract.Questions.TABLE_NAME, null, values)
        return question.uid
    }

    override fun addQuestion(text: String): Question {
        val question = Question(text)
        addQuestion(question)
        return question
    }

    fun addAnswer(question: Question, answer: Answer) {
        question.addAnswer(answer)
        val values = ContentValues()
        values.put(DbContract.Answers.COLUMN_ID, answer.uid)
        values.put(DbContract.Answers.COLUMN_TEXT, answer.text)
        values.put(DbContract.Answers.COLUMN_QUESTION, question.uid)
        sqLiteDatabase.insert(DbContract.Answers.TABLE_NAME, null, values)
    }

    override fun getCount(): Int {
        if (questions.isEmpty()) readFromDb()
        return questions.size
    }

    override fun findQuestion(question: Question): Boolean {
        return questions.any { it.uid == question.uid }
    }

    override fun removeQuestion(question: Question) {
        removeQuestion(question.uid)
    }

    override fun removeQuestion(uid: String) {
        if (questions.size == 0) readFromDb()
        if (findQuestion(Question("", uid))) {
            sqLiteDatabase.delete(DbContract.Questions.TABLE_NAME,
                    "${DbContract.Questions.COLUMN_ID} = ?",
                    arrayOf(uid))
            questions.remove(questions.first { it.uid == uid })
        } else throw NoSuchElementException("Element with uid $uid not found")
    }

    fun removeAnswer(question: Question, answer: Answer) {
        question.removeAnswer(answer.uid)
        sqLiteDatabase.delete(DbContract.Answers.TABLE_NAME,
                "${DbContract.Answers.COLUMN_ID} = ?",
                arrayOf(answer.uid))
    }

    override fun changeQuestion(question: Question, text: String) {
        changeQuestion(question.uid, text)
    }

    override fun changeQuestion(uid: String, text: String) {
        questions.first { it.uid == uid }.text = text
        val sql = "update ${DbContract.Questions.TABLE_NAME} " +
                "set ${DbContract.Questions.COLUMN_TEXT} = '$text' " +
                "where ${DbContract.Questions.COLUMN_ID} = ? "
        sqLiteDatabase.execSQL(sql, arrayOf(uid))
    }

    override fun changeAnswer(question: Question, answer: Answer, text: String) {
        changeAnswer(question.uid, answer.uid, text)
    }

    override fun changeAnswer(uidQuestion: String, uidAnswer: String, text: String) {
        //questions.first { it.uid == uidQuestion }.getAnswers().first { it.uid == uidAnswer }.text = text
        val sql = "update ${DbContract.Answers.TABLE_NAME} " +
                "set ${DbContract.Answers.COLUMN_TEXT} = '$text' " +
                "where ${DbContract.Answers.COLUMN_ID} = ? "
        sqLiteDatabase.execSQL(sql, arrayOf(uidAnswer))
    }
}