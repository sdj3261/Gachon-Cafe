package org.androidtown.seterm;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;


import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * class that tests User Data
 */
public class TestData {
    User user = new User("hey","americano","cafelatte","cappuccino","camomile","1시20분","2000");
    OrderActivity order = new OrderActivity();
    /**
     * method that User class's Mapping method works
     */
    @Test
    public void Mapping(){
        Map<String,Object> result = new HashMap<>();
        result = user.toMap();
        assertEquals(result.get("name"),"hey");
        assertEquals(result.get("order1"),"americano");
        assertEquals(result.get("order2"),"cafelatte");
        assertEquals(result.get("order3"),"cappuccino");
        assertEquals(result.get("order4"),"camomile");
        assertEquals(result.get("time"),"1시20분");
        assertEquals(result.get("sum"),"2000");
    }

    @Test
    public void userTest() {

        User user = new User();

        String username="song";
        String ameri="takeout 1";
        String latte="takeout 2";
        String cappu = "0";
        String camo="3";
        String time="오전 1시 20분";
        String sum = String.valueOf(order.price1*1 + order.price2*2 + order.price4*3);
        user = PayActivity.writeTest(username,ameri,latte,cappu,camo,time,sum);

        assertEquals(user.username,username);
        assertEquals(user.order1,ameri);
        assertEquals(user.order2,latte);
        assertEquals(user.order3,cappu);
        assertEquals(user.order4,camo);
        assertEquals(user.time,time);
        assertEquals(user.sum,sum);

    }



}
