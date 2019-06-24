package com.debugg3r.android.testdecisions;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);

        TestOdd testOdd = new TestOdd();
        Integer arr[] = {1,2,3,4,5,65,66,67,68,69};
        testOdd.printOdd(arr);
    }
}