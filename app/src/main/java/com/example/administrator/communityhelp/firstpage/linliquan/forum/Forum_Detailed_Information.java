package com.example.administrator.communityhelp.firstpage.linliquan.forum;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.communityhelp.BaseActivity;
import com.example.administrator.communityhelp.Login_Main;
import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.firstpage.PictureViewPagerActivity2;
import com.example.administrator.communityhelp.firstpage.linliquan.carpooling.Carpooling_Detaile_Information;
import com.example.administrator.communityhelp.my_other.CircularImage;
import com.example.administrator.communityhelp.my_other.GlideLoader;
import com.example.administrator.communityhelp.myadapter.Firstpageviewpager;
import com.example.administrator.communityhelp.thirdpage.publication.bbs.PictureViewPagerActivity;
import com.example.administrator.communityhelp.webserviceutils.ProgressDialogUtils;
import com.example.administrator.communityhelp.webserviceutils.WebServiceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/6.
 */
public class Forum_Detailed_Information extends BaseActivity implements OnClickListener {
    private int state_skipIn=1;//默认跳转其他activity时未登录
    private int state_skioOut=1,dianzan=1;//默认其他activity跳转到此时未登录
    List list_fei=new ArrayList();
    ProgressDialogUtils progress;
    private CircularImage imageView_touxiang;
    private boolean flag = true;//退出时销毁子线程
    private int ff=-1;
    private List<JSONObject> images = new ArrayList<>();
    private String name_fangfa;
    private ImageView clickOnLike, leaveWord, imageView_tupian,dianzan_iv;
    private ViewPager viewPager_tupian;
    Bitmap picPath1;
    //private int[] images = {R.drawable.poster1, R.drawable.blerweima, R.drawable.banshizhinan,R.drawable.ganguoganhuo};
    private List<Map<String, Object>> list_iv = new ArrayList();
    private LinearLayout linearLayout;
    private int currentItem;
    private int CLICK = 1;//first点击
    private int UNCLICK = 2;//second点击
    private int STATE = 1;//点击状态
    private Intent intent;
    private String picPath,dierge;
    private TextView back;
    private Firstpageviewpager adapter;
    private List<ImageView> list = new ArrayList<>();
    private List<ImageView> point_list = new ArrayList<>();
    private List<Map<String, Object>> listRecycler;
    //以下为借口部分数据
    private List<Map<String, Object>> listGridTop, listGridCenter;
    String ures;
    public static final String NameSpace = "http://bbs.service.zhidisoft.com";  //命名空间
    public static final String MethodNameTop = "bbsInfo";//方法名
    public static final String URL = "http://120.27.5.22:8080/services/bbsService";//地址
    public int mun = 10;
    Intent intent_id;
    String Id,isPraise="";
    TextView textView_name, textView_time, textView_tit, textView_con, replyNum, praiseNum;

    @Override
    public void init() {
        super.init();
        setContentView(R.layout.forum_detailed_information);
        intent_id = getIntent();
        Id = intent_id.getStringExtra("id");
        isPraise=intent_id.getStringExtra("isPraise");
        dianzan_iv= (ImageView) findViewById(R.id.iv_forum_detailed_dianzan);
        if (isPraise!=null) {
            if (isPraise.equals("Y")) {
                dianzan_iv.setBackgroundResource(R.mipmap.zan2);
            } else {
                dianzan_iv.setBackgroundResource(R.mipmap.zan1);
            }
        }
        clickOnLike = (ImageView) findViewById(R.id.iv_forum_detailed_dianzan);
        leaveWord = (ImageView) findViewById(R.id.iv_forum_detailed_mess);
        back = (TextView) findViewById(R.id.textview_back_forum_detailed);
        textView_name = (TextView) findViewById(R.id.tv_forum_detailed_name);
        textView_time = (TextView) findViewById(R.id.tv_forum_detailed_time);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout_forum);
        textView_tit = (TextView) findViewById(R.id.tv_forum_detailed_title);
        textView_con = (TextView) findViewById(R.id.tv_forum_detailed_context);
        imageView_touxiang = (CircularImage) findViewById(R.id.iv_forum_detailed);
        //imageView_tupian= (ImageView) findViewById(R.id.tv_forum_detailed_picture);
        viewPager_tupian = (ViewPager) findViewById(R.id.tv_forum_detailed_picture);
        replyNum = (TextView) findViewById(R.id.tv_forum_detailed_mess);
        praiseNum = (TextView) findViewById(R.id.tv_forum_detailed_dianzan);

