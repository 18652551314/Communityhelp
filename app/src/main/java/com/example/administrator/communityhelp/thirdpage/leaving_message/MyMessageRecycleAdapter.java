package com.example.administrator.communityhelp.thirdpage.leaving_message;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.firstpage.PictureViewPagerActivity2;
import com.example.administrator.communityhelp.my_other.GlideLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/6.
 */
public class MyMessageRecycleAdapter extends RecyclerView.Adapter<MyMessageRecycleHolder> {
    List<JSONObject> list;
    Context context;
    private ImageView[] images = new ImageView[5];
    private List<String> imgList;
    private Map<Integer,List<String>> map=new HashMap<>();
    private long start;
    private long end;

    public MyMessageRecycleAdapter(Context context, List<JSONObject> list) {
        this.context = context;
        this.list = list;

    }

    public void AddAll(List list) {
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public MyMessageRecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyMessageRecycleHolder holder = new MyMessageRecycleHolder(LayoutInflater.from(context).inflate(R.layout.myleaving_message_recycleview_item, parent, false));
        images[0] = holder.imageView_mes01;
        images[1] = holder.imageView_mes02;
        images[2] = holder.imageView_mes03;
        images[3] = holder.imageView_mes04;
        images[4] = holder.imageView_mes05;
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyMessageRecycleHolder holder, final int position) {
        try {
            if (!isNull(list.get(position))) {
                new GlideLoader().displayImage(context, list.get(position).getString("headImg"), holder.imageView_headPic);
                holder.textView_name.setText(list.get(position).getString("userName"));
                holder.textView_time.setText(list.get(position).getString("createTime"));
                holder.textView_message.setText(list.get(position).getString("content"));
                if (!"".equals(list.get(position).getString("amrPath")) && !"0".equals(list.get(position).getString("vflength"))) {
                    ViewGroup.LayoutParams layoutParams = holder.imageView_sound.getLayoutParams();
                    layoutParams.width = 160 + Integer.parseInt(list.get(position).getString("vflength"))*1000 / 120;
                    holder.imageView_sound.setLayoutParams(layoutParams);
                    holder.imageView_sound.setVisibility(View.VISIBLE);
                    holder.imageView_sound.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Handler handler = new Handler();
                            holder.imageView_sound.setImageResource(R.drawable.voice_play);
                            final AnimationDrawable ad = (AnimationDrawable) holder.imageView_sound.getDrawable();
                            try {
                                final MediaPlayer player = new MediaPlayer();
//                                final Player player = new Player();
                                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                player.setDataSource(list.get(position).getString("amrPath"));
                                player.prepareAsync();//异步方式装在流媒体
                                player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                    @Override
                                    public void onPrepared(MediaPlayer mp) {
                                        player.start();
                                        ad.start();
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                while(true){
                                                    if(!player.isPlaying()){
                                                        handler.post(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                ad.stop();
                                                                holder.imageView_sound.setImageResource(R.mipmap.chatfrom_voice_playing);
                                                            }
                                                        });
                                                        break;
                                                    }
                                                }

                                            }
                                        }).start();
                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    holder.textView_sound.setVisibility(View.VISIBLE);
                    holder.textView_sound.setText(Integer.parseInt(list.get(position).getString("vflength")) + "''");
                }
                if (list.get(position).getJSONArray("picPathSet") != null) {
                    JSONArray arr = list.get(position).getJSONArray("picPathSet");
                    imgList=new ArrayList<>();
                    for (int i = 0; i < arr.length(); i++) {
                       try {
                           images[i].setVisibility(View.VISIBLE);
                           imgList.add(arr.getJSONObject(i).getString("picPath"));
                           new GlideLoader().displayImage(context, arr.getJSONObject(i).getString("picPath"), images[i]);
                       }catch (Exception e){}
                    }

                    map.put(position,imgList);
                    if (imgList.size() != 0) {
                        for (int i = 0; i < imgList.size(); i++) {
                            images[i].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(context, PictureViewPagerActivity2.class);
                                    Log.e("imgList", imgList.size() + "");
                                    intent.putStringArrayListExtra("path", (ArrayList<String>) map.get(position));
                                    context.startActivity(intent);
                                }
                            });
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public boolean isNull(Object object) {
        return object == null;
    }
}
