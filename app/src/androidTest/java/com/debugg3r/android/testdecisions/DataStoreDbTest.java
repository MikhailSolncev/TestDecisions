package com.debugg3r.android.testdecisions;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.debugg3r.android.testdecisions.data.Answer;
import com.debugg3r.android.testdecisions.data.DataStore;
import com.debugg3r.android.testdecisions.data.DataStoreDb;
import com.debugg3r.android.testdecisions.data.Question;
import com.debugg3r.android.testdecisions.data.sqlite.DbContract;
import com.debugg3r.android.testdecisions.data.sqlite.DbHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DataStoreDbTest {

    private static final String LOG_TAG = "DataStoreDb_TST";
    private final Context mContext = InstrumentationRegistry.getTargetContext();
    private final Class mDbHelperClass = DbHelper.class;

    @Before
    public void setUp(){
        //deleteDatabase();
    }

    @Test
    public void addQuestionTest(){
        DataStore dataStore = new DataStoreDb(mContext);

        Question question = dataStore.addQuestion("two beer or not two beer");
        assertNotNull("added question is null", question);

        String uid = dataStore.addQuestion(new Question("two bee or not two bee"));
        assertFalse("uid is emoty", uid.isEmpty());
    }

    @Test
    public void getQuestionsTest() {
        DataStore dataStore = new DataStoreDb(mContext);
        dataStore.addQuestion("Two bear or not two bear");

        Collection collection = dataStore.getQuestions();
        assertFalse("Question collection is empty", collection.isEmpty());
    }

    @Test
    public void getQuestionTest() {
        DataStore dataStore = new DataStoreDb(mContext);
        Question question = dataStore.addQuestion("Two bear or not two bear");

        Collection collection = dataStore.getQuestions();

        Question question1;
        boolean exception = false;
        try {
            question1 = dataStore.getQuestion(question.getUid());
        } catch (Exception ex) {
            ex.printStackTrace();
            exception = true;
        }
        assertFalse("Exception happened get by uid", exception);

        exception = false;
        try {
            question1 = dataStore.getQuestion("wronguid");
        } catch (Exception ex) {
            exception = true;
        }
        assertTrue("Exception did not happened get by uid", exception);

        exception = false;
        try {
            question1 = dataStore.getQuestion(collection.size() - 1);
        } catch (Exception ex) {
            ex.printStackTrace();
            exception = true;
        }
        assertFalse("Exception happened get by position", exception);

        exception = false;
        try {
            question1 = dataStore.getQuestion(collection.size());
        } catch (Exception ex) {
            exception = true;
        }
        assertTrue("Exception did not happened get by position", exception);
    }

    @Test
    public void addAnswerTest() {
        Class dataStoreClass = null;
        Method method = null;
        try {
            dataStoreClass = DataStoreDb.class;
            method = dataStoreClass.getDeclaredMethod("readFromDb");
            method.setAccessible(true);
        } catch (NoSuchMethodException e) {
            fail("No such method exception");
        }
        DataStoreDb dataStore = null;
        try {
            dataStore = (DataStoreDb) dataStoreClass.getConstructor(Context.class).newInstance(mContext);
        } catch (InstantiationException e) {
            fail("Instantination exception");
        } catch (IllegalAccessException e) {
            fail("Illegal access exception");
        } catch (InvocationTargetException e) {
            fail("Invocation targer exception");
        } catch (NoSuchMethodException e) {
            fail("No such method exception");
        }
        Question question = new Question("two bee or not two bee");
        dataStore.addQuestion(question);

        assertEquals("data store contains not one question", dataStore.getCount(), 1);

        Answer answer = new Answer("Yes");
        dataStore.addAnswer(question, answer);
        answer = new Answer("No");
        dataStore.addAnswer(question, answer);

        assertEquals("Answer contains not two questions 1", question.getAnswers().size(), 2);

        try {
            method.invoke(dataStore);
        } catch (IllegalAccessException e) {
            fail("Illegal access exception");
        } catch (InvocationTargetException e) {
            fail("Invocation target exception");
        }

        assertEquals("Answer contains not two questions 2", question.getAnswers().size(), 2);

        dataStore.removeAnswer(question, answer);

        assertEquals("Answer contains not one questions 1", question.getAnswers().size(), 1);

        try {
            method.invoke(dataStore);
        } catch (IllegalAccessException e) {
            fail("Illegal access exception");
        } catch (InvocationTargetException e) {
            fail("Invocation target exception");
        }

        assertEquals("Answer contains not one questions 2", question.getAnswers().size(), 1);

    }
    //    fun findQuestion(question: Question): Boolean
//    fun removeQuestion(question: Question)
//    fun removeQuestion(uid: String)

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.debugg3r.android.testdecisions", appContext.getPackageName());
    }

    void deleteDatabase() {

        try {
            Field f = mDbHelperClass.getDeclaredField("DATABASE_NAME");
            f.setAccessible(true);
            mContext.deleteDatabase((String) f.get(null));
        } catch (NoSuchFieldException ex) {
            fail("Class dbHelper does not have DATABASE_NAME field");
        } catch (IllegalAccessException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void getDecisionsTest() {
        DataStoreDb dataStore = new DataStoreDb(mContext);

        Question question = dataStore.addQuestion("two beer or not two beer");
        assertNotNull("added question is null", question);

        String uid = dataStore.addQuestion(new Question("two bee or not two bee"));
        assertFalse("uid is empty", uid.isEmpty());

        Question q2 = dataStore.getQuestion(uid);

        Question q3 = dataStore.addQuestion("third");

        dataStore.addAnswer(question, new Answer("yes"));
        dataStore.addAnswer(question, new Answer("no"));

        dataStore.addAnswer(q2, new Answer("maybe"));
        dataStore.addAnswer(q2, new Answer("or maybe not"));

        dataStore.addAnswer(q3, new Answer("third1"));
        dataStore.addAnswer(q3, new Answer("third2"));

        Map result = dataStore.getDecisions();

        assertEquals("decision map isn't equal", 3, result.size());
        assertEquals("Answers 1 isn't equal", 8, ((List)result.get(question)).size());
        assertEquals("Answers 2 isn't equal", 8, ((List)result.get(q2)).size());
        assertEquals("Answers 3 isn't equal", 8, ((List)result.get(q3)).size());

        Log.d("TEST_DECISION", result.toString());
    }

    @Test
    public void updateAnswers() {

        String LOG_TAG = "SQL_TEST_UPDATE";
        DbHelper dbHelper = new DbHelper(mContext);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        sqLiteDatabase.beginTransaction();
        String answersName = DbContract.Answers.INSTANCE.getTABLE_NAME();
        String columnEnabled = DbContract.Answers.INSTANCE.getCOLUMN_ENABLED();

        String alterQuery = "ALTER TABLE " + answersName + " ADD COLUMN " + columnEnabled + " BOOL";
        boolean sucs = false;
//        try {
//            sqLiteDatabase.execSQL(alterQuery);
//            sucs = true;
//        } catch (SQLException ex) {
//            Log.e(LOG_TAG, ex.getMessage());
//        }

        //assertTrue("Add column at answers failed", sucs);

        alterQuery = "UPDATE " + answersName + " SET " + columnEnabled + " = 'true'";
        sucs = false;
        try {
            sqLiteDatabase.execSQL(alterQuery);
            sucs = true;
        } catch (SQLException ex) {
            Log.e(LOG_TAG, ex.getMessage());
        }

        assertTrue("Update column 'Enabled' failed", sucs);

        sqLiteDatabase.endTransaction();

    }

    private void fillDatabase(SQLiteDatabase sqLiteDatabase) {
        ContentValues values = new ContentValues();
        values.put("guid", "123");
        values.put("atext", "first answer");
        values.put("question", "1");
        values.put("enabled", true);
        sqLiteDatabase.insert("answers", null, values);

        values.clear();
        values.put("guid", "456");
        values.put("atext", "first answer");
        values.put("question", "1");
        values.put("enabled", false);
        sqLiteDatabase.insert("answers", null, values);
    }

    @Test
    public void selectAnswersWorksFromQuestionCorrect() {
        String LOG_TAG = "SQL_TEST_UPDATE";
        DbHelper dbHelper = new DbHelper(mContext);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        sqLiteDatabase.beginTransaction();

        fillDatabase(sqLiteDatabase);

        //  SELECT ALL FROM 1 QUESTION

        Cursor cursor = null;
        String sql = "select * from answers where question = ?";

        cursor = sqLiteDatabase.rawQuery(sql, new String[] {"1"});

        assertNotEquals("Raw count is 0 !!!", 0, cursor.getCount());
    }

    @Test
    public void selectAnswersWorksFromEnabledCorrect() {
        String LOG_TAG = "SQL_TEST_UPDATE";
        DbHelper dbHelper = new DbHelper(mContext);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        sqLiteDatabase.beginTransaction();

        fillDatabase(sqLiteDatabase);

        //  SELECT ALL FROM TRUE ANSWER
        String sql = "select * from answers where enabled = $1";

        Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[] {"1"});

        assertEquals("Raw count is not 1 !!!", 1, cursor.getCount());
    }

    @Test
    public void selectAnswersWorksEnabledCorrect() {
        String LOG_TAG = "SQL_TEST_UPDATE";
        DbHelper dbHelper = new DbHelper(mContext);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        sqLiteDatabase.beginTransaction();

        fillDatabase(sqLiteDatabase);

        //  SELECT ALL FROM TRUE ANSWER
        String sql = "select * from answers where enabled = 1";

        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

        assertEquals("Raw count is not 1 !!!", 1, cursor.getCount());
    }

    @Test
    public void selectAnswersParametersWorks() {
        String LOG_TAG = "SQL_TEST_UPDATE";
        DbHelper dbHelper = new DbHelper(mContext);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        sqLiteDatabase.beginTransaction();

        fillDatabase(sqLiteDatabase);

        String sql = "SELECT guid, atext " +
                "FROM answers " +
                "WHERE question = ?" +
                " AND (? = '0')";

        Cursor cursor = null;
        try {
            cursor = sqLiteDatabase.rawQuery(sql, new String[] {"1", "0"});
        } catch (SQLException ex) {
            Log.e(LOG_TAG, ex.getMessage());
        }

        assertNotNull("Cursor is null", cursor);

        assertEquals("Row count not 2", 2, cursor.getCount());

    }

    @Test
    public void selectAnswersWorksOnlyEnabledCorrect() {
        String LOG_TAG = "SQL_TEST_UPDATE";
        DbHelper dbHelper = new DbHelper(mContext);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        sqLiteDatabase.beginTransaction();

        fillDatabase(sqLiteDatabase);

        String sql = "SELECT guid, atext " +
                "FROM answers " +
                "WHERE question = ?" +
                " AND (? = 0 OR enabled = 1)";

        Cursor cursor = null;
        try {
            cursor = sqLiteDatabase.rawQuery(sql, new String[] {"1", "0"});
        } catch (SQLException ex) {
            Log.e(LOG_TAG, ex.getMessage());
        }

        assertNotNull("Cursor is null", cursor);

        assertEquals("Row count not 2", 2, cursor.getCount());

    }

}
