package com.example.administrator.communityhelp.firstpage.taoshihui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.communityhelp.BaseActivity;
import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.firstpage.PictureViewPagerActivity2;
import com.example.administrator.communityhelp.firstpage.bianmin.activity.Image_Big;
import com.example.administrator.communityhelp.my_other.GlideLoader;
import com.example.administrator.communityhelp.myadapter.TaoShiHuiViewPagerAdapter;
import com.example.administrator.communityhelp.myadapter.TaoShiHuiXiangQingAdapter;
import com.example.administrator.communityhelp.thirdpage.publication.bbs.PictureViewPagerActivity;
import com.example.administrator.communityhelp.webserviceutils.ProgressDialogUtils;
import com.example.administrator.communityhelp.webserviceutils.WebServiceUtils;
import com.yancy.imageselector.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaoShiHui_ShangPingXiangQingActivity extends BaseActivity implements View.OnClickListener{
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
    private RatingBar ratingBar;
    private ProgressDialogUtils progress;

    private ArrayList<String> pic;
    private String picPath;

    @Override
    public void init() {
        super.init();
        setContentView(R.layout.activity_tao_shi_hui__shang_ping_xiang_qing);
        initView();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        jsonMap = new HashMap<>();
        GetRequestMessageRecycler();
    }

    private void initView() {
        textView_name= (TextView) findViewById(R.id.spxq_name);
        textView_price= (TextView) findViewById(R.id.spxq_price);
        textView_oldPrice= (TextView) findViewById(R.id.spxq_oldPrice);
        peopleBuy= (TextView) findViewById(R.id.spxq_peopleBuy);
        textView_pingFen= (TextView) findViewById(R.id.textView_pingFen);
        textView_pingJia= (TextView) findViewById(R.id.textView_pingJia);
        textView_shangdianAdr= (TextView) findViewById(R.id.spxq_shangdianAdr);
        textView_shangdianName= (TextView) findViewById(R.id.spxq_shangdianName);
        textView_jianjie= (TextView) findViewById(R.id.spxq_jianjie);
        findViewById(R.id.TaoShiHui_backSPLB).setOnClickListener(this);
        findViewById(R.id.spxq_nowBuy).setOnClickListener(this);
        findViewById(R.id.spxq_pingjia).setOnClickListener(this);
        findViewById(R.id.spxq_shangdian).setOnClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        viewPager= (ViewPager) findViewById(R.id.viewPager);
        ratingBar= (RatingBar) findViewById(R.id.ratingBar);
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
                    Log.e("json", "json: "+json);
                    jsonMap = jsonPaseRecycler("["+json+"]");
                    //Log.e("jsonMap", "jsonMap: "+jsonMap);
                    textView_name.setText(jsonMap.get("goodsName")+"");
                    textView_jianjie.setText(jsonMap.get("summary")+"");
                    textView_price.setText(jsonMap.get("discountPrice")+"");
                    textView_oldPrice.setText(jsonMap.get("oldPrice")+"");
                    peopleBuy.setText(jsonMap.get("peopleBuy")+"");
                    ratingBar.setMax(5);
                    Log.e("total", jsonMap.get("total").toString());
                    ratingBar.setRating(Float.parseFloat(jsonMap.get("total").toString().replaceAll("分","")));
                    if(jsonMap.get("total").equals("0分")){
                        textView_pingFen.setText("暂未评价");
                    }else{
                        textView_pingFen.setText(jsonMap.get("total")+"");
                    }
                    textView_pingJia.setText(jsonMap.get("evaluationCount")+"");
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
                        adapter = new TaoShiHuiXiangQingAdapter(listRecycler, TaoShiHui_ShangPingXiangQingActivity.this);
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
                            new GlideLoader().displayImage(TaoShiHui_ShangPingXiangQingActivity.this,picPath,viewImage);
                            list.add(view);
                            layout= (LinearLayout) findViewById(R.id.linearLayout);
                            ImageView im = new ImageView(TaoShiHui_ShangPingXiangQingActivity.this);
                            ImageView iv = new ImageView(TaoShiHui_ShangPingXiangQingActivity.this);
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
                                    intent=new Intent(TaoShiHui_ShangPingXiangQingActivity.this,PictureViewPagerActivity2.class);
                                    Log.e("pic", pic.size()+"");
                                    intent.putStringArrayListExtra("path",pic);
                                    intent.putExtra("position", finalI);
                                    startActivity(intent);
                                }
                            });
                        }
                        viewPagerAdapter = new TaoShiHuiViewPagerAdapter(list, TaoShiHui_ShangPingXiangQingActivity.this);
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
                    //Toast若是new出来，则需设置setView
                    Toast toast = Toast.makeText(TaoShiHui_ShangPingXiangQingActivity.this, "", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    View view = getLayoutInflater().inflate(R.layout.toastview, null);
                    TextView toastText = (TextView) view.findViewById(R.id.toast_text);
                    toastText.setText("无法获取网络数据，请检查网络连接状态！");
                    toast.setView(view);
                    toast.show();
                }
            }
        });
    }

    //解析json数据
    public Map<String, Object> jsonPaseRecycler(String result) {
        try {
            JSONArray array = new JSONArray(result);
            for (int i = 0; i < array.length(); i++) {
                JSONObject jo = array.getJSONObject(i);
                String total = jo.getString("total");
                String summary = jo.getString("summary");
                String oldPrice = jo.getString("oldPrice");
                String discountPrice = jo.getString("discountPrice");
                String goodsNum = jo.getString("goodsNum");
                String peopleBuy = jo.getString("peopleBuy");
                String goodsName = jo.getString("goodsName");
                String evaluationCount = jo.getString("evaluationCount");
                String merchantName = jo.getString("merchantName");
                String merchantAddr = jo.getString("merchantAddr");
                String merchantId = jo.getString("merchantId");
                String picPathSet = jo.getString("picPathSet");
                String details = jo.getString("details");
                jsonMap.put("total", total+"分");
                jsonMap.put("summary", summary);
                jsonMap.put("oldPrice", oldPrice+"元");
                jsonMap.put("discountPrice", discountPrice+"元");
                jsonMap.put("goodsNum", "数量(剩余:"+goodsNum+")");
                jsonMap.put("peopleBuy", "已售 "+peopleBuy);
                jsonMap.put("goodsName", goodsName);
                jsonMap.put("evaluationCount", evaluationCount+"人评价");
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
            case R.id.spxq_nowBuy:
                intent=new Intent(this,TaoShiHui_DingDanXiangQingActivity.class);
                intent.putExtra("merchantsGoodsId",this.getIntent().getStringExtra("id"));
                intent.putExtra("name",textView_name.getText());
                intent.putExtra("price",textView_price.getText());
                intent.putExtra("goodsNum",jsonMap.get("goodsNum")+"");
                startActivity(intent);
                break;
            case R.id.spxq_pingjia:
//                intent=new Intent(this,TaoShiHui_PingJiaActivity.class);
//                startActivity(intent);
                //Toast若是new出来，则需设置setView
                Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                View view = getLayoutInflater().inflate(R.layout.toastview, null);
                TextView toastText = (TextView) view.findViewById(R.id.toast_text);
                toastText.setText("未提供接口，该功能暂未实现！");
                toast.setView(view);
                toast.show();
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
