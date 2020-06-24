package androidtown.org.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.apache.http.entity.FileEntity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TodayStyle extends AppCompatActivity {
    Spinner spi_gender,spi_style;
    ListView list;
    ArrayAdapter adapter,adapter2;
    Button recom;
    File file;
    List myList;


    String select_gender,select_style;
    String[] genderData ={"성별","남","여"};
    String[] styleData ={"스타일","베이직","섹시","유니크","스트릿"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style);

        list = (ListView) findViewById(R.id.list);
        recom = (Button) findViewById(R.id.btn);
        Intent intent = getIntent();
        int temp = (int) intent.getSerializableExtra("Temp");
        String date = (String) intent.getSerializableExtra("Time");
        String date2 = date.substring(5,7);


        Context context = getApplicationContext();
        myList = new ArrayList();
        String storage = context.getFilesDir().getAbsolutePath();
        recom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Spring
                if ((date2.equals("03")) || (date2.equals("04")) || (date2.equals("05"))) {
                    if (temp >= 25) {
                        file = new File(storage + "/Summer");
                        File list[] = file.listFiles();
                        Random random = new Random();
                        int fnum = random.nextInt(list.length);
                        File recomImage = list[fnum];
                        if (recomImage.exists()) {
                            Bitmap myBitmap = BitmapFactory.decodeFile(list[fnum].getAbsolutePath());

                            ImageView myImage = (ImageView) findViewById(R.id.recom_image);
                            myImage.setImageBitmap(myBitmap);
                        }
                    }
                    if ((10 <= temp) & (temp < 25)) {
                        file = new File(storage + "/Spring");
                        File list[] = file.listFiles();
                        Random random = new Random();
                        int fnum = random.nextInt(list.length);
                        File recomImage = list[fnum];
                        if (recomImage.exists()) {
                            Bitmap myBitmap = BitmapFactory.decodeFile(list[fnum].getAbsolutePath());

                            ImageView myImage = (ImageView) findViewById(R.id.recom_image);
                            myImage.setImageBitmap(myBitmap);
                        }
                    }
                    else {
                        file = new File(storage + "/Winter");
                        File list[] = file.listFiles();
                        Random random = new Random();
                        int fnum = random.nextInt(list.length);
                        File recomImage = list[fnum];
                        if (recomImage.exists()) {
                            Bitmap myBitmap = BitmapFactory.decodeFile(list[fnum].getAbsolutePath());

                            ImageView myImage = (ImageView) findViewById(R.id.recom_image);
                            myImage.setImageBitmap(myBitmap);
                        }
                    }
                }

                //Summer
                if ((date2.equals("06")) || (date2.equals("07")) || (date2.equals("08"))) {
                    if (temp >= 25) {

                        file = new File(storage + "/Summer");
                        if(file.exists()){
                            File list[] = file.listFiles();
                            Random random = new Random();
                            int fnum = random.nextInt(list.length);
                            File recomImage = list[fnum];
                            if (recomImage.exists()) {
                                Bitmap myBitmap = BitmapFactory.decodeFile(list[fnum].getAbsolutePath());

                                ImageView myImage = (ImageView) findViewById(R.id.recom_image);
                                myImage.setImageBitmap(myBitmap);
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Summer closet is empty",
                                    Toast.LENGTH_SHORT).show();
                        }


                    }
                    if ((10 <= temp) & (temp < 25)) {
                        file = new File(storage + "/Spring");
                        if(file.exists()){
                            File list[] = file.listFiles();
                            Random random = new Random();
                            int fnum = random.nextInt(list.length);
                            File recomImage = list[fnum];
                            if (recomImage.exists()) {
                                Bitmap myBitmap = BitmapFactory.decodeFile(list[fnum].getAbsolutePath());

                                ImageView myImage = (ImageView) findViewById(R.id.recom_image);
                                myImage.setImageBitmap(myBitmap);
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Spring closet is empty",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                    else {
                        file = new File(storage + "/Winter");
                        if(file.exists()){
                            File list[] = file.listFiles();
                            Random random = new Random();
                            int fnum = random.nextInt(list.length);
                            File recomImage = list[fnum];
                            if (recomImage.exists()) {
                                Bitmap myBitmap = BitmapFactory.decodeFile(list[fnum].getAbsolutePath());

                                ImageView myImage = (ImageView) findViewById(R.id.recom_image);
                                myImage.setImageBitmap(myBitmap);
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Winter closet is empty",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                //Fall
                if ((date2.equals("09")) || (date2.equals("10")) || (date2.equals("11"))) {
                    if (temp >= 25) {
                        file = new File(storage + "/Summer");
                        if(file.exists()){
                            File list[] = file.listFiles();
                            Random random = new Random();
                            int fnum = random.nextInt(list.length);
                            File recomImage = list[fnum];
                            if (recomImage.exists()) {
                                Bitmap myBitmap = BitmapFactory.decodeFile(list[fnum].getAbsolutePath());

                                ImageView myImage = (ImageView) findViewById(R.id.recom_image);
                                myImage.setImageBitmap(myBitmap);
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Summer closet is empty",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    if ((10 <= temp) & (temp < 25)) {
                        file = new File(storage + "/Fall");
                        if(file.exists()){
                            File list[] = file.listFiles();
                            Random random = new Random();
                            int fnum = random.nextInt(list.length);
                            File recomImage = list[fnum];
                            if (recomImage.exists()) {
                                Bitmap myBitmap = BitmapFactory.decodeFile(list[fnum].getAbsolutePath());

                                ImageView myImage = (ImageView) findViewById(R.id.recom_image);
                                myImage.setImageBitmap(myBitmap);
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Fall closet is empty",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        file = new File(storage + "/Winter");
                        if(file.exists()){
                            File list[] = file.listFiles();
                            Random random = new Random();
                            int fnum = random.nextInt(list.length);
                            File recomImage = list[fnum];
                            if (recomImage.exists()) {
                                Bitmap myBitmap = BitmapFactory.decodeFile(list[fnum].getAbsolutePath());

                                ImageView myImage = (ImageView) findViewById(R.id.recom_image);
                                myImage.setImageBitmap(myBitmap);
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Winter closet is empty",
                                    Toast.LENGTH_SHORT).show();
                        }}
                }

                //Winter
                if((date2.equals("12")) || (date2.equals("01")) || (date2.equals("02"))) {
                    if(temp>=25) {
                        file = new File(storage + "/Summer");
                        if (file.exists()) {
                            File list[] = file.listFiles();
                            Random random = new Random();
                            int fnum = random.nextInt(list.length);
                            File recomImage = list[fnum];
                            if (recomImage.exists()) {
                                Bitmap myBitmap = BitmapFactory.decodeFile(list[fnum].getAbsolutePath());

                                ImageView myImage = (ImageView) findViewById(R.id.recom_image);
                                myImage.setImageBitmap(myBitmap);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Summer closet is empty",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    if((10<=temp)&(temp<25)) {
                        file = new File(storage + "/Spring");
                        if(file.exists()){
                            File list[] = file.listFiles();
                            Random random = new Random();
                            int fnum = random.nextInt(list.length);
                            File recomImage = list[fnum];
                            if (recomImage.exists()) {
                                Bitmap myBitmap = BitmapFactory.decodeFile(list[fnum].getAbsolutePath());

                                ImageView myImage = (ImageView) findViewById(R.id.recom_image);
                                myImage.setImageBitmap(myBitmap);
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Spring closet is empty",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        file = new File(storage + "/Winter");
                        if (file.exists()) {
                            File list[] = file.listFiles();
                            Random random = new Random();
                            int fnum = random.nextInt(list.length);
                            File recomImage = list[fnum];
                            if (recomImage.exists()) {
                                Bitmap myBitmap = BitmapFactory.decodeFile(list[fnum].getAbsolutePath());

                                ImageView myImage = (ImageView) findViewById(R.id.recom_image);
                                myImage.setImageBitmap(myBitmap);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Winter closet is empty",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        list.setVisibility(View.INVISIBLE);
    }
}