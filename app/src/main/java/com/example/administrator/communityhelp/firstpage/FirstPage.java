package com.example.administrator.communityhelp.firstpage;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.communityhelp.Community_Choice;
import com.example.administrator.communityhelp.FirstMajor;
import com.example.administrator.communityhelp.ImageScale;
import com.example.administrator.communityhelp.Login_Main;
import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.firstpage.bianmin.BianMin_Main_Major;
import com.example.administrator.communityhelp.firstpage.bianmin.activity.Details;
import com.example.administrator.communityhelp.firstpage.linliquan.LinLi_Main_Major;
import com.example.administrator.communityhelp.firstpage.taoshihui.TaoShiHui_Main_Major;
import com.example.administrator.communityhelp.firstpage.taoshihui.TaoShiHui_ShangPingXiangQingActivity;
import com.example.administrator.communityhelp.firstpage.wuye.WuYe_Main_Major;
import com.example.administrator.communityhelp.my_other.GlideLoader;
import com.example.administrator.communityhelp.myadapter.Firstpageviewpager;
import com.example.administrator.communityhelp.mysql.SqlHelper;
import com.example.administrator.communityhelp.mysql.UserData;
import com.example.administrator.communityhelp.webserviceutils.ProgressDialogUtils;
import com.example.administrator.communityhelp.webserviceutils.WebServiceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/12/30.
 */
