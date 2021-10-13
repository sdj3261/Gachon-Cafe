package org.androidtown.seterm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChargeActivity extends AppCompatActivity implements View.OnClickListener{
    FirebaseUser user;
    String point;
    @Override
    protected void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge);
        Button ch5000 = (Button)findViewById(R.id.charge5000);
        Button ch10000 = (Button)findViewById(R.id.charge10000);
        Button ch30000 = (Button)findViewById(R.id.charge30000);
        ch5000.setOnClickListener(this);
        ch10000.setOnClickListener(this);
        ch30000.setOnClickListener(this);

        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                String username = user.getDisplayName();
                DataSnapshot data =dataSnapshot.child("users").child(username).child("point");
                point = data.getValue().toString();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        String username = user.getDisplayName();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabase = database.getReference("users").child(username).child("point");
        switch (v.getId()) {
            case R.id.charge5000 :
                int charge = Integer.parseInt(point) + 5000;
                mDatabase.setValue(Integer.toString(charge));
                Toast.makeText(ChargeActivity.this, "충전이 완료되었습니다 현재 포인트는 " + charge + "입니다.", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.charge10000 :
                int charge2 = Integer.parseInt(point) + 10000;
                mDatabase.setValue(Integer.toString(charge2));
                Toast.makeText(ChargeActivity.this, "충전이 완료되었습니다. 현재 포인트는 " + charge2 + "입니다.", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.charge30000 :
                int charge3 = Integer.parseInt(point) + 30000;
                mDatabase.setValue(Integer.toString(charge3));
                Toast.makeText(ChargeActivity.this, "충전이 완료되었습니다. 현재 포인트는 " + charge3 + "입니다.", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }
}
