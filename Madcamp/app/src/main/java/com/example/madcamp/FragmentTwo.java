package com.example.madcamp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;

public class FragmentTwo extends Fragment {
    private ListView listview ;
    private ListViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.frag_layout_2,container,false);




        // Adapter 생성
        adapter = new ListViewAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) view.findViewById(R.id.listview1);
        listview.setAdapter(adapter);


        // 첫 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(getContext(), R.drawable.niniz1),
                "울고있는 앙몬드","엉엉엉..",R.drawable.niniz1) ;
        // 두 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(getContext(), R.drawable.g_pic1),
                "애교부리는 라이언","애교뿜뿜!",R.drawable.g_pic1) ;
        adapter.addItem(ContextCompat.getDrawable(getContext(), R.drawable.g_pic2),
                "공부하는 라이언","무엇이든 물어보세용",R.drawable.g_pic2) ;
        adapter.addItem(ContextCompat.getDrawable(getContext(), R.drawable.g_pic3),
                "베이비 어피치","응애응애",R.drawable.g_pic3) ;
        adapter.addItem(ContextCompat.getDrawable(getContext(), R.drawable.g_pic4),
                "신난 앙몬드","히히 신난당",R.drawable.g_pic4) ;
        adapter.addItem(ContextCompat.getDrawable(getContext(), R.drawable.g_pic5),
                "코니와 무지","하위하위",R.drawable.g_pic5) ;
        adapter.addItem(ContextCompat.getDrawable(getContext(), R.drawable.g_pic6),
                "뿌듯한 스카피","후훗^^",R.drawable.g_pic6) ;
        adapter.addItem(ContextCompat.getDrawable(getContext(), R.drawable.g_pic7),
                "배고픈 스카피","와구와구",R.drawable.g_pic7) ;
        adapter.addItem(ContextCompat.getDrawable(getContext(), R.drawable.g_pic8),
                "튜브와 어피치","헤헤",R.drawable.g_pic8) ;
        adapter.addItem(ContextCompat.getDrawable(getContext(), R.drawable.g_pic9),
                "카카오 친구들","총총총..",R.drawable.g_pic9) ;
        adapter.addItem(ContextCompat.getDrawable(getContext(), R.drawable.g_pic10),
                "유혹하는 라이언","나랑 놀래?",R.drawable.g_pic10) ;
        adapter.addItem(ContextCompat.getDrawable(getContext(), R.drawable.g_pic11),
                "떨떠름한 튜브","긁적긁적",R.drawable.g_pic11) ;
        adapter.addItem(ContextCompat.getDrawable(getContext(), R.drawable.g_pic12),
                "괴롭힘 당하는 튜브","귀찮..",R.drawable.g_pic12) ;
        adapter.addItem(ContextCompat.getDrawable(getContext(), R.drawable.g_pic13),
                "꽃을 든 튜브","흐음..",R.drawable.g_pic13) ;

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                ListViewItem item = (ListViewItem) parent.getItemAtPosition(position) ;

                String titleStr = item.getTitle() ;
                String conStr=item.getCon();
                Drawable iconDrawable = item.getIcon() ;
                int iconContext=item.getContext();

                Intent intent = new Intent(getActivity(), PhotozoomActivity.class);

                Bitmap sendBitmap = BitmapFactory.decodeResource(getResources(),iconContext);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                sendBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                intent.putExtra("image",byteArray);
                startActivity(intent);

                // TODO : use item data.
            }
        }) ;


        return view;
    }
}