package androidtown.org.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    private Timer mTimer;
    private LocationManager locationManager;
    private LocationListener locationListener;

    double lat = 37.566535;
    double lon = 126.977969;

    String getTime,weather_str,location;
    int temp,temp_max,temp_min;
    int weather_id;
    int[] weather_id2 = new int[4];
    int[] weather_temp = new int[4];
    String[] weather_date = new String[4];
    private TextView view,name;
    private Handler handler = new Handler();


    String[] str={"우아함은 눈에 띄지 않지만, 기억에는 남게 된다.",
            "여성은 가방을 들수 있지만, 여성을 들어올려주는 건 그녀가 신고 있는 신발이다.",
            "덜 사라. 그리고 잘 골라라",
            "패선은 변하지만 스타일은 그대로 남는다.",
            "패션은 변하지만 스타일은 영원하다.","우아함이란 제거하는 것이다",
            "패션은 구속이 아닌 현실도피의 형태이어야 한다."};
    String[] str_name={"-조르지오 아르마니-","-크리스찬 루부탱-","-비비안 웨스트우드-",
            "-코코샤넬-","-이브 생 로랑-","-크리스토발 발렌시아가-","-알렉산더 맥퀸-"};
    int count=0;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(count==str.length){
                count=0;
            }
            view.setText(str[count]);
            name.setText(str_name[count]);
            handler.postDelayed(this, 3000);
            count++;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = (TextView) findViewById(R.id.view);
        name = (TextView) findViewById(R.id.name);

        Button b1 = (Button)findViewById(R.id.closet);
        Button b2 = (Button)findViewById(R.id.button2);
        Button b3 = (Button)findViewById(R.id.seeweather);
        Button b4 = (Button)findViewById(R.id.setting);

        try {
            settingGPS();
            Location lo = getMyLocation();
            lat = lo.getLatitude();
            lon = lo.getLongitude();

            MainTimerTask timerTask = new MainTimerTask();
            mTimer = new Timer();
            mTimer.schedule(timerTask, 500, 1000);

            new GetWeather().start();
            new GetWeather5day3time().start();
        }catch (NullPointerException e) {
            Toast.makeText(this,"Can't take your location",Toast.LENGTH_SHORT).show();
            new GetWeather().start();
            new GetWeather5day3time().start();
        }catch (Exception e){
            Toast.makeText(this,"Error:"+e,Toast.LENGTH_LONG).show();
        }

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Closet.class);
                startActivity(intent);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),TodayStyle.class);
                intent.putExtra("Temp", temp);
                intent.putExtra("Time", getTime);
                startActivity(intent);
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Seeweather.class);
                intent.putExtra("weather", weather_id2);
                intent.putExtra("weather_d", weather_date);
                intent.putExtra("weather_t", weather_temp);

                intent.putExtra("weather_c",weather_id);
                intent.putExtra("location",location);
                intent.putExtra("des",weather_str);

                intent.putExtra("temp",temp);
                intent.putExtra("tmax",temp_max);
                intent.putExtra("tmin",temp_min);
                startActivity(intent);
            }
        });


        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Settime.class);
                startActivity(intent);
            }
        });
        Intent intent = new Intent(this, Splash.class);
        startActivity(intent);


    }

    private void settingGPS() {
        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                // TODO 위도, 경도로 하고 싶은 것
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
    }

    private Location getMyLocation() {
        Location currentLocation = null;
        // Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 사용자 권한 요청
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            // 수동으로 위치 구하기
            String locationProvider = LocationManager.GPS_PROVIDER;
            List<String> providers = locationManager.getProviders(true);
            Location bestLocation = null;
            for (String provider : providers) {
                Location l = locationManager.getLastKnownLocation(provider);
                if (l == null) {
                    continue;
                }
                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    // Found best last known location: %s", l);
                    bestLocation = l;
                }
            }
            return bestLocation;
        }
        return currentLocation;
    }

    class GetWeather extends Thread {
        public void run() {
            try {
                handler.postDelayed(runnable,1000);
                handler.post(runnable);
                String urlstr = "http://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&units=metric&appid=8f5437af74a38d00b26d075de28d7da3";
                URL url = new URL(urlstr);
                BufferedReader bf;
                String line;
                String result = "";

                bf = new BufferedReader(new InputStreamReader(url.openStream()));

                while ((line = bf.readLine()) != null) {
                    result = result.concat(line);
                }

                JsonParser jsonParser = new JsonParser();
                JsonElement element = jsonParser.parse(result);

                location=element.getAsJsonObject().get("name").getAsString();

                JsonArray weatherArray = (JsonArray) element.getAsJsonObject().get("weather");
                JsonObject obj = (JsonObject) weatherArray.get(0);
                weather_id = obj.get("id").getAsInt();
                weather_str = transferWeather(weather_id);

                JsonObject mainArray = (JsonObject) element.getAsJsonObject().get("main");
                temp = (int)Math.round(Double.parseDouble(mainArray.get("temp").toString()));
                temp_max = (int)Math.round(Double.parseDouble(mainArray.get("temp_max").toString()));
                temp_min = (int)Math.round(Double.parseDouble(mainArray.get("temp_min").toString()));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        /*
                        curLocation.setText(location);
                        SetIcon(weather_id,weatherIcon);
                        curWeather.setText(weather_str);
                        curTemp.setText(temp+"° ("+temp_max+"°/"+temp_min+"°)");
                        */

                    }
                });
                bf.close();
            } catch (Exception e) {
                System.out.println("Error:" + e.getMessage());
            }
        }
    }
    class GetWeather5day3time extends Thread {
        public void run() {
            try {

                String urlstr = "http://api.openweathermap.org/data/2.5/forecast?lat=" + lat + "&lon=" + lon + "&units=metric&appid=8f5437af74a38d00b26d075de28d7da3";
                URL url = new URL(urlstr);
                BufferedReader bf;
                String line;
                String result = "";

                bf = new BufferedReader(new InputStreamReader(url.openStream()));
                while ((line = bf.readLine()) != null) {
                    result = result.concat(line);
                }

                Gson gson = new Gson();
                Weather wl = gson.fromJson(result, Weather.class);

                for(int i=0;i<4;i++){
                    weather_id2[i]=Integer.parseInt(wl.getMyList().get(i).getWeather());
                    weather_temp[i]=(int)Math.round(Double.parseDouble(wl.getMyList().get(i).getTemp()));
                    SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    transFormat.setTimeZone ( TimeZone.getTimeZone ( "UTC+9" ) );
                    Date d=transFormat.parse(wl.getMyList().get(i).getDt_txt()); //String -> Date

                    transFormat = new SimpleDateFormat("yyyy-MM-dd a HH:mm:ss");

                    String a = transFormat.format(d).toString().substring(11,16).replace("오전","AM").replace("오후","PM");
                    System.out.println(a);

                    weather_date[i]=a;

                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        /*
                        SetIcon(weather_id2[0],icon1);
                        SetIcon(weather_id2[1],icon2);
                        SetIcon(weather_id2[2],icon3);
                        SetIcon(weather_id2[3],icon4);

                        w1.setText(weather_date[0]+"\n"+weather_temp[0]+"°");
                        w2.setText(weather_date[1]+"\n"+weather_temp[1]+"°");
                        w3.setText(weather_date[2]+"\n"+weather_temp[2]+"°");
                        w4.setText(weather_date[3]+"\n"+weather_temp[3]+"°");

                         */

                    }
                });
                bf.close();
            } catch (Exception e) {
                System.out.println("Error:" + e);
            }
        }
    }

    private String transferWeather(int weather)
    {

        if(200<=weather&&weather<=232)
            return "뇌우";
        else if(300<=weather&&weather<=321)
            return "이슬비";

        else if(500<=weather&&weather<=531){
            if(weather == 503 || weather ==504)
                return "폭우";
            else
                return "비";
        }
        else if(600<=weather&&weather<=622){
            if(weather==602)
                return "폭설";
            else
                return "눈";
        }

        else if(weather == 781|| weather ==900)
            return "토네이도";
        else if(700<=weather&&weather<=781){
            if(weather==731||weather==751||weather==761)
                return "모래와 먼지";
            else if(weather ==762)
                return "화산재";
            else if(weather==771)
                return "돌풍";
            else
                return "안개";
        }
        else if(weather == 800)
            return "맑은 하늘";
        else if(weather ==801 || weather==802)
            return "구름 조금";
        else if(weather ==803||weather ==804)
            return "흐린 하늘";
        else if(weather == 901 || weather==962)
            return "허리케인";
        else if(weather == 902)
            return "추움";
        else if(weather == 903)
            return "더움";
        else if(weather == 904)
            return "바람";
        else if(weather == 905)
            return "우박";
        else if(951<=weather&&weather<=962){
            if(weather==951)
                return "잔잔한 하늘";
            else if(952<=weather&&weather<=956)
                return "바람";
            else if(957<=weather&&weather<=959)
                return "강풍";
            else if(weather == 960 || weather==961)
                return "폭풍우";
        }
        return "error";
    }

    boolean canReadLocation = false;

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
// success!
                Location userLocation = getMyLocation();
                if (userLocation != null) {
// todo 사용자의 현재 위치 구하기
                    double latitude = userLocation.getLatitude();
                    double longitude = userLocation.getLongitude();
                }
                canReadLocation = true;
            } else {
// Permission was denied or request was cancelled
                canReadLocation = false;
            }
        }
    }

    private Handler mHandler = new Handler();
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            Date rightNow = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat(
                    "yyyy.MM.dd a hh:mm");

            String dateString = formatter.format(rightNow);
            getTime = dateString;
            //curDate.setText(dateString);

        }
    };

    class MainTimerTask extends TimerTask {
        public void run() {
            mHandler.post(mUpdateTimeTask);
        }
    }
    @Override
    protected void onDestroy() {
        if(mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if(mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                return;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                return;
            }
        }
    }

    @Override
    protected void onResume() {
        try {
            if(mTimer == null) {
                MainTimerTask timerTask = new MainTimerTask();
                mTimer = new Timer();
                mTimer.schedule(timerTask, 500, 3000);
            }
            super.onResume();
        }
        catch (Exception e){
            System.out.println("Error:"+e);
        }

    }
    public void addcloset (View v){
        Intent CAM = new Intent(getApplicationContext(),Cam.class);

        startActivity(CAM);

    }
}