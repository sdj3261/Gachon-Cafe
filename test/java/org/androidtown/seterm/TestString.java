package org.androidtown.seterm;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * class that tests order information(string)
 */
public class TestString {

    OrderActivity order = new OrderActivity();
    @Test
    public void check(){

        order.quantity = 0;

        order.incrementTest();
        assertEquals(order.quantity,1);
        order.decrementTest();
        assertEquals(order.quantity,0);
        order.decrementTest();
        assertEquals(order.quantity,0);
        order.incrementTest();
        order.incrementTest();

        String arr[] = order.stringTest();

        assertEquals(arr[0],"아메리카노");
        assertEquals(arr[1],"2");
        assertEquals(arr[2],"4000");


    }
    @Test
    public void testing(){
        PayActivity act = new PayActivity();
        assertEquals(act.onTimeSetTest(11,0),"오전 11시0분");
        assertEquals(act.onTimeSetTest(12,0),"오후 12시0분");
        assertEquals(act.onTimeSetTest(13,0),"오후 1시0분");

    }


}
