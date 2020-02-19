package com.example.madcamp;

import android.location.Address;
import android.location.Geocoder;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

/*import com.google.android.maps.GeoPoint;*/

public class GeoToWeather {

    private Geocoder geoCoder;
    private double latitudeH;
    private double longitudeH;
    /*private GeoPoint geoPoint;*/

    public GeoToWeather(Geocoder t_gc,double latitude,double longitude)
    {
        geoCoder = t_gc;
        latitudeH=latitude;
        longitudeH=longitude;
        /*geoPoint = t_gp;*/
    }

    public Weather getWeather()
    {
        List<Address> tList=null;
        try {
            tList = geoCoder.getFromLocation(latitudeH,
                    longitudeH, 5);
        } catch (IOException e) {

        }
        // geocoder의 getFromLocation()을 이용하여 Reverse Geocoding(Geopoint->주소)한다.
        // getFromLocation()의 인자로 들어가는 latitude와 longitude는 마이크로 값이 아니므로
        // 10^6을 나누어 인자로 넣어준다.

        Address tAddr = tList.get(0);


        Weather dataWeather = new Weather();
        dataWeather.m_sRegion = tAddr.getLocality();
        // 지역명을 가지고 온다. Geocoder 생성자의 두 번째 이자를 특정한 Locale로 주었을 경우에는
        // 해당 언어로 지역명이 나온다. 영어가 Default이다.



        // 아래는 실제 파싱하는 부분이다.
        XmlPullParserFactory factory = null;

        try{
            factory = XmlPullParserFactory.newInstance();

            factory.setNamespaceAware(true);
            XmlPullParser xpp = null;

            xpp = factory.newPullParser();

            String connectUrl = "http://api.openweathermap.org/data/2.5/weather?lat=36.35&lon=125.55&APPID=58a42758beb08521b77f2f87e685d327";
            // 해당 지역의 url을 설정한다.
            URL UrlRecWeather = null;
            UrlRecWeather = new URL(connectUrl);

            InputStream in;
            in = UrlRecWeather.openStream();

            xpp.setInput(in, "euc-kr");

            ReceiveParsing getParse = new ReceiveParsing(dataWeather);

            getParse.proceed(xpp);
        }
        catch(Exception ex)
        {

        }

        return dataWeather;
    }


    // 파싱하는 클래스. 내부클래스로 삽입
    class ReceiveParsing {

        Weather dataWeather;

        public ReceiveParsing(Weather t_dW)
        {
            dataWeather = t_dW;
        }


        void proceed(XmlPullParser ReceiveStream) {

            boolean bcurrent_condition = false;

            try {

                String sTag;

                int eventType = ReceiveStream.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {

                    // Wait(10);
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            break;

                        case XmlPullParser.END_DOCUMENT:
                            break;

                        case XmlPullParser.START_TAG:
                            // items.add(xpp.getAttributeValue(0));
                            sTag = ReceiveStream.getName();

                            if (sTag.equals("current_conditions")) {
                                bcurrent_condition = true;
                            }

                            if (bcurrent_condition == true) {
                                if (sTag.equals("condition")) {
                                    String sValue = ReceiveStream.getAttributeValue(0);
                                    dataWeather.m_sCurrentState = sValue;
                                } else if (sTag.equals("temp_f")) {
                                    String sValue = ReceiveStream.getAttributeValue(0);
                                    dataWeather.m_nTempF = Integer.parseInt(sValue);
                                } else if (sTag.equals("temp_c")) {
                                    String sValue = ReceiveStream.getAttributeValue(0);
                                    dataWeather.m_nTempC = Integer.parseInt(sValue);
                                } else if (sTag.equals("humidity")) {
                                    String sValue = ReceiveStream.getAttributeValue(0);
                                    dataWeather.m_sHumidity = sValue;
                                } else if (sTag.equals("wind_condition")) {
                                    String sValue = ReceiveStream.getAttributeValue(0);
                                    dataWeather.m_sWindCondition = sValue;
                                }
                            }
                            break;

                        case XmlPullParser.END_TAG:
                            sTag = ReceiveStream.getName();

                            if (sTag.equals("current_conditions")) {
                                bcurrent_condition = false;
                            }
                            break;

                        case XmlPullParser.TEXT:
                            break;
                    }

                    eventType = ReceiveStream.next();
                }
            } catch (Exception e) {

            }
        }
    }


}

// 날씨 형에 관한 구조를 지니는 구조체 형 클래스
class Weather {
    int m_nTempF = 0;
    int m_nTempC = 0;
    String m_sRegion = "Not";
    String m_sCurrentState = "Not";
    String m_sHumidity = "Not";
    String m_sWindCondition = "Not";

}
