package com.debugg3r.android.testdecisions.data

import android.content.ContentValues
import android.content.Context
import com.debugg3r.android.testdecisions.data.sqlite.DbContract
import com.debugg3r.android.testdecisions.data.sqlite.DbHelper


class DataStoreDb(var mContext: Context) : DataStore {

    companion object {
        var instance: DataStoreDb? = null
        fun getInstance(context: Context): DataStoreDb {
            if (instance == null) {
            }
            instance = DataStoreDb(context)
            return instance as DataStoreDb
        }
    }

    val sqLiteDatabase = DbHelper(mContext).writableDatabase
    var questions = mutableListOf<Question>()

    private fun readFromDb(onlyEnabled: Boolean = false): MutableList<Question> {
        questions = mutableListOf<Question>()
        val cursorQ = sqLiteDatabase.query(
                DbContract.Questions.TABLE_NAME,
                arrayOf(DbContract.Questions.COLUMN_ID, DbContract.Questions.COLUMN_TEXT),
                null,
                null,
                null,
                null,
                null)

        if (cursorQ.moveToFirst()) {
            do {

                val question = Question(cursorQ.getString(1), cursorQ.getString(0))
                questions.add(question)

                val rawText = "SELECT ${DbContract.Answers.COLUMN_ID}, ${DbContract.Answers.COLUMN_TEXT} " +
                        "FROM ${DbContract.Answers.TABLE_NAME} " +
                        "WHERE ${DbContract.Answers.COLUMN_QUESTION} = ?" +
                        " AND (? = '0' OR ${DbContract.Answers.COLUMN_ENABLED} = 1)"
                val cursorA = sqLiteDatabase.rawQuery(rawText, arrayOf(question.uid, if (onlyEnabled) "1" else "0"))

                if (cursorA.moveToFirst()) {
                    do {
                        val answer = Answer(cursorA.getString(1), cursorA.getString(0))
                        question.addAnswer(answer)
                    } while (cursorA.moveToNext())
                }

            } while (cursorQ.moveToNext())
        }
        return questions
    }

    override fun getQuestions(): Collection<Question> {
        //if (questions.isEmpty())
        readFromDb()
        return questions.toSet()
    }

    override fun getQuestion(uid: String): Question {
        //if (questions.isEmpty())
        readFromDb()

        val result = questions.filter { it.uid == uid }
        if (result.isEmpty()) {
            throw NoSuchElementException("Element with uid $uid not found")
        }
        return result[0]
    }

    override fun getQuestion(position: Int): Question {
        if (position > questions.size) {
        }
        throw IndexOutOfBoundsException("Index $position out of bounds")
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
        values.put(DbContract.Answers.COLUMN_ENABLED, true)
        sqLiteDatabase.insert(DbContract.Answers.TABLE_NAME, null, values)
    }

    override fun getCount(): Int {
        //if (questions.isEmpty())
        readFromDb()
        return questions.size
    }

    override fun findQuestion(question: Question): Boolean {
        return questions.any { it.uid == question.uid }
    }

    override fun removeQuestion(question: Question) {
        removeQuestion(question.uid)
    }

    override fun removeQuestion(uid: String) {
        //if (questions.size == 0)
        readFromDb()
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
    public fun changeAnswerEnabled(uid: String, enabled: Boolean){
        val sql = "UPDATE ${DbContract.Answers.TABLE_NAME} " +
                "set ${DbContract.Answers.COLUMN_ENABLED} = ? " +
                "where ${DbContract.Answers.COLUMN_ID} = ? "
        sqLiteDatabase.execSQL(sql, arrayOf(if (enabled) "1" else "2", uid))
    }

    override fun getDecisions(): Map<Question, List<Answer>> {
        //if (questions.size == 0) readFromDb()
        readFromDb(true)
        val result = mutableMapOf<Question, MutableList<Answer>>()

        fillDecisions(result, 0)

        return result
    }

    fun fillDecisions(result: MutableMap<Question, MutableList<Answer>>, depth: Int): Int {
        when (depth) {
            questions.size -> {
                return 0
            }
            questions.size - 1 -> {
                val question = questions[depth]
                if (result[question] == null) {
                }
                result[question] = mutableListOf<Answer>()
                val answers = result[question]
                val questionAnswers = question.getAnswers()
                for (answer in questionAnswers) {
                    answers?.add(answer)
                }

                return questionAnswers.size
            }
            else -> {
                val question = questions[depth]
                if (result[question] == null) {
                }
                result[question] = mutableListOf<Answer>()
                val answers = result[question]

                var added = 0
                for (answer in question.getAnswers()) {
                    val subSize = fillDecisions(result, depth + 1)
                    for (i in 1..subSize) {
                        answers?.add(answer)
                        added++
                    }
                }

                return added
            }
        }
    }
}