package com.example.madcamp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madcamp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class FragmentOne extends Fragment {

    public ArrayList<Phonenumber> dataList;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.frag_layout_1,container,false);


        mRecyclerView = view.findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        dataList = new ArrayList<Phonenumber>();
        Cursor c = ((MainActivity2)getActivity()).getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " asc");


        while (c.moveToNext()) {
            // 연락처 id 값
            String id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
            // 연락처 대표 이름
            String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
            String photo_id = c.getString(c.getColumnIndex(ContactsContract.Contacts.PHOTO_ID));
            String photo_thum_id = c.getString(c.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI));
            String person_id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
            String photo_url = c.getString(c.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
            String number;

            // ID로 전화 정보 조회
            Cursor phoneCursor = ((MainActivity2)getActivity()).getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                    null, null);

            // 데이터가 있는 경우
            if (phoneCursor.moveToFirst()) {
                number = phoneCursor.getString(phoneCursor.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.NUMBER));

            }
            else{
                number="None";
            }
            Phonenumber p = new Phonenumber(name,number, photo_id, photo_thum_id, person_id,photo_url );
            phoneCursor.close();
            dataList.add(p);
        }// end while
        Collections.sort(dataList, new Comparator<Phonenumber>() {
            @Override
            public int compare(Phonenumber s1, Phonenumber s2) {
                return s1.getName().toUpperCase().compareTo(s2.getName().toUpperCase());
            }
        });
        c.close();

        RecyclerAdapter adapter = new RecyclerAdapter(getContext(),dataList);
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        return view;
    }




}

