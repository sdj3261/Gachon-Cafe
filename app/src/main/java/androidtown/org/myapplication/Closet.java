package androidtown.org.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Closet extends AppCompatActivity {
    Button search, cam,closet1,closet2,closet3,closet4,closet5,closet6;
    public static String str;
    TextView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_closet);
        closet1=(Button)findViewById(R.id.closet1);
        view = (TextView)findViewById(R.id.view);
    }
    public void click(View v){
        Intent best=new Intent(getApplicationContext(),Best.class);
        Intent myintnet = new Intent(getApplicationContext(),Cloth.class);
        Bundle mybundle = new Bundle();

        StringBuffer data = new StringBuffer();
        FileInputStream fis;
        BufferedReader buffer;
        String str="";
        switch (v.getId()){
            case R.id.closet1:
                mybundle.putInt("val", 0);
                myintnet.putExtras(mybundle);
                startActivity(myintnet);
                break;
            case R.id.closet2:
                mybundle.putInt("val", 1);
                myintnet.putExtras(mybundle);
                startActivity(myintnet);
                break;
            case R.id.closet3:
                mybundle.putInt("val", 2);
                myintnet.putExtras(mybundle);
                startActivity(myintnet);
                break;
            case R.id.closet4:
                mybundle.putInt("val", 3);
                myintnet.putExtras(mybundle);
                startActivity(myintnet);
                break;
            case R.id.closet5:
                mybundle.putInt("val", 4);
                myintnet.putExtras(mybundle);
                startActivity(myintnet);
                break;
            case R.id.closet6:
                startActivity(best);
                break;
        }

    }
}