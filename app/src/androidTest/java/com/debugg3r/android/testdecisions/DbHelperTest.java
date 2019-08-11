package com.debugg3r.android.testdecisions;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.debugg3r.android.testdecisions.data.Answer;
import com.debugg3r.android.testdecisions.data.Question;
import com.debugg3r.android.testdecisions.data.sqlite.DbContract;
import com.debugg3r.android.testdecisions.data.sqlite.DbHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DbHelperTest {

    private static final String LOG_TAG = "INDUSTRIAL_TST";
    private final Context mContext = InstrumentationRegistry.getTargetContext();
    /* Class reference to help load the constructor on runtime */
    private final Class mDbHelperClass = DbHelper.class;

    @Before
    public void setUp(){
        deleteDatabase();
    }

    @Test
    public void createDatabase() {
        SQLiteOpenHelper helper = new DbHelper(mContext);
        SQLiteDatabase database = helper.getWritableDatabase();

        assertEquals("Database should be opened", true, database.isOpen());

        Cursor tableNameConstructor = database.rawQuery(
                "select name from sqlite_master where type='table' and name ='" +
                        DbContract.Questions.INSTANCE.getTABLE_NAME() + "'", null);
        assertTrue("Database was not created correctly", tableNameConstructor.moveToFirst());

        assertEquals("Database was not created correctly",
                DbContract.Questions.INSTANCE.getTABLE_NAME(),
                tableNameConstructor.getString(0));
    }

    @Test
    public void insertQuestionRecord() {
        SQLiteOpenHelper helper = new DbHelper(mContext);
        SQLiteDatabase database = helper.getWritableDatabase();

        assertTrue("Databasea should be opened", database.isOpen());

        Question question = new Question("two bee or not two bee?");

        ContentValues values = new ContentValues();
        values.put(DbContract.Questions.INSTANCE.getCOLUMN_ID(), question.getUid());
        values.put(DbContract.Questions.INSTANCE.getCOLUMN_TEXT(), question.getText());

        long rowId = database.insert(DbContract.Questions.INSTANCE.getTABLE_NAME(), null, values);
        assertNotEquals("Question insertion sucks", -1, rowId);

        Cursor cursor = database.rawQuery("select * from " + DbContract.Questions.INSTANCE.getTABLE_NAME(),
                null);

        assertTrue("Database was not created correctly", cursor.moveToFirst());
    }

    @Test
    public void insertAnswerRecord() {
        SQLiteOpenHelper helper = new DbHelper(mContext);
        SQLiteDatabase database = helper.getWritableDatabase();

        assertTrue("Databasea should be opened", database.isOpen());

        Question question = new Question("two bee or not two bee?");

        ContentValues values = new ContentValues();
        values.put(DbContract.Questions.INSTANCE.getCOLUMN_ID(), question.getUid());
        values.put(DbContract.Questions.INSTANCE.getCOLUMN_TEXT(), question.getText());

        long rowId = database.insert(DbContract.Questions.INSTANCE.getTABLE_NAME(), null, values);
        assertNotEquals("Question insertion sucks", -1, rowId);

        Answer answer = new Answer("yes");
        values = new ContentValues();
        values.put(DbContract.Answers.INSTANCE.getCOLUMN_ID(), answer.getUid());
        values.put(DbContract.Answers.INSTANCE.getCOLUMN_QUESTION(), question.getUid());
        values.put(DbContract.Answers.INSTANCE.getCOLUMN_TEXT(), answer.getText());

        rowId = database.insert(DbContract.Answers.INSTANCE.getTABLE_NAME(), null, values);
        assertNotEquals("Answer first insertion sucks", -1, rowId);

        answer = new Answer("bzzzz");
        values = new ContentValues();
        values.put(DbContract.Answers.INSTANCE.getCOLUMN_QUESTION(), question.getUid());
        values.put(DbContract.Answers.INSTANCE.getCOLUMN_ID(), answer.getUid());
        values.put(DbContract.Answers.INSTANCE.getCOLUMN_TEXT(), answer.getText());

        rowId = database.insert(DbContract.Answers.INSTANCE.getTABLE_NAME(), null, values);
        assertNotEquals("second first insertion sucks", -1, rowId);
    }

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
