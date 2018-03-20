package com.example.administrator.communityhelp.myadapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.administrator.communityhelp.R;

import java.util.ArrayList;
import java.util.List;

public class SoundSelectAdapter extends BaseAdapter {
    private Context context;
    private List<Ringtone> list;
    private int position=-1;
    private boolean flag=false;
    private String soundName;

    public SoundSelectAdapter(Context context, List<Ringtone> list) {
        this.context = context;
        this.list = list;
        SharedPreferences preferences=context.getSharedPreferences("soundName",Context.MODE_PRIVATE);
        soundName=preferences.getString("RingToneName","");
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
//        MyHolder holder=null;
//        if(view==null){
//            holder=new MyHolder();
            view= View.inflate(context, R.layout.soundselect_item,null);
            TextView textView= (TextView) view.findViewById(R.id.textview);
            final RadioButton radioButton= (RadioButton) view.findViewById(R.id.radiobutton);
//            view.setTag(holder);
//        }else {
//            holder= (MyHolder) view.getTag();
//        }
        textView.setText(list.get(i).getTitle(context));
        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    position=i;
                }
            }
        });
//        if(list.get(i).getTitle(context).equals(soundName)){
//            radioButton.setChecked(true);
//            radioButton.setBackgroundResource(R.mipmap.checked);
//        }
        if(flag==false&&list.get(i).getTitle(context).equals(soundName)){
            radioButton.setChecked(true);
            radioButton.setBackgroundResource(R.mipmap.checked);
            flag=true;
        }
        if(position==i){
            radioButton.setChecked(true);
            radioButton.setBackgroundResource(R.mipmap.checked);
        }else{
            radioButton.setChecked(false);
            radioButton.setBackgroundResource(R.mipmap.pressed);
        }
        notifyDataSetChanged();
        return view;
    }
    class MyHolder{
        TextView textView;
        RadioButton radioButton;
    }
}