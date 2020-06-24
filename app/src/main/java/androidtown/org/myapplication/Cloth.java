package androidtown.org.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Random;

public class Cloth extends AppCompatActivity {
    TextView view;
    File file2;
    Button next;
    int j=0;
    public static int check=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloth);
       // view =(TextView)findViewById(R.id.view);
        next = (Button) findViewById(R.id.next);
        Intent callIntent = getIntent();
        Bundle mybundle = callIntent.getExtras();
        check = mybundle.getInt("val");

        StringBuffer data = new StringBuffer();
        FileInputStream fis;
        BufferedReader buffer;
        String str="";
        String[] order={"Spring","Summer","Fall","Winter","All"};
        String file;
        Context context = getApplicationContext();
        String storage = context.getFilesDir().getAbsolutePath();

        for(int i=0;i<order.length;i++){
            if(check==i){
                file=order[i];
                file+=".txt";
                file2 = new File(storage +"/"+order[i]);
                try {
                    fis= openFileInput(file);
                    buffer = new BufferedReader
                            (new InputStreamReader(fis));
                    str = buffer.readLine();
                    while (str != null) {
                        data.append(str + "\n");
                        str = buffer.readLine();
                    }
                    File list[] = file2.listFiles();
                        Bitmap myBitmap = BitmapFactory.decodeFile(list[0].getAbsolutePath());
                        ImageView myImage2 = (ImageView) findViewById(R.id.cloth_image);
                        myImage2.setImageBitmap(myBitmap);

                    next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            changeImage();
                        }
                    });

                    buffer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
               // view.setText(data);
                i=order.length;
            }
        }

        }
    private void changeImage() {
        File list[] = file2.listFiles();
        if(j != list.length-1)
        {
            j++;
            Bitmap myBitmap = BitmapFactory.decodeFile(list[j].getAbsolutePath());
            ImageView myImage2 = (ImageView) findViewById(R.id.cloth_image);
            myImage2.setImageBitmap(myBitmap);
        }
        else
        {
            Toast.makeText(this, "마지막 옷입니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