public class FirstPage extends Fragment implements View.OnClickListener {
    SqlHelper helper;
    public SQLiteDatabase dbweiter;
    public SQLiteDatabase dbReader;
    private TextView text_cell, text_feedback;
    private ViewPager viewPager;
    private CardView card_bianmin, card_taoshihui, card_wuye, card_linliquan;
    private List<ImageView> list = new ArrayList<>();
    private List<ImageView> point_list = new ArrayList<>();
    private LinearLayout linearLayout;
    private List<JSONObject> images = new ArrayList<>();
    private Intent intent, intent1, intent2, intent3, intent4, intent5;
    private Handler handler;
    private int currentItem;
    private Thread mThread;
    private Firstpageviewpager adapter;
    private boolean flag = true;//退出时销毁子线程
    public static final String URL = "http://120.27.5.22:8080/services/takeTurnsService";
    public static final String NameSpace = "http://service.zhidisoft.com";  //命名空间
    public static final String MethodName = "takeTurnsList";//方法名
    private String communityCode;
    private boolean cellIsChanged;
    private String cellBeforeChanged;
    private String cellAfterChanged;
    private boolean isFirst = true;
    private boolean isClick = false;
    private int state_skipIn = 1;//默认跳转其他activity时未登录
    private int state_skioOut = 1;//默认其他activity跳转到此时未登录
    private ProgressDialogUtils progress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.firstpage, container, false);
        init(view);
        reInit();
        setCommunity(checkCommunity());
        if (checkCommunity() == null) {
            cellBeforeChanged = "";
        } else {
            cellBeforeChanged = checkCommunity();
        }
        handler = new Handler() {//外部ViewPager切换到第三个视图，本视图会被销毁（currentItem作为状态保存了），重建时，currentItem会被恢复
            //此时子线程自增色currentI会被重置，即子线程在视图销毁期间做了无用功
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                int what = msg.what;
                if (!cellIsChanged) {
                    viewPager.setCurrentItem(what);
                } else {
                    currentItem = 0;
                    viewPager.setCurrentItem(0);
                    cellBeforeChanged = checkCommunity();
                    cellIsChanged = false;
                }
            }
        };
        if (mThread == null) {
            mThread = new Thread() {
                @Override
                public void run() {
                    super.run();
                    while (flag) {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (images.size() > 0) {
                            currentItem %= images.size();
                        }
                        if (handler != null && list.size() > 1) {
                            Message msg = handler.obtainMessage(currentItem);
                            handler.sendMessage(msg);
                        } else {
                            currentItem = 0;
                        }
                        if (currentItem >= images.size() - 1) {
                            currentItem = 0;
                        } else {
                            currentItem++;
                        }
                    }
                }
            };
            mThread.start();
        }
        return view;
    }

    private void setCommunity(String s) {
        text_cell.setText(s);
//        if(isLoad()){
//            String name=getUserData().getCommunityName();
//            text_cell.setText(getUserData().getCommunityName());
//        }
    }

    public String checkCommunity() {
        String name = "";
        Cursor cursor = dbReader.query("community", null, null, null, null, null, null, null);
        try {
            while (cursor.moveToNext()) {
                name = cursor.getString(cursor.getColumnIndex("communityName"));
            }
        } catch (Exception e) {
            return null;
        }
        if (name == null) {
            return null;
        }
        return name;
    }

    @Override
    public void onStart() {
        super.onStart();
        progress = new ProgressDialogUtils(getActivity());
        progress.show();
        setCommunity(checkCommunity());
        cellAfterChanged = checkCommunity();
        if (!cellBeforeChanged.equals(cellAfterChanged)) {
            cellIsChanged = true;
        }
        if (isLoad()) {
            state_skipIn = 2;
        } else {
            state_skipIn = 1;
        }
        if (state_skipIn != state_skioOut || cellIsChanged) {
            dataClear();//list.size=0,得重新设置adapter,重新获取数据
        }
        communityCode = getUserData().getCommunityCode();
        if (communityCode == null) {
            Cursor cursor = dbReader.rawQuery("select * from community", null);
            while (cursor.moveToNext()) {
                communityCode = cursor.getString(cursor.getColumnIndex("communityCode"));
            }
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("communityCode", communityCode);
        WebServiceUtils.callWebService(URL, MethodName, NameSpace, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(SoapObject result) {
                if (result != null) {
                    String json = result.getProperty(0).toString();
                    try {
                        if (isClick || isFirst || (state_skipIn != state_skioOut) || cellIsChanged) {
                            JSONArray array = new JSONArray(json);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                images.add(object);
                            }
                        }
                        if (list.size() == 0) {
                            getData();
                            adapter = new Firstpageviewpager(getActivity(), list);
                            viewPager.setAdapter(adapter);
                            reInit();
                            if (isFirst) {
                                isFirst = false;
                            } else if (isClick) {
                                isClick = false;
                            }

                        }
                        if (images.size() > 0) {
                            for (int i = 0; i < list.size(); i++) {
                                String type = images.get(i).getString("type").toString();
                                final int j = i;
                                if ("info".equals(type)) {
                                    list.get(i).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getActivity(), Details.class);
                                            try {
                                                intent.putExtra("id", images.get(j).getString("objId").toString());
                                                startActivity(intent);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                } else if ("goods".equals(type)) {
                                    list.get(i).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getActivity(), TaoShiHui_ShangPingXiangQingActivity.class);
                                            try {
                                                intent.putExtra("id", images.get(j).get("objId").toString());
                                                startActivity(intent);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(), "无法获取网络数据，请检查网络连接状态！", Toast.LENGTH_SHORT).show();
                }
                progress.dismiss();
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isLoad()) {
            state_skioOut = 2;
        } else {
            state_skioOut = 1;
        }
        if (isClick) {
            dataClear();
        }
    }

    private void dataClear() {
        images.clear();
        for (int i = 0; i < list.size(); i++) {//防止缓存
            viewPager.removeView(list.get(i));
        }
        list.clear();
        for (int i = 0; i < point_list.size(); i++) {
            linearLayout.removeView(point_list.get(i));
        }
        point_list.clear();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        flag = false;
    }

    private void reInit() {
        viewPager.setCurrentItem(currentItem);
        if (point_list.size() > 1) {
            point_list.get(currentItem).setImageResource(R.mipmap.image_indicator_focus);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    for (int i = 0; i < point_list.size(); i++) {
                        point_list.get(i).setImageResource(R.mipmap.image_indicator);
                    }
                    point_list.get(position).setImageResource(R.mipmap.image_indicator_focus);
                    currentItem = viewPager.getCurrentItem();
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }

    private void getData() {
        if (images.size() == 0) {
            ImageView image = new ImageView(getActivity());
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            image.setImageResource(R.drawable.poster1);
            list.add(image);
            return;
        }
        if (images.size() == 1) {
            ImageView image = new ImageView(getActivity());
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            try {
                Log.e("cat", images.get(0).getString("filePath"));
                new GlideLoader().displayImage(getActivity(), images.get(0).getString("filePath"), image);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            list.add(image);
            return;
        }
        for (int i = 0; i < images.size(); i++) {
            ImageView image = new ImageView(getActivity());
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            try {
                Log.e("cat", images.get(0).getString("filePath"));
                new GlideLoader().displayImage(getActivity(), images.get(i).getString("filePath"), image);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            list.add(image);
            ImageView image_point = new ImageView(getActivity());
            image_point.setScaleType(ImageView.ScaleType.CENTER_CROP);
            image_point.setImageResource(R.mipmap.image_indicator);
            linearLayout.addView(image_point);
            point_list.add(image_point);
        }
    }

    private void init(View view) {
        helper = new SqlHelper(getActivity());
        dbReader = helper.getReadableDatabase();
        dbweiter = helper.getWritableDatabase();
        intent = new Intent(view.getContext(), BianMin_Main_Major.class);
        intent1 = new Intent(view.getContext(), TaoShiHui_Main_Major.class);
        intent2 = new Intent(view.getContext(), WuYe_Main_Major.class);
        intent3 = new Intent(view.getContext(), LinLi_Main_Major.class);

        text_cell = (TextView) view.findViewById(R.id.text_cell);
        text_cell.setOnClickListener(this);
        text_feedback = (TextView) view.findViewById(R.id.text_feedback);
        text_feedback.setOnClickListener(this);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);

        card_bianmin = (CardView) view.findViewById(R.id.card_bianmin);
        ImageScale ims1 = new ImageScale(getActivity(), card_bianmin, intent);
        ims1.animate();
        card_taoshihui = (CardView) view.findViewById(R.id.card_taoshihui);
        ImageScale ims2 = new ImageScale(getActivity(), card_taoshihui, intent1);
        ims2.animate();
        card_wuye = (CardView) view.findViewById(R.id.card_wuye);
        ImageScale ims3 = new ImageScale(getActivity(), card_wuye, intent2);
        ims3.animate();
        card_linliquan = (CardView) view.findViewById(R.id.card_linliquan);
        ImageScale ims4 = new ImageScale(getActivity(), card_linliquan, intent3);
        ims4.animate();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_cell:
                isClick = true;
                intent4 = new Intent(v.getContext(), Community_Choice.class);
                startActivity(intent4);
                break;
            case R.id.text_feedback:
                if (isLoad()) {
                } else {
                    Intent intent = new Intent(getActivity(), Login_Main.class);
                    startActivity(intent);
                    return;
                }
                intent5 = new Intent(v.getContext(), FeedBackPage.class);
                startActivity(intent5);
                break;
        }
    }

    public void unLoadSet() {

    }

    public void loadSet() {
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

}
