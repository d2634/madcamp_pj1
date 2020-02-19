package com.example.madcamp;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class weatherActivity extends AppCompatActivity {
    public static final int THREAD_HANDLER_SUCCESS_INFO = 1;
    TextView textView_w1;
    TextView textView_w2;
    TextView textView_w3;

    ForeCastManager mForeCast;

    String lon = "128.3910799"; // 좌표 설정
    String lat = "36.1444292";  // 좌표 설정
    weatherActivity mThis;
    ArrayList<ContentValues> mWeatherData;
    ArrayList<WeatherInfo> mWeatherInfomation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tb.setBackgroundColor(Color.rgb(255,177,0));

        Intent intent = getIntent();
        double latitude = intent.getDoubleExtra("latitude",1.0);
        double longitude = intent.getDoubleExtra("longitude",1.0);
        String location= intent.getStringExtra("currentLocation");
        textView_w1=(TextView)findViewById(R.id.textView_w1);
        location=location.substring(0,location.length()-1);
        textView_w1.setText(location );

        String tmp1=Double.toString(longitude);
        String tmp2=Double.toString(latitude);
        lon=tmp1;
        lat=tmp2;
        Initialize();
    }
    public void Initialize()
    {
        textView_w2=(TextView)findViewById(R.id.textView_w2);
        textView_w3 = (TextView)findViewById(R.id.textView_w3);
        mWeatherInfomation = new ArrayList<>();
        mThis = this;
        mForeCast = new ForeCastManager(lon,lat,mThis);
        mForeCast.run();
    }

    public String PrintValue1()
    {
        String mData = "";
        //for(int i = 0; i < mWeatherInfomation.size(); i ++)
        {
            mData = //mData + "\r\n"
                    "최고: " + mWeatherInfomation.get(0).getTemp_Max() + "℃"
                            +  "\n최저: " + mWeatherInfomation.get(0).getTemp_Min() + "℃" ;

        }
        return mData;
    }

    public String PrintValue2()
    {
        String mData = "";
        //for(int i = 0; i < mWeatherInfomation.size(); i ++)
        {
            mData =
                    "습도: " + mWeatherInfomation.get(0).getHumidity() + "%";
        }
        return mData;
    }

    /*public void DataChangedToHangeul()
    {
        for(int i = 0 ; i <mWeatherInfomation.size(); i ++)
        {
            WeatherToHangeul mHangeul = new WeatherToHangeul(mWeatherInfomation.get(i));
            mWeatherInfomation.set(i,mHangeul.getHangeulWeather());
        }
    }*/


    public void DataToInformation()
    {
        for(int i = 0; i < mWeatherData.size(); i++)
        {
            mWeatherInfomation.add(new WeatherInfo(
                    String.valueOf(mWeatherData.get(i).get("weather_Name")),  String.valueOf(mWeatherData.get(i).get("weather_Number")), String.valueOf(mWeatherData.get(i).get("weather_Much")),
                    String.valueOf(mWeatherData.get(i).get("weather_Type")),  String.valueOf(mWeatherData.get(i).get("wind_Direction")),  String.valueOf(mWeatherData.get(i).get("wind_SortNumber")),
                    String.valueOf(mWeatherData.get(i).get("wind_SortCode")),  String.valueOf(mWeatherData.get(i).get("wind_Speed")),  String.valueOf(mWeatherData.get(i).get("wind_Name")),
                    String.valueOf(mWeatherData.get(i).get("temp_Min")),  String.valueOf(mWeatherData.get(i).get("temp_Max")),  String.valueOf(mWeatherData.get(i).get("humidity")),
                    String.valueOf(mWeatherData.get(i).get("Clouds_Value")),  String.valueOf(mWeatherData.get(i).get("Clouds_Sort")), String.valueOf(mWeatherData.get(i).get("Clouds_Per")),String.valueOf(mWeatherData.get(i).get("day"))
            ));

        }

    }
    public Handler handler = new Handler(){
        @Override      public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case THREAD_HANDLER_SUCCESS_INFO :
                    mForeCast.getmWeather();
                    mWeatherData = mForeCast.getmWeather();
                    if(mWeatherData.size() ==0)
                        textView_w3.setText("데이터가 없습니다");

                    DataToInformation(); // 자료 클래스로 저장,

                    String data1 = "";
                    data1 = PrintValue1();
                    //DataChangedToHangeul();

                    String data2 = "";
                    data2 = PrintValue2();
                    //DataChangedToHangeul();

                    textView_w2.setText(data1);
                    textView_w3.setText(data2);

                    break;
                default:
                    break;
            }
        }
    };
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
