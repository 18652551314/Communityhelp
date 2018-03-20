package com.example.administrator.communityhelp.thirdpage;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.communityhelp.Login_Main;
import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.my_other.CircularImage;
import com.example.administrator.communityhelp.my_other.GlideLoader;
import com.example.administrator.communityhelp.my_other.PictureCut;
import com.example.administrator.communityhelp.mysql.SqlHelper;
import com.example.administrator.communityhelp.mysql.UserData;
import com.example.administrator.communityhelp.thirdpage.about_us.AboutUsActivity;
import com.example.administrator.communityhelp.thirdpage.about_us.WebSerVieceUtil;
import com.example.administrator.communityhelp.thirdpage.community.MyCommunityActivity;
import com.example.administrator.communityhelp.thirdpage.leaving_message.MyLeavingMessageActivity;
import com.example.administrator.communityhelp.thirdpage.order.MyOrderActivity;
import com.example.administrator.communityhelp.thirdpage.personal_information.PersonnalMessageActivity;
import com.example.administrator.communityhelp.thirdpage.publication.MyPublicationActivity;
import com.example.administrator.communityhelp.thirdpage.set_up.SetActivity;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/30.
 */
public class ThirdPage extends Fragment {
    String jsonStr;
    public SQLiteDatabase dbweiter;
    public SQLiteDatabase dbReader;
    SqlHelper helper;
    CircularImage imageView_head;
    TextView textView_name_head;
    TextView textView_telNum_head;
    //头部头像测试用的数据
    int headimage = R.drawable.wd_del;
    Bitmap unloadBitmap = null;
    //我的订单点击对象
    LinearLayout linearLayout_myOrder;
    //我的发布
    LinearLayout linearLayout_myPublication;
    //我的小区
    LinearLayout linearLayout_myCommunity;

