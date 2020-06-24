package androidtown.org.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Seeweather extends AppCompatActivity {
    TextView curLocation, curTemp, curWeather,w1,w2,w3,w4;
    String des,location;
    int temp,temp_max,temp_min;
    LinearLayout back;
    int weather_id;
    ImageView weatherIcon, icon1,icon2,icon3,icon4;
    int[] weather_id2 = new int[4];
    int[] weather_temp = new int[4];
    String[] weather_date = new String[4];
    int num;
    int[] img = {R.drawable.spfa};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        curLocation = (TextView) findViewById(R.id.location);
        curTemp = (TextView) findViewById(R.id.temp);
        curWeather = (TextView) findViewById(R.id.weather);
        weatherIcon = (ImageView) findViewById(R.id.weatherIcon);
        icon1 = (ImageView) findViewById(R.id.icon1);
        icon2 = (ImageView) findViewById(R.id.icon2);
        icon3 = (ImageView) findViewById(R.id.icon3);
        icon4 = (ImageView) findViewById(R.id.icon4);

        w1 = (TextView)findViewById(R.id.w1);
        w2 = (TextView)findViewById(R.id.w2);
        w3 = (TextView)findViewById(R.id.w3);
        w4 = (TextView)findViewById(R.id.w4);



        Intent intent = getIntent();
        weather_id2 = (int[]) intent.getSerializableExtra("weather");
        weather_date = (String[]) intent.getSerializableExtra("weather_d");
        weather_temp = (int[]) intent.getSerializableExtra("weather_t");
        weather_id = (int) intent.getSerializableExtra("weather_c");
        location = (String) intent.getSerializableExtra("location");
        des = (String) intent.getSerializableExtra("des");
        temp = (int) intent.getSerializableExtra("temp");
        temp_max = (int) intent.getSerializableExtra("tmax");
        temp_min = (int) intent.getSerializableExtra("tmin");

        if(temp < 25 && temp > 5)
            num = 0;
        else if(temp >= 25)
            num = 1;
        else
            num = 2;
        LinearLayout back = (LinearLayout)findViewById(R.id.layout);
        back.setBackgroundResource(img[0]);

        SetIcon(weather_id2[0],icon1);
        SetIcon(weather_id2[1],icon2);
        SetIcon(weather_id2[2],icon3);
        SetIcon(weather_id2[3],icon4);
        SetIcon(weather_id,weatherIcon);

        w1.setText(weather_date[0]+"\n"+weather_temp[0]+"°");
        w2.setText(weather_date[1]+"\n"+weather_temp[1]+"°");
        w3.setText(weather_date[2]+"\n"+weather_temp[2]+"°");
        w4.setText(weather_date[3]+"\n"+weather_temp[3]+"°");

        curLocation.setText(location);
        curWeather.setText(des);
        curTemp.setText(temp+"° ("+temp_max+"°/"+temp_min+"°)");
    }


    private void SetIcon(int weather,ImageView weatherIcon)
    {

        if(weather == 781|| weather ==900 || weather == 901 || weather==962||weather==771)//허리케인 토네이도 화산재 돌풍
            weatherIcon.setImageResource(R.drawable.hurricane);
        else if(200<=weather&&weather<=232)//뇌우
            weatherIcon.setImageResource(R.drawable.thunderstorm);
        else if(weather == 503 || weather ==504)//폭우
            weatherIcon.setImageResource(R.drawable.heavyrain);
        else if(300<=weather&&weather<=321 || 500<=weather&&weather<=531) //비
            weatherIcon.setImageResource(R.drawable.rain);
        else if(weather==602) //폭설
            weatherIcon.setImageResource(R.drawable.heavysnow);
        else if(600<=weather&&weather<=622)//눈
            weatherIcon.setImageResource(R.drawable.snow);
        else if(700<=weather&&weather<=781)
            weatherIcon.setImageResource(R.drawable.sanddust);
        else if(weather == 800||weather==951)//맑음
            weatherIcon.setImageResource(R.drawable.clear);
        else if(weather ==801 || weather==802)//구름조금
            weatherIcon.setImageResource(R.drawable.fewcloud);
        else if(weather ==803||weather ==804)//흐림
            weatherIcon.setImageResource(R.drawable.overcastcloud);
        else if(weather == 902)
            weatherIcon.setImageResource(R.drawable.cold);
        else if(weather == 903)
            weatherIcon.setImageResource(R.drawable.hot);
        else if(weather == 904||952<=weather&&weather<=956)
            weatherIcon.setImageResource(R.drawable.wind);
        else if(weather == 905)
            weatherIcon.setImageResource(R.drawable.hail);
        else if(957<=weather&&weather<=959)
            weatherIcon.setImageResource(R.drawable.highwind);
        else if(weather == 960 || weather==961)
            weatherIcon.setImageResource(R.drawable.storm);

    }

}
