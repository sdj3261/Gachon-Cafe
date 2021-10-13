package org.androidtown.seterm;

import org.junit.Test;
import static org.junit.Assert.*;


/**
 * class that tests time information is converted into string well
 */
public class TestConvert {



    @Test
    public void convert()
    {

        int[] hourOfDay = new int[3];
        hourOfDay[0] = 9;
        hourOfDay[1] = 12;
        hourOfDay[2] = 14;
        String[] AmPm = new String[3];
        for (int i = 0; i < 3; i++) {
            if (hourOfDay[i] > 12) {
                hourOfDay[i] = hourOfDay[i] - 12;
                AmPm[i] = "오후";
            } else {
                if (hourOfDay[i] == 12)
                    AmPm[i] = "오후";
                else
                    AmPm[i] = "오전";
            }
        }

        assertEquals(hourOfDay[0],9);
        assertEquals(hourOfDay[1],12);
        assertEquals(hourOfDay[2],2);
        assertEquals(AmPm[0],"오전");
        assertEquals(AmPm[1],"오후");
        assertEquals(AmPm[2],"오후");

    }
}
