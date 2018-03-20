package com.example.administrator.communityhelp.firstpage.bianmin.activity;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.communityhelp.BaseActivity;
import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.firstpage.PictureViewPagerActivity2;
import com.example.administrator.communityhelp.my_other.GlideLoader;
import com.example.administrator.communityhelp.myadapter.MyPageAdapter;
import com.example.administrator.communityhelp.webserviceutils.ProgressDialogUtils;
import com.example.administrator.communityhelp.webserviceutils.WebServiceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Details extends BaseActivity {

    private View v1,v2,v3;
    private ViewPager vPager;
    private ArrayList<View> aList;
    private MyPageAdapter mAdapter;
    Intent intent;
    ImageView image=null;
    private LinearLayout layout_back;
    private TextView title,time,content;
    private List<String> list=new ArrayList();
    public static String URL="http://120.27.5.22:8080/services/generalInfoService";
    public static String NAMESPACE="http://info.service.zhidisoft.com";
    public static String MethodName="generalInfoContent";
    private ProgressDialogUtils progress;

    @Override
    public void init() {
        super.init();
        setContentView(R.layout.activity_details);
        vPager = (ViewPager) findViewById(R.id.details_vp);
        layout_back= (LinearLayout) findViewById(R.id.layout_back);
        //返回
        layout_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title= (TextView) findViewById(R.id.title);
        time= (TextView) findViewById(R.id.time);
        content= (TextView) findViewById(R.id.content);

        String id=getIntent().getStringExtra("id");
        progress=new ProgressDialogUtils(this);
        progress.show();
        HashMap<String,Object> map=new HashMap<>();
        map.put("id",id);
        WebServiceUtils.callWebService(URL, MethodName, NAMESPACE, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(SoapObject result) {
                progress.dismiss();
                if(result!=null){
                    String json=result.getProperty(0).toString();
                    Log.e("tag",json);
                    try {
                        JSONObject object=new JSONObject(json);
                        if(object!=null){
                            title.setText(object.getString("title"));
                            time.setText(object.getString("releaseDate"));
                            content.setText(Html.fromHtml(object.getString("content")));
                            JSONArray array=object.getJSONArray("picPathSet");
                            if(array.length()!=0){
                                for(int i=0;i<array.length();i++){
                                    JSONObject picJson=array.getJSONObject(i);
                                    list.add(picJson.getString("picPath"));
                                }
                            }
                            //动态加载三个View
                            aList = new ArrayList<View>();
                            LayoutInflater li = getLayoutInflater();
                            for(int i=0;i<list.size();i++){
                                v1 = li.inflate(R.layout.image_details, null);
                                image= (ImageView) v1.findViewById(R.id.iv_details);
                                new GlideLoader().displayImage(Details.this,list.get(i),image);
                                aList.add(v1);
                            }
                            mAdapter = new MyPageAdapter(aList);
                            vPager.setAdapter(mAdapter);
                        }
                        if(aList.size()!=0){
                            for(int i=0;i<aList.size();i++){
                                final int finalI = i;
                                aList.get(i).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        intent=new Intent(Details.this,PictureViewPagerActivity2.class);
                                        intent.putStringArrayListExtra("path", (ArrayList<String>) list);
                                        startActivity(intent);
                                    }
                                });
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        vPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}
