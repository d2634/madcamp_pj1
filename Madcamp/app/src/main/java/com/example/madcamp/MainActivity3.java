package com.example.madcamp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import org.jetbrains.annotations.NotNull;

public class MainActivity3 extends AppCompatActivity implements View.OnClickListener{

    String strNickname, strProfile, strEmail, strAgeRange, strGender, strBirthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tb.setBackgroundColor(Color.rgb(255,177,0));


        TextView ivNickname = findViewById(R.id.ivNickname);
        ImageView ivProfile = findViewById(R.id.ivProfile);
        /*TextView tvEmail = findViewById(R.id.tvEmail);
        TextView tvAgeRange = findViewById(R.id.tvAgeRange);
        TextView tvGender = findViewById(R.id.tvGender);
        TextView tvBirthday = findViewById(R.id.tvBirthday);*/

        Intent intent = getIntent();
        strNickname = intent.getStringExtra("name");
        strProfile = intent.getStringExtra("profile");
        strEmail = intent.getStringExtra("email");
        strAgeRange = intent.getStringExtra("ageRange");
        strGender = intent.getStringExtra("gender");
        strBirthday = intent.getStringExtra("birthday");

        findViewById(R.id.btnLogout).setOnClickListener(this);
        ivNickname.setText(strNickname);
        Glide.with(this).load(strProfile)
                .apply(RequestOptions.circleCropTransform())
                .into(ivProfile);

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.account, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                Toast.makeText(getApplicationContext(), "정상적으로 로그아웃되었습니다.", Toast.LENGTH_SHORT).show();
                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        Intent intent = new Intent(MainActivity3.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
                break;
            case android.R.id.home: { //toolbar의 back키 눌렀을 때 동작
                finish();
                break;
            }

        }
        return true;
    }

    @Override
    public void onClick(@NotNull View v) {
        switch (v.getId()) {
            case R.id.btnLogout:
                Toast.makeText(getApplicationContext(), "정상적으로 로그아웃되었습니다.", Toast.LENGTH_SHORT).show();
                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        Intent intent = new Intent(MainActivity3.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
                break;
        }
    }
}
