package org.androidtown.seterm;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

    ImageView timeImage = null;



    @Override
    protected void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        send = (Button)findViewById(R.id.send);
        sum = (TextView)findViewById(R.id.sum);
        this.InitializeView();
        this.InitializeListener();

        timeImage = (ImageView)findViewById(R.id.paymentImage);

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

    public void InitializeView()
    {
        timer = (TextView)findViewById(R.id.timer);
    }

    public void InitializeListener()
    {
        callbackMethod = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute)
            {
                if(hourOfDay<9||hourOfDay>21){
                    timeImage.setImageResource(R.drawable.night);
                }
                else if(hourOfDay>17){
                    timeImage.setImageResource(R.drawable.sunset);
                }
                else{
                    timeImage.setImageResource(R.drawable.day);
                }

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
            }
        };
    }
    public void OnClickHandler(View view)
    {
        TimePickerDialog dialog = new TimePickerDialog(this, callbackMethod, 12, 0, false);
        dialog.show();
    }

    private void writeNewPost(String username, String order1, String order2, String order3, String order4, String time, String sum) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("users").child(username).child("order");
        User user = new User(username, order1, order2,order3,order4, time, sum);
        mDatabase.setValue(user);
    }
}
