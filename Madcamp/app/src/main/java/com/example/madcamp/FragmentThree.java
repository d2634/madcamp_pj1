package com.example.madcamp;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.List;
import java.util.Locale;

public class FragmentThree extends Fragment {
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int PERMISSIONS_REQUEST_ACCESS_CALL_PHONE = 2;

    // 중요...
    public LocationManager lm;

    // 위치 정보
    TextView txtCurrentPositionInfo;
    // 위치 버튼
    TextView btnCurrentPositionInfo;
    // 지도 구현
    private MapView mapView;
    private Context mContext;

    public double longitude; //경도
    public double latitude; //위도
    public double altitude; //고도
    public float accuracy; //정확도
    public String provider; //위치제공자
    public String currentLocation; // 그래서 최종 위치

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.mapview,container,false);
        Button button=(Button)view.findViewById(R.id.button);


        initView(view);
        setLocation();
        initSet();
        setListener();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), weatherActivity.class);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                intent.putExtra("currentLocation",currentLocation);
// Activity 시작
                startActivity(intent);
            }
        });
       /*MapView mapView = new MapView(this.getActivity());
        ViewGroup mapViewContainer = (ViewGroup) view.findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(37.53737528, 127.00557633);
        mapView.setMapCenterPoint(mapPoint, true);


        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("MADCAMP");
        marker.setTag(0);
        marker.setMapPoint(mapPoint);
        // 기본으로 제공하는 BluePin 마커 모양.
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
        // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
        mapView.addPOIItem(marker);*/

        //Intent intent= new Intent(getContext(), weatherActivity.class);

// putExtra("넘길변수명", 넘기는 int값)



        return view;

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
    public void initView(View v) {

        // 지도
        mapView = new MapView(this.getActivity());

        ViewGroup mapViewContainer = (ViewGroup) v.findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(37.53737528, 127.00557633);
        mapView.setMapCenterPoint(mapPoint, true);

        // 현재위치 받아옴
        txtCurrentPositionInfo = (TextView) v.findViewById(R.id.item_text_1);
        txtCurrentPositionInfo.setSelected(true);

        // 클릭시 지도, 현재위치 갱신
        btnCurrentPositionInfo = (TextView) v.findViewById(R.id.item_text_2);
    }

    public void setLocation() {

        // Location 제공자에서 정보를 얻어오기(GPS)
        // 1. Location을 사용하기 위한 권한을 얻어와야한다 AndroidManifest.xml
        //     ACCESS_FINE_LOCATION : NETWORK_PROVIDER, GPS_PROVIDER
        //     ACCESS_COARSE_LOCATION : NETWORK_PROVIDER
        // 2. LocationManager 를 통해서 원하는 제공자의 리스너 등록
        // 3. GPS 는 에뮬레이터에서는 기본적으로 동작하지 않는다
        // 4. 실내에서는 GPS_PROVIDER 를 요청해도 응답이 없다.  특별한 처리를 안하면 아무리 시간이 지나도
        //    응답이 없다.
        //    해결방법은
        //     ① 타이머를 설정하여 GPS_PROVIDER 에서 일정시간 응답이 없는 경우 NETWORK_PROVIDER로 전환
        //     ② 혹은, 둘다 한꺼번헤 호출하여 들어오는 값을 사용하는 방식.
        // LocationManager 객체를 얻어온다
        // getPermission();

        lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // 위치정보를 얻는다
        //basicToast("현재 위치를 찾고있습니다...");
        getLocation();

    }
    public void initSet() {

        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);

    }

    public void getLocation() {

        try {

            //txtCurrentPositionInfo.setText("수신중..");
            // GPS 제공자의 정보가 바뀌면 콜백하도록 리스너 등록하기~!!!
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자
                    100, // 통지사이의 최소 시간간격 (miliSecond)
                    1, // 통지사이의 최소 변경거리 (m)
                    mLocationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자
                    100, // 통지사이의 최소 시간간격 (miliSecond)
                    1, // 통지사이의 최소 변경거리 (m)
                    mLocationListener);

            //txtCurrentPositionInfo.setText("위치정보 미수신중");
            //lm.removeUpdates(mLocationListener);  //  미수신할때는 반드시 자원해체를 해주어야 한다.

        } catch (SecurityException ex) {

        }

    }
    public void setListener() {

        // 현재위치 가져오기
        btnCurrentPositionInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(this,"현재 위치를 찾고있습니다...", Toast.LENGTH_LONG).show();
                getLocation();

            }

        });
    }

    public void setDaumMapCurrentLocation(double latitude, double longitude) {

        // 중심점 변경
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude), true);

        // 줌 레벨 변경
        mapView.setZoomLevel(4, true);

        // 중심점 변경 + 줌 레벨 변경
        //mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(latitude, longitude), 9, true);

        // 줌 인
        //mapView.zoomIn(true);

        // 줌 아웃
        mapView.zoomOut(true);

        // 마커 생성
        setDaumMapCurrentMarker();

    }

    public void setDaumMapCurrentMarker(){

        MapPOIItem marker = new MapPOIItem();
        marker.setItemName(currentLocation);
        marker.setTag(0);
        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude));
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        mapView.addPOIItem(marker);


    }

    public static String getCompleteAddressString(Context context, double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");


                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("MyCurrentloctionaddress", strReturnedAddress.toString());
            } else {
                Log.w("MyCurrentloctionaddress", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("MyCurrentloctionaddress", "Canont get Address!");
        }

        // "대한민국 " 글자 지워버림
        strAdd = strAdd.substring(5);

        return strAdd;
    }

    private final LocationListener mLocationListener = new LocationListener() {

        public void onLocationChanged(Location location) {
            //여기서 위치값이 갱신되면 이벤트가 발생한다.
            //값은 Location 형태로 리턴되며 좌표 출력 방법은 다음과 같다.

            Log.d("test", "onLocationChanged, location:" + location);
            longitude = location.getLongitude(); //경도
            latitude = location.getLatitude();   //위도
            altitude = location.getAltitude();   //고도
            accuracy = location.getAccuracy();    //정확도
            provider = location.getProvider();   //위치제공자
            //Gps 위치제공자에 의한 위치변화. 오차범위가 좁다.
            //Network 위치제공자에 의한 위치변화
            //Network 위치는 Gps에 비해 정확도가 많이 떨어진다.

            currentLocation = getCompleteAddressString(mContext, latitude, longitude);

//            txtCurrentMoney.setText("위치정보 : " + provider + "\n위도 : " + longitude + "\n경도 : " + latitude
//                    + "\n고도 : " + altitude + "\n정확도 : "  + accuracy);

            // 위치 정보를 글로 나타낸다
            //txtCurrentPositionInfo.setText(currentLocation.toString());

            // 지도를 움직인다
            setDaumMapCurrentLocation(latitude, longitude);

            lm.removeUpdates(mLocationListener);  //  미수신할때는 반드시 자원해체를 해주어야 한다.

        }

        public void onProviderDisabled(String provider) {
            // Disabled시
            Log.d("test", "onProviderDisabled, provider:" + provider);
        }

        public void onProviderEnabled(String provider) {
            // Enabled시
            Log.d("test", "onProviderEnabled, provider:" + provider);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            // 변경시
            Log.d("test", "onStatusChanged, provider:" + provider + ", status:" + status + " ,Bundle:" + extras);
        }
    };
}

