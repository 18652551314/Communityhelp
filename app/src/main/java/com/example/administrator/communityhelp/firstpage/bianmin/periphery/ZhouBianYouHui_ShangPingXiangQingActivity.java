package com.example.administrator.communityhelp.firstpage.bianmin.periphery;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.communityhelp.BaseActivity;
import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.firstpage.PictureViewPagerActivity2;
import com.example.administrator.communityhelp.firstpage.bianmin.activity.Image_Big;
import com.example.administrator.communityhelp.firstpage.taoshihui.TaoShiHui_DingDanXiangQingActivity;
import com.example.administrator.communityhelp.firstpage.taoshihui.TaoShiHui_PingJiaActivity;
import com.example.administrator.communityhelp.firstpage.taoshihui.TaoShiHui_ShangDianShangPingLieBiaoActivity;
import com.example.administrator.communityhelp.my_other.GlideLoader;
import com.example.administrator.communityhelp.myadapter.TaoShiHuiViewPagerAdapter;
import com.example.administrator.communityhelp.myadapter.TaoShiHuiXiangQingAdapter;
import com.example.administrator.communityhelp.webserviceutils.ProgressDialogUtils;
import com.example.administrator.communityhelp.webserviceutils.WebServiceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZhouBianYouHui_ShangPingXiangQingActivity extends BaseActivity implements View.OnClickListener{
    public static final String NameSpace = "http://goods.service.zhidisoft.com";  //命名空间
    public static final String MethodNameRecycler = "goodsContent";//方法名
    public static final String URLRecycler = "http://120.27.5.22:8080/services/goodsService";//地址
    private Map<String, Object> jsonMap;

    private TextView textView_name,textView_price,textView_oldPrice,peopleBuy,textView_shangdianName,
            textView_pingFen,textView_pingJia,textView_shangdianAdr,textView_jianjie;
    private Intent intent;
    private RecyclerView recyclerView;
    private TaoShiHuiXiangQingAdapter adapter;
    private List<Map<String, Object>> listRecycler;
    private ViewPager viewPager;
    private TaoShiHuiViewPagerAdapter viewPagerAdapter;
    private List<View> list,point_list;
    private LinearLayout layout;
    private int currentItem;
    private ProgressDialogUtils progress;
    private ArrayList<String> pic;
    private String picPath;

    @Override
    public void init() {
        super.init();
        setContentView(R.layout.activity_zhou_bian_you__hui_shang_ping_xiang_qing);
        initView();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        GetRequestMessageRecycler();
    }

    private void initView() {
        textView_name= (TextView) findViewById(R.id.spxq_name);
        textView_price= (TextView) findViewById(R.id.spxq_price);
        textView_oldPrice= (TextView) findViewById(R.id.spxq_oldPrice);
        textView_shangdianAdr= (TextView) findViewById(R.id.spxq_shangdianAdr);
        textView_shangdianName= (TextView) findViewById(R.id.spxq_shangdianName);
        textView_jianjie= (TextView) findViewById(R.id.spxq_jianjie);
        findViewById(R.id.TaoShiHui_backSPLB).setOnClickListener(this);
        findViewById(R.id.spxq_shangdian).setOnClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        viewPager= (ViewPager) findViewById(R.id.viewPager);
    }

    private void GetRequestMessageRecycler() {
        //访问时弹出dialog对话框
        progress=new ProgressDialogUtils(this);
        progress.show();
        //访问webservice服务需要的参数
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", this.getIntent().getStringExtra("id"));
        WebServiceUtils.callWebService(URLRecycler, MethodNameRecycler, NameSpace, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(SoapObject result) {
                progress.dismiss();
                if (result != null) {
                    //获取json数据
                    String json = result.getProperty(0).toString();
                    //解析json数据并放入list集合中
                    //Log.e("json", "json: "+json);
                    jsonMap = jsonPaseRecycler("["+json+"]");
                    //Log.e("jsonMap", "jsonMap: "+jsonMap);
                    textView_name.setText(jsonMap.get("goodsName")+"");
                    textView_jianjie.setText(jsonMap.get("summary")+"");
                    textView_price.setText(jsonMap.get("discountPrice")+"");
                    textView_oldPrice.setText(jsonMap.get("oldPrice")+"");
                    textView_shangdianAdr.setText(jsonMap.get("merchantAddr")+"");
                    textView_shangdianName.setText(jsonMap.get("merchantName")+"");
                    textView_oldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    try {
                        listRecycler=new ArrayList();
                        JSONArray array = new JSONArray(jsonMap.get("details")+"");
                        for (int i = 0; i < array.length(); i++) {
                            Map<String ,Object> map=new HashMap<>();
                            JSONObject jo = array.getJSONObject(i);
                            String title = jo.getString("title");
                            String content = jo.getString("content");
                            map.put("title",title);
                            map.put("content",content);
                            listRecycler.add(map);
                        }
                        adapter = new TaoShiHuiXiangQingAdapter(listRecycler, ZhouBianYouHui_ShangPingXiangQingActivity.this);
                        recyclerView.setAdapter(adapter);
                        list=new ArrayList<>();
                        point_list=new ArrayList<>();
                        pic=new ArrayList<>();
                        JSONArray arr = new JSONArray(jsonMap.get("picPathSet")+"");
                        for (int j = 0; j < arr.length(); j++) {
                            JSONObject jo = arr.getJSONObject(j);
                            picPath = jo.getString("picPath");
                            View view = getLayoutInflater().inflate(R.layout.taoshihui_shangpingxiangqing_viewpager, null);
                            ImageView viewImage= (ImageView) view.findViewById(R.id.spxq_image);
                            new GlideLoader().displayImage(ZhouBianYouHui_ShangPingXiangQingActivity.this,picPath,viewImage);
                            list.add(view);
                            layout= (LinearLayout) findViewById(R.id.linearLayout);
                            ImageView im = new ImageView(ZhouBianYouHui_ShangPingXiangQingActivity.this);
                            ImageView iv = new ImageView(ZhouBianYouHui_ShangPingXiangQingActivity.this);
                            im.setBackgroundResource(R.mipmap.dot_unselect);
                            iv.setBackgroundResource(R.mipmap.dot_unselect);
                            iv.setVisibility(View.INVISIBLE);
                            layout.addView(im);
                            layout.addView(iv);
                            point_list.add(im);
                            pic.add(picPath);
                        }
                        for (int i = 0; i < list.size(); i++) {
                            final int finalI = i;
                            list.get(i).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    intent=new Intent(ZhouBianYouHui_ShangPingXiangQingActivity.this,PictureViewPagerActivity2.class);
                                    Log.e("pic", pic.size()+"");
                                    intent.putStringArrayListExtra("path",pic);
                                    intent.putExtra("position", finalI);
                                    startActivity(intent);
                                }
                            });
                        }
                        viewPagerAdapter = new TaoShiHuiViewPagerAdapter(list, ZhouBianYouHui_ShangPingXiangQingActivity.this);
                        viewPager.setAdapter(viewPagerAdapter);

                        viewPager.setCurrentItem(currentItem);
                        point_list.get(currentItem).setBackgroundResource(R.mipmap.dot_select);
                        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                            }

                            @Override
                            public void onPageSelected(int position) {
                                for (int i = 0; i < point_list.size(); i++) {
                                    point_list.get(i).setBackgroundResource(R.mipmap.dot_unselect);
                                }
                                point_list.get(position).setBackgroundResource(R.mipmap.dot_select);
                                currentItem = viewPager.getCurrentItem();
                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {

                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(ZhouBianYouHui_ShangPingXiangQingActivity.this, "无法获取网络数据，请检查网络连接状态！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //解析json数据
    public Map<String, Object> jsonPaseRecycler(String result) {
        jsonMap = new HashMap<>();
        try {
            JSONArray array = new JSONArray(result);
            for (int i = 0; i < array.length(); i++) {
                JSONObject jo = array.getJSONObject(i);
                String total = jo.getString("total");
                String summary = jo.getString("summary");
                String oldPrice = jo.getString("oldPrice");
                String discountPrice = jo.getString("discountPrice");
                String goodsName = jo.getString("goodsName");
                String merchantName = jo.getString("merchantName");
                String merchantAddr = jo.getString("merchantAddr");
                String merchantId = jo.getString("merchantId");
                String picPathSet = jo.getString("picPathSet");
                String details = jo.getString("details");
                jsonMap.put("total", total+"分");
                jsonMap.put("summary", summary);
                jsonMap.put("oldPrice", oldPrice+"元");
                jsonMap.put("discountPrice", discountPrice+"元");
                jsonMap.put("goodsName", goodsName);
                jsonMap.put("merchantName", merchantName);
                jsonMap.put("merchantAddr", merchantAddr);
                jsonMap.put("merchantId", merchantId);
                jsonMap.put("picPathSet", picPathSet);
                jsonMap.put("details", details);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonMap;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.TaoShiHui_backSPLB:
                finish();
                break;
            case R.id.spxq_shangdian:
                intent=new Intent(this,TaoShiHui_ShangDianShangPingLieBiaoActivity.class);
                intent.putExtra("name",textView_shangdianName.getText().toString());
                intent.putExtra("merchantId",jsonMap.get("merchantId")+"");
                startActivity(intent);
                break;
        }
    }
}
