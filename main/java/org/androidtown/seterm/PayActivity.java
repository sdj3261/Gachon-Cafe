package org.androidtown.seterm;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * class that sets the payment information and proceeds with the payment.
 * @author SeProject
 * @date 2020-06-21
 * @version 0.0.1
 *
 */
public class PayActivity extends AppCompatActivity implements View.OnClickListener{
    Button send;
    TextView sum;
    private TextView timer;
    private FirebaseAuth mAuth;
    private TimePickerDialog.OnTimeSetListener callbackMethod;
    private DatabaseReference mDatabase;
    private FirebaseDatabase database;
    private FirebaseUser user;
    private String username = null;
    private int point;
    private int pay_sum;
    int hour;



    /**
     * Methods that occur when creating a screen <br/>
     * defines 'send' button that proceed order and textview 'sum' that is total price of all beverages<br/>
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        send = (Button)findViewById(R.id.send);
        sum = (TextView)findViewById(R.id.sum);
        this.InitializeView();
        this.InitializeListener();

        Intent intent = getIntent();
        String[] arr1 = intent.getExtras().getStringArray("americano");
        String[] arr2 = intent.getExtras().getStringArray("latte");
        String[] arr3 = intent.getExtras().getStringArray("capuchino");
        String[] arr4 = intent.getExtras().getStringArray("camomile");

        int am = Integer.parseInt(arr1[2]);
        int la = Integer.parseInt(arr2[2]);
        int ca = Integer.parseInt(arr3[2]);
        int mi = Integer.parseInt(arr4[2]);
        pay_sum = am+la+ca+mi;

        sum.setText(NumberFormat.getCurrencyInstance().format(am+la+ca+mi));

        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            /**
             * method that gets user's name and point
             * @param dataSnapshot
             */
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mAuth = FirebaseAuth.getInstance();
                user = FirebaseAuth.getInstance().getCurrentUser();
                username = mAuth.getCurrentUser().getDisplayName();
                DataSnapshot data =dataSnapshot.child("users").child(username).child("point");
                point = Integer.parseInt(data.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        send.setOnClickListener(this);

    }

    /**
     * method that occurs when click screen<br/>
     * shows sum of all beverage's price.<br/>
     * if user doesn't set the reservation time, user can't proceed the payment.<br/>
     * user can't proceed payment if total beverage quantity is 0 or point is lower than total price<br/>
     */
    @Override
    public void onClick(View v) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        username = mAuth.getCurrentUser().getDisplayName();
        switch (v.getId()) {

            case R.id.send:

                sum = (TextView) findViewById(R.id.sum);
                timer = (TextView) findViewById(R.id.timer);
                Intent intent = getIntent();
                String[] arr1 = intent.getExtras().getStringArray("americano");
                String[] arr2 = intent.getExtras().getStringArray("latte");
                String[] arr3 = intent.getExtras().getStringArray("capuchino");
                String[] arr4 = intent.getExtras().getStringArray("camomile");

                if (arr1[1].equals("0") && arr2[1].equals("0") && arr3[1].equals("0") && arr4[1].equals("0")) {
                    Toast.makeText(PayActivity.this, "최소 음료 1개를 지정하셔야 주문 할 수 있습니다.", Toast.LENGTH_SHORT).show();
                } else if (timer.getText().toString().equals("00:00")) {
                    Toast.makeText(PayActivity.this, "예약 시간을 지정하셔야 주문 할 수 있습니다.", Toast.LENGTH_SHORT).show();
                }
                else if(point < pay_sum)
                {
                    Toast.makeText(PayActivity.this, "포인트가 부족합니다 충전해주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    String name = mAuth.getCurrentUser().getDisplayName();
                    String time = timer.getText().toString();
                    String money = sum.getText().toString();
                    DatabaseReference mDatabase = database.getReference("users").child(username).child("point");
                    mDatabase.setValue(Integer.toString(point - pay_sum));
                    arr1[1] = arr1[1];
                    writeNewPost(name,arr1[1],arr2[1],arr3[1],arr4[1],time,money);
                    Toast.makeText(PayActivity.this, "예약 주문이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                }
        }
    }

    /**
     * method that initialize textview that shows reservation time
     */
    public void InitializeView()
    {
        timer = (TextView)findViewById(R.id.timer);
    }

    /**
     * method that set textView that shows reservation time<br/>
     * this method divide the time into AM, PM.<br/>
     */
    public void  InitializeListener()
    {
        callbackMethod = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute)
            {
                if(hourOfDay > 12)
                {
                    hourOfDay = hourOfDay - 12;
                    timer.setText("오후 " + hourOfDay + "시" + minute + "분");
                }
                else {
                    if(hourOfDay == 12)
                        timer.setText("오후 " + hourOfDay + "시" + minute + "분");
                    else
                        timer.setText("오전 " + hourOfDay + "시" + minute + "분");
                }
                hour = hourOfDay;
            }
        };

    }

    /**
     * method that tests 'Initialize Listner' method works
     * Initialize Listner method is hard to test because of callbackMethod
     * @param hourOfDay
     * @param minute
     * @return Time information in string
     */
    public String onTimeSetTest(int hourOfDay, int minute)
    {
        String time;
        if(hourOfDay > 12)
        {
            hourOfDay = hourOfDay - 12;
            time = ("오후 " + hourOfDay + "시" + minute + "분");
        }
        else {
            if(hourOfDay == 12)
                time = ("오후 " + hourOfDay + "시" + minute + "분");
            else
                time = ("오전 " + hourOfDay + "시" + minute + "분");
        }
        return time;
    }
    /**
     * method that set time 12:00
     */
    public void OnClickHandler(View view)
    {
        TimePickerDialog dialog = new TimePickerDialog(this, callbackMethod, 12, 0, false);
        dialog.show();
    }

    /**
     * method that send order information to firebase.<br/>
     * @param username user's name
     * @param order1 order information of americano
     * @param order2 order information of cafelatte
     * @param order3 order information of cappuccino
     * @param order4 order information of camomile
     * @param time Reservation time
     * @param sum total price
     */
    private void writeNewPost(String username, String order1, String order2, String order3, String order4, String time, String sum) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("users").child(username).child("order");
        User user = new User(username, order1, order2,order3,order4, time, sum);
        mDatabase.setValue(user);
    }

    /**
     * method that tests 'writeNewPost' method works
     * @param username testing username
     * @param order1 testing order information
     * @param order2 testing order information
     * @param order3 testing order information
     * @param order4 testing order information
     * @param time testing time information
     * @param sum testing total price
     * @return
     */
    public static User writeTest(String username, String order1, String order2, String order3, String order4, String time, String sum) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        User user = new User(username, order1, order2,order3,order4, time, sum);
        return user;
    }

}