        findViewById(R.id.iv_forum_detailed_dianzan).setOnClickListener(this);
        findViewById(R.id.iv_forum_detailed_mess).setOnClickListener(this);
        findViewById(R.id.textview_back_forum_detailed).setOnClickListener(this);

        GetRequestMessageTop();

        viewPager_tupian.setOnTouchListener(new View.OnTouchListener() {
            int flage = 0 ;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        flage = 0 ;
                        break ;
                    case MotionEvent.ACTION_MOVE:
                        flage = 1 ;
                        break ;
                    case  MotionEvent.ACTION_UP :
                        if (flage == 0) {
                            int item = viewPager_tupian.getCurrentItem();
                            //saveImagePath(list_fei);
                            if (item == 0) {
                                Intent intent = new Intent(Forum_Detailed_Information.this, PictureViewPagerActivity2.class);
                                intent.putStringArrayListExtra("path", (ArrayList<String>) list_fei);
                                Forum_Detailed_Information.this.startActivity(intent);
                            } else if (item == 1) {
                                Intent intent = new Intent(Forum_Detailed_Information.this, PictureViewPagerActivity2.class);
                                intent.putStringArrayListExtra("path", (ArrayList<String>) list_fei);
                                Forum_Detailed_Information.this.startActivity(intent);
                            } else if (item == 2) {
                                Intent intent = new Intent(Forum_Detailed_Information.this, PictureViewPagerActivity2.class);
                                intent.putStringArrayListExtra("path", (ArrayList<String>) list_fei);
                                Forum_Detailed_Information.this.startActivity(intent);
                            }else if (item == 3) {
                                Intent intent = new Intent(Forum_Detailed_Information.this, PictureViewPagerActivity2.class);
                                intent.putStringArrayListExtra("path", (ArrayList<String>) list_fei);
                                Forum_Detailed_Information.this.startActivity(intent);
                            }
                        }
                        break ;


                }
                return false;
            }
        });



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_forum_detailed_dianzan:
                if(isLoad()){}else {
                    Intent intent=new Intent(this, Login_Main.class);
                    startActivity(intent);
                    return;
                }
                dianzan=Integer.parseInt(listRecycler.get(0).get("praiseNum").toString());
                if (isPraise.equals("Y")) {
                    dierge=praiseNum.getText().toString();
                    if (listRecycler.get(0).get("praiseNum").toString().equals(dierge)){
                        dianzan--;
                    }

                    dianzan_iv.setBackgroundResource(R.mipmap.zan1);

                    name_fangfa="cancelPraise";
                    isPraise="N";
                    GetRequestMessageTop1();

                    praiseNum.setText(dianzan+"");

                } else if (isPraise.equals("N")){
                    dierge=praiseNum.getText().toString();
                    if (listRecycler.get(0).get("praiseNum").toString().equals(dierge)){
                        dianzan++;
                    }

                    dianzan_iv.setBackgroundResource(R.mipmap.zan2);
                    isPraise="Y";
                    name_fangfa="addPraise";
                    GetRequestMessageTop1();

                    praiseNum.setText(dianzan+"");

                }
                break;
            case R.id.iv_forum_detailed_mess:
                intent = new Intent(Forum_Detailed_Information.this, Forum_Reply.class);
                startActivity(intent);
                break;
            case R.id.textview_back_forum_detailed:
                finish();
                break;
        }
    }
    //以下为接口数据部分

    private void GetRequestMessageTop() {
        //访问时弹出dialog对话框
        progress=new ProgressDialogUtils(this);
        progress.show();
        //访问webservice服务需要的参数
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", Id);
        map.put("userId", "");
        Log.e("json", "id: " + Id);

        WebServiceUtils.callWebService(URL, MethodNameTop, NameSpace, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(SoapObject result) {
                Log.e("json", "json: " + result);
                progress.dismiss();

                if (result != null) {
                    //获取json数据
                    String json = result.getProperty(0).toString();
                    //解析json数据并放入list集合中
                    Log.e("json", "json: " + json);
                    listRecycler = jsonPaseTop(json);
                    textView_name.setText(listRecycler.get(0).get("releaseUserName").toString());
                    textView_time.setText(listRecycler.get(0).get("createTime").toString());
                    textView_tit.setText(listRecycler.get(0).get("createTime").toString());
                    textView_con.setText(Html.fromHtml(listRecycler.get(0).get("content").toString()));
                    replyNum.setText(listRecycler.get(0).get("replyNum").toString());
                    praiseNum.setText(listRecycler.get(0).get("praiseNum").toString());
                    dianzan=Integer.parseInt(listRecycler.get(0).get("praiseNum").toString());
                    new GlideLoader().displayImage(Forum_Detailed_Information.this, listRecycler.get(0).get("headimgurl") + "", imageView_touxiang);
                    list_fei.clear();
                    for (int j = 0; j < list_iv.size(); j++) {
                        if (ff==-1) {
                            list_fei.add(list_iv.get(j).get("picPath"));
                        }
                    }

                    if (list.size() == 0) {
                        getData();
                        if (list_iv.size()!=0) {

                            adapter = new Firstpageviewpager(Forum_Detailed_Information.this, list);
                            viewPager_tupian.setAdapter(adapter);
                            reInit();
                        }
                    }
                    //ssnew GlideLoader().displayImage(Forum_Detailed_Information.this,listRecycler.get(0).get("picPath")+"",imageView_tupian);
                } else {
                    Toast.makeText(Forum_Detailed_Information.this, "获取WebService数据错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //解析json数据
    public List<Map<String, Object>> jsonPaseTop(String result) {
        listGridTop = new ArrayList();
        try {
            String jjh = "[" + result + "]";
            JSONArray array = new JSONArray(jjh);
//            JSONObject object =
//            JSONArray array = object.getJSONArray("rows");new JSONObject(result);
            Log.e("json", "json: " + array);
            for (int i = 0; i < array.length(); i++) {
                Map<String, Object> map1 = new HashMap<>();
                JSONObject jo = array.getJSONObject(i);
                String title = jo.getString("title");
                String createTime = jo.getString("createTime");
                String content = jo.getString("content");
                String releaseUserName = jo.getString("releaseUserName");
                String headimgurl = jo.getString("headimgurl");
                String replyNum = jo.getString("replyNum");
                String praiseNum = jo.getString("praiseNum");
                JSONArray array1 = new JSONArray();
                try {
                    array1 = jo.getJSONArray("picPathSet");
                } catch (Exception e) {
                    Toast.makeText(Forum_Detailed_Information.this, "没有图片", Toast.LENGTH_SHORT).show();
                }

                map1.put("title", title);
                map1.put("createTime", createTime);
                map1.put("content", content);
                map1.put("releaseUserName", releaseUserName);
                map1.put("praiseNum", praiseNum);
                map1.put("headimgurl", headimgurl);
                map1.put("replyNum", replyNum);
                list_iv.clear();
                for (int j = 0; j < array1.length(); j++) {
                    Map<String, Object> map_iv = new HashMap<>();
                    picPath = array1.getString(j);
                    map_iv.put("picPath", picPath);
                    list_iv.add(map_iv);

                }


                listGridTop.add(map1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listGridTop;
    }

    @Override
    public void onStart() {
        super.onStart();



        if(isLoad()){
            state_skipIn=2;
        }else {
            state_skipIn=1;
        }
        if(state_skipIn!=state_skioOut){
            dataClear();//list.size=0,得重新设置adapter,重新获取数据
        }

        HashMap<String,Object> map=new HashMap<>();
        map.put("id", Id);
        map.put("userId", "");
        WebServiceUtils.callWebService(URL, MethodNameTop, NameSpace, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(SoapObject result) {
                Log.e("json", "json: " + result);

                if (result != null) {
                    //获取json数据
                    String json = result.getProperty(0).toString();
                    //解析json数据并放入list集合中
                    Log.e("json", "json: " + json);
                    listRecycler = jsonPaseTop(json);
                    textView_name.setText(listRecycler.get(0).get("releaseUserName").toString());
                    textView_time.setText(listRecycler.get(0).get("createTime").toString());
                    textView_tit.setText(listRecycler.get(0).get("createTime").toString());
                    textView_con.setText(Html.fromHtml(listRecycler.get(0).get("content").toString()));
                    new GlideLoader().displayImage(Forum_Detailed_Information.this,listRecycler.get(0).get("headimgurl")+"",imageView_touxiang);
                    if (list.size() == 0) {
                        getData();
                        if (list_iv.size()!=0) {
                            adapter = new Firstpageviewpager(Forum_Detailed_Information.this, list);
                            viewPager_tupian.setAdapter(adapter);
                            reInit();
                        }
                    }
                    //ssnew GlideLoader().displayImage(Forum_Detailed_Information.this,listRecycler.get(0).get("picPath")+"",imageView_tupian);
                } else {
                    Toast.makeText(Forum_Detailed_Information.this, "获取WebService数据错误", Toast.LENGTH_SHORT).show();
                }
                ProgressDialogUtils.dismissProgressDialog();
            }
        });

    }

    private void dataClear() {
        images.clear();
        for(int i=0;i<list.size();i++){//防止缓存
            viewPager_tupian.removeView(list.get(i));
        }
        list.clear();
        for (int i = 0; i < point_list.size(); i++) {
            linearLayout.removeView(point_list.get(i));
        }
        point_list.clear();
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        list.clear();
        for (int i = 0; i < point_list.size(); i++) {
            linearLayout.removeView(point_list.get(i));
        }
        list_iv.clear();
        point_list.clear();
        ff=1;
    }

    private void reInit() {
        viewPager_tupian.setCurrentItem(currentItem);
        point_list.get(currentItem).setImageResource(R.mipmap.image_indicator_focus);
        viewPager_tupian.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < point_list.size(); i++) {
                    point_list.get(i).setImageResource(R.mipmap.image_indicator);
                }
                point_list.get(position).setImageResource(R.mipmap.image_indicator_focus);
                currentItem = viewPager_tupian.getCurrentItem();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void getData() {
        if (list_iv.size() == 0) {
            ImageView image = new ImageView(this);
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            image.setImageResource(R.drawable.poster1);
            list.add(image);
            return;
        }
        for (int i = 0; i < list_iv.size(); i++) {
            ImageView image = new ImageView(this);
            //           image.setScaleType(ImageView.ScaleType.FIT_XY);
            try{new GlideLoader().displayImage(Forum_Detailed_Information.this,list_iv.get(i).get("picPath")+"",image);}catch (Exception e){};
//            image.setImageResource(images[i]);
            list.add(image);
            ImageView image_point = new ImageView(this);
            image_point.setScaleType(ImageView.ScaleType.CENTER_CROP);
            image_point.setImageResource(R.mipmap.image_indicator);
            linearLayout.addView(image_point);
            point_list.add(image_point);
        }
    }

    public Bitmap getBitmap(String url) {
        Bitmap bm = null;
        try {
            URL iconUrl = new URL(url);
            URLConnection conn = iconUrl.openConnection();
            HttpURLConnection http = (HttpURLConnection) conn;

            int length = http.getContentLength();

            conn.connect();
            // 获得图像的字符流
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is, length);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();// 关闭流
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bm;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        flag = false;
    }



    //以下为接口数据部分

    private void GetRequestMessageTop1() {
        //访问时弹出dialog对话框
        //访问webservice服务需要的参数
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", Id);
        map.put("userId", getUserData().getUserId());


        WebServiceUtils.callWebService("http://120.27.5.22:8080/services/bbsService", name_fangfa, "http://bbs.service.zhidisoft.com", map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(SoapObject result) {
                Log.e("json", "json: " + result);

                if (result != null) {
                    //获取json数据
                    String json = result.getProperty(0).toString();
                    //解析json数据并放入list集合中
                    Log.e("json", "json: " + json);


                } else {
                    Toast.makeText(Forum_Detailed_Information.this, "获取WebService数据错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //
    public List<Map<String, Object>> jsonPaseTop1(String result) {
        listGridTop = new ArrayList();
        try {
            String jjh = "[" + result + "]";
            JSONArray array = new JSONArray(jjh);
//            JSONObject object =
//            JSONArray array = object.getJSONArray("rows");new JSONObject(result);
            Log.e("json", "11: " + array);
            for (int i = 0; i < array.length(); i++) {
                Map<String, Object> map = new HashMap<>();
                JSONObject jo = array.getJSONObject(i);
                //String title = jo.getString("title");


                //Toast.makeText(Carpooling_Detaile_Information.this, title, Toast.LENGTH_SHORT).show();
                listGridTop.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listGridTop;
    }

}
