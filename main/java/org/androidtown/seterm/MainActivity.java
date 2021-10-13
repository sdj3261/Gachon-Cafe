package org.androidtown.seterm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 *
 * class that allow user to login<br/>
 * Main activity
 * @author SeProject
 * @date 2020-06-21
 * @version 0.0.1
 *
 */
public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth = null;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private SignInButton signInButton;
    /**
     * Method that occurs when creating a screen<br/>
     * If user has a previous login history, user can access the app right away.<br/>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signInButton = findViewById(R.id.google_login_button);
        mAuth = FirebaseAuth.getInstance();

        /*기존에 로그인 이력이 있는경우 바로 앱 접속*/
        if (mAuth.getCurrentUser() != null) {
            FirebaseUser user = mAuth.getCurrentUser();
            Intent intent = new Intent(getApplication(), AfterActivity.class);
            startActivity(intent);
            Toast.makeText(MainActivity.this,user.getDisplayName() + "님 로그인되었습니다.",Toast.LENGTH_LONG).show();
            finish();
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    /**
     * method of logging in, Start with user information received.
     */
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /**
     * Method requesting login
     * @param requestCode logging in request code
     * @param resultCode Logging in result code
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
            }
        }
    }

    /**
     * Method for verifying login information through Google
     * @param acct
     */
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(MainActivity.this,user.getDisplayName() + "님 로그인되었습니다.",Toast.LENGTH_LONG).show();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this,"로그인 오류(구글 계정이 필요합니다)",Toast.LENGTH_LONG).show();
                            updateUI(null);
                        }
                    }
                });
    }

    /**
     * Method that goes over to next activity when the account is confirmed
     * @param user User information received from Firebase
     */
    private void updateUI(final FirebaseUser user) { //update ui code here
        if (user != null) {
            FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String username = user.getDisplayName();
                    DataSnapshot data =dataSnapshot.child("users").child(username).child("point");
                    if(data.getValue() == null)
                    {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference mDatabase = database.getReference("users").child(username).child("point");
                        mDatabase.setValue("0");
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
            Intent intent = new Intent(this, AfterActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
