package org.androidtown.seterm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * class that shows side menu(sub menu)
 * @author SeProject
 * @date 2020-06-21
 * @version 0.0.1
 */
public class sideMenu extends AppCompatActivity implements View.OnClickListener{
    TextView name;
    TextView point;
    ImageButton cancel;
    Button charge;
    Button recent;
    Button revoke;
    private FirebaseUser user;
    private String username = null;
    private FirebaseAuth mAuth;
    /**
     * Method that occurs when creating a screen<br/>
     * prints user information(username, user's point).<br/>
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_menu);
        name = (TextView)findViewById(R.id.userName);
        point = (TextView)findViewById(R.id.userPoint);
        cancel = (ImageButton)findViewById(R.id.btn_cancel);
        String email = null;
        charge = (Button)findViewById(R.id.btn_pointCharge);
        recent = (Button)findViewById(R.id.btn_recent);
        revoke = (Button)findViewById(R.id.btn_revoke);
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            // Name, email address, and profile photo Url
            username = user.getDisplayName();
            email = user.getEmail();
        }
        name.setText(username + " " + email);

        charge.setOnClickListener(this);
        cancel.setOnClickListener(this);
        recent.setOnClickListener(this);
        revoke.setOnClickListener(this);

        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {

            /**
             * method that got user information from firebase<br/>
             * read user's name and user's point.<br/>
             * @param dataSnapshot
             */
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                DataSnapshot data =dataSnapshot.child("users").child(username).child("point");
                point.setText(data.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


    }
    /**
     * method that allow user to revoke Access<br/>
     */
    public void revokeAccess() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(sideMenu.this, "계정이 삭제 되었습니다.", Toast.LENGTH_LONG).show();
                        finishAffinity();
                    }
                });
    }

    /**
     * method that occurs when click screen<br/>
     * there are buttons to charge points, go back, revoke access, show recent order information.<br/>
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pointCharge :
                Intent intent = new Intent(this, ChargeActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_cancel :
                onBackPressed();
                break;
            case R.id.btn_revoke :
                revokeAccess();
                break;
            case R.id.btn_recent :
                Intent intent2 = new Intent(this,RecentActivity.class);
                startActivity(intent2);
                finish();
                break;
        }
    }
}