    LinearLayout linearLayout_mySetting;
    //我的留言
    LinearLayout linearLayout_myMessage;
    //关于我们
    LinearLayout linearLayout_oboutUs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_third_page, container, false);
        init(view);
        helper = new SqlHelper(getActivity());
        dbReader = helper.getReadableDatabase();
        dbweiter = helper.getWritableDatabase();
        //获取头像ID等数据，网络请求
        if (isLoad()) {
            loadSet();
        } else {
            unLoadSet();
        }

        //获取整个头部的布局
        LinearLayout linnearLayout_head = (LinearLayout) view.findViewById(R.id.geRenZhongXin_headLayout);
        //设置头部布局的点击事件
        linnearLayout_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoad()) {
                    Intent intent = new Intent(getActivity(), PersonnalMessageActivity.class);
                    startActivity(intent);
                } else {
                    tranceLogin(getActivity());
                }
            }
        });
        linearLayout_myOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoad()) {
                    Intent intent = new Intent(getActivity(), MyOrderActivity.class);
                    startActivity(intent);
                } else {
                    tranceLogin(getActivity());
                }

            }
        });
        linearLayout_myPublication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoad()) {
                    Intent intent = new Intent(getActivity(), MyPublicationActivity.class);
                    startActivity(intent);
                } else {
                    tranceLogin(getActivity());
                }
            }
        });

        linearLayout_myCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoad()) {
                    Intent intent = new Intent(getActivity(), MyCommunityActivity.class);
                    startActivity(intent);
                } else {
                    tranceLogin(getActivity());
                }

            }
        });
        linearLayout_mySetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SetActivity.class);
                startActivity(intent);
            }
        });
        linearLayout_myMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoad()) {
                    Intent intent = new Intent(getActivity(), MyLeavingMessageActivity.class);
                    startActivity(intent);
                } else {
                    tranceLogin(getActivity());
                }
            }
        });
        linearLayout_oboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AboutUsActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void init(View view) {
        imageView_head = (CircularImage) view.findViewById(R.id.imageView_geRenZhongXin_head);
        textView_name_head = (TextView) view.findViewById(R.id.textView_geRenZhongXin_name);
        textView_telNum_head = (TextView) view.findViewById(R.id.textView_telNum_geRenZhongXin_head);
        linearLayout_myOrder = (LinearLayout) view.findViewById(R.id.layout_myOrder);
        linearLayout_myCommunity = (LinearLayout) view.findViewById(R.id.linearLayout_personnal_myCommunity);
        linearLayout_myPublication = (LinearLayout) view.findViewById(R.id.linearLayout_myPublication);
        linearLayout_mySetting = (LinearLayout) view.findViewById(R.id.layout_mySetting);
        linearLayout_myMessage = (LinearLayout) view.findViewById(R.id.linearLayout_myMessage);
        linearLayout_oboutUs = (LinearLayout) view.findViewById(R.id.linearLayout_about_us);
        textView_telNum_head.setVisibility(View.VISIBLE);
    }

    public UserData getUserData() {
        String select = "select * from user";
        Cursor cursor = dbReader.rawQuery(select, null);
        UserData data = new UserData();
        while (cursor.moveToNext()) {
            String communityCode = cursor.getString(cursor.getColumnIndex("communityCode"));
            String communityName = cursor.getString(cursor.getColumnIndex("communityName"));
            boolean state = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("state")));
            boolean isAdmin = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("isAdmin")));
            String userName = cursor.getString(cursor.getColumnIndex("userName"));
            String userId = cursor.getString(cursor.getColumnIndex("userId"));
            String uuid = cursor.getString(cursor.getColumnIndex("uuid"));
            String msg = cursor.getString(cursor.getColumnIndex("msg"));
            data.setCommunityCode(communityCode);
            data.setCommunityName(communityName);
            data.setIsAdmin(isAdmin);
            data.setState(state);
            data.setMsg(msg);
            data.setUserId(userId);
            data.setUserName(userName);
            data.setUuid(uuid);
        }
        return data;
    }

    public boolean isLoad() {
        return (getUserData().getUserId() != null) && (getUserData().getUserId() != "");
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isLoad()) {
            loadSet();
        } else {
            unLoadSet();
        }
    }

    public void loadSet() {
        //电话显示出来
        textView_telNum_head.setVisibility(View.VISIBLE);
        //获取用户登录基本网络数据
        Map<String, String> map = new HashMap();
        Cursor cursor = dbReader.query("user", null, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String userId = cursor.getString(cursor.getColumnIndex("userId"));
            map.put("userId", userId);
        }
        final WebSerVieceUtil webSerVieceUtil = new WebSerVieceUtil("http://member.service.zhidisoft.com", "memberInfo", "memberService", map);
//设置从网络获取的头像
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    jsonStr = webSerVieceUtil.GetStringMessage();
                    Log.e("json",jsonStr);
                } catch (Exception e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "没有网络", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                Gson gson = new Gson();
                UserMessage userMessage = new UserMessage();
                userMessage = gson.fromJson(jsonStr, UserMessage.class);
//                Log.e("cat", userMessage.getHeadImg());
                final Bitmap bitmap1 = BitmapFactory.decodeFile(userMessage.getHeadImg());
                final UserMessage finalUserMessage = userMessage;
                imageView_head.post(new Runnable() {
                    @Override
                    public void run() {
                        //等待
                        new GlideLoader().displayImage(getActivity(), finalUserMessage.getHeadImg(), imageView_head);
                    }
                });
                textView_name_head.post(new Runnable() {
                    @Override
                    public void run() {
                        textView_name_head.setText(finalUserMessage.getNickName());
                    }
                });
                textView_telNum_head.post(new Runnable() {
                    @Override
                    public void run() {
                        textView_telNum_head.setText("手机号： " + finalUserMessage.getAccount());
                    }
                });
            }
        }).start();
    }

    public void unLoadSet() {
        unloadBitmap = BitmapFactory.decodeResource(getResources(), headimage);
        //压缩裁剪
        Bitmap lastbitmap = PictureCut.toRoundCorner(PictureCut.getLittleImg(unloadBitmap, 30), 2);
        imageView_head.setImageBitmap(lastbitmap);
        //设置没有登录的提示文字
        textView_name_head.setText("您还未登陆，登录");
        textView_telNum_head.setVisibility(View.GONE);
    }

    public void tranceLogin(Context context) {
        Intent intent = new Intent(context, Login_Main.class);
        startActivity(intent);
    }
}
