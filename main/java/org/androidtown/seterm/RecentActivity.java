package org.androidtown.seterm;

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
 * class that shows Recent Order Information
 * @author SeProject
 * @date 2020-06-21
 * @version 0.0.1
 */
public class RecentActivity extends AppCompatActivity {
    TextView name;
    TextView time;
    TextView sum;
    TextView am;
    TextView lt;
    TextView cp;
    TextView cm;


    FirebaseUser user;
    /**
     * Method that occurs when creating a screen<br/>
     * execute 'onData change' method with the information of user from firebase.<br/>
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_order);

        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            /**
             * method that prints recent order information<br/>
             * if the recent order information is not null, it changes textVies to the values<br/>
             * @param dataSnapshot
             */
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name = (TextView)findViewById(R.id.recent_name);
                time = (TextView)findViewById(R.id.recent_time);
                sum = (TextView)findViewById(R.id.recent_pay);
                am = (TextView)findViewById(R.id.recent_order1);
                lt = (TextView)findViewById(R.id.recent_order2);
                cp = (TextView)findViewById(R.id.recent_order3);
                cm = (TextView)findViewById(R.id.recent_order4);
                user = FirebaseAuth.getInstance().getCurrentUser();
                String username = user.getDisplayName();
                DataSnapshot data =dataSnapshot.child("users").child(username).child("order").child("sum");
                DataSnapshot data2 =dataSnapshot.child("users").child(username).child("order").child("username");
                DataSnapshot data3 =dataSnapshot.child("users").child(username).child("order").child("time");
                DataSnapshot data4 =dataSnapshot.child("users").child(username).child("order").child("order1");
                DataSnapshot data5 =dataSnapshot.child("users").child(username).child("order").child("order2");
                DataSnapshot data6 =dataSnapshot.child("users").child(username).child("order").child("order3");
                DataSnapshot data7 =dataSnapshot.child("users").child(username).child("order").child("order4");
                if(data.getValue() != null) {
                    time.setText("예약시간 : " + data3.getValue().toString());
                    name.setText(data2.getValue().toString() + "님의 최근 구매 이력 입니다.");
                    sum.setText("지불 금액 " + data.getValue().toString());
                    am.setText("아메리카노 주문 개수 " + data4.getValue().toString());
                    lt.setText("카페라떼 주문 개수 " + data5.getValue().toString());
                    cp.setText("카푸치노 주문 개수 " + data6.getValue().toString());
                    cm.setText("카모마일 주문 개수 " + data7.getValue().toString());
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
