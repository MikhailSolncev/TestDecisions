package com.debugg3r.android.testdecisions;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

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
        deleteDatabase();
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
}
