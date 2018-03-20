package com.example.administrator.communityhelp.thirdpage.set_up;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.myadapter.SoundSelectAdapter;

import java.util.ArrayList;
import java.util.List;

public class SoundSelectActivity extends AppCompatActivity implements View.OnClickListener{
    private ListView listView;
    private List<Ringtone> list = new ArrayList<>();
    private RingtoneManager manager;
    private TextView text_save,text_back;
    private int hasChecked;
    private SoundSelectAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_select);
        init();
        getData();
        adapter = new SoundSelectAdapter(this, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RadioButton radioButton = (RadioButton) view.findViewById(R.id.radiobutton);
                radioButton.setChecked(true);
                hasChecked=position;
                adapter.notifyDataSetChanged();
                for(int i=0;i<list.size();i++){
                    Ringtone ringtone=list.get(i);
                    if(ringtone.isPlaying()){
                        ringtone.stop();
                    }
                }
                Ringtone ringtone=list.get(position);
                ringtone.play();
            }
        });
    }

    private void init() {
        listView = (ListView) findViewById(R.id.sound_listview);
        listView.setFocusable(false);
        text_back= (TextView) findViewById(R.id.text_back);
        text_back.setOnClickListener(this);
        text_save= (TextView) findViewById(R.id.text_save);
        text_save.setOnClickListener(this);
    }

    public void getData() {
        manager = new RingtoneManager(this);
        manager.setType(RingtoneManager.TYPE_NOTIFICATION);//Type_ringtone:电话铃声会循环播放
        Cursor cursor = manager.getCursor();
        int count = cursor.getCount();
        for (int i = 0; i < count; i++) {
            list.add(manager.getRingtone(i));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.text_back:
                finish();
                break;
            case R.id.text_save:
                if(hasChecked==-1){
                    return;
                }
                SharedPreferences preferences=getSharedPreferences("soundName",MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("RingToneName",list.get(hasChecked).getTitle(this));
                editor.commit();
                Intent intent=getIntent();
                intent.putExtra("soundName",list.get(hasChecked).getTitle(this));
                setResult(RESULT_OK,intent);
                finish();
                break;
        }
    }
}
