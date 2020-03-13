package com.alirnp.alichtimerview;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    float mIntegerMinValue = 10;
    float mIntegerMaxValue = 20;

    @Test
    public void per() {

        float value = 15;

        float ex3 = (value - mIntegerMinValue) * getDegreePerHand();

        assertEquals(180, ex3, 0);


    }

    private float getDegreePerHand() {
        return 360 / (float) getHandCount();
    }

    private float getHandCount() {
        float left = (mIntegerMaxValue - mIntegerMinValue);
        return (float) left % 2 == 0 ? left : left + 1;
    }
}