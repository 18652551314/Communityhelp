package com.example.administrator.communityhelp.firstpage.linliquan.housing_rental;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.communityhelp.BaseActivity;
import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.firstpage.linliquan.forum.Forum_Reply;
import com.example.administrator.communityhelp.my_other.GlideLoader;
import com.example.administrator.communityhelp.myadapter.Carpooling_Detaile_Information_Adapter;
import com.example.administrator.communityhelp.myadapter.Firstpageviewpager;
import com.example.administrator.communityhelp.webserviceutils.ProgressDialogUtils;
import com.example.administrator.communityhelp.webserviceutils.WebServiceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/9.
 */
public class Housing_Detaile_Information2 extends BaseActivity {

    private List<HashMap<String, Object>> list;
    ProgressDialogUtils progress;

    private ListView listView;
    private TextView back;
    private ImageView reply, imageView_touxiang;
    private Intent intent;
    private Carpooling_Detaile_Information_Adapter adapter;

    private String title[] = {"title1", "title2", "title3", "title4", "title5", "title6"};
    private String time[] = {(String) DateFormat.format("yyyy年MM月dd日 HH:mm", new Date())};
    private String details[] = {"details1", "details12", "details13", "details14", "details15", "details16"};
    private int image[] = {R.drawable.common_loading3_0, R.drawable.common_loading3_1,
            R.drawable.common_loading3_2, R.drawable.common_loading3_3, R.drawable.common_loading3_4,
            R.drawable.common_loading3_5};

    //以下为借口部分数据
    private List<Map<String, Object>> listGridTop, listGridCenter;
    String ures;
    public static final String NameSpace = "http://info.service.zhidisoft.com";  //命名空间
    public static final String MethodNameTop = "houseRentalContent";//方法名
    public static final String URL = "http://120.27.5.22:8080/services/infoExchangeService";//地址
    public int mun = 10;
    Intent intent_id;
    String Id, content1, userId1, replyUserHeadimgurl1, travelId1, replyTime1, isAnonymous1, replyUserName1, picPathSet, picPath;
    TextView textView_name, textView_time, textView_qidian, textView_con, textView_zhongdian, textView_chufa, textView_lianxiren, textView_dianhua, textView_miaoshu, textView_miaoshu_2, textView_biaoti, replyNum;
    private List<Map<String, Object>> listRecycler;
    private List<Map<String, Object>> list_iv = new ArrayList();
    private ViewPager viewPager;
    private LinearLayout linearLayout;
    private int currentItem;
    private List<ImageView> point_list = new ArrayList<>();
    private Firstpageviewpager adapter1;
    private List<Map<String, Object>> point_list2 = new ArrayList();
    private List<ImageView> list_pp = new ArrayList<>();


    @Override
    public void init() {
        super.init();
        setContentView(R.layout.housing_detaile_information2);
        list = new ArrayList();
        listView = (ListView) findViewById(R.id.carpooling_details_listview);

        intent_id = getIntent();
        Id = intent_id.getStringExtra("id");

        textView_name = (TextView) findViewById(R.id.pinche_tv_name);
        textView_biaoti = (TextView) findViewById(R.id.tit_chuzu);
        textView_time = (TextView) findViewById(R.id.time_housing_tv);
        textView_dianhua = (TextView) findViewById(R.id.dianhua_housing_1);
        textView_miaoshu_2 = (TextView) findViewById(R.id.neirong_housing);
        imageView_touxiang = (ImageView) findViewById(R.id.pinche_iv1);
        textView_lianxiren = (TextView) findViewById(R.id.lianxiren_tv_1);
        replyNum = (TextView) findViewById(R.id.tv_housing_detailed_mess);

        listView.setFocusable(false);
        back = (TextView) findViewById(R.id.textview_back_housing_detailed);
        //返回键
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //回复信息
        reply = (ImageView) findViewById(R.id.iv_housing_detailed_mess);
        reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Housing_Detaile_Information2.this, Forum_Reply.class);
                startActivity(intent);
            }
        });


        GetRequestMessageTop();
    }


    public void getdata() {
        for (int i = 0; i < list_iv.size(); i++) {
            HashMap<String, Object> map = new HashMap();
            map.put("title", list_iv.get(i).get("replyUserName1"));
            map.put("time", list_iv.get(i).get("replyTime1"));
            map.put("datails", list_iv.get(i).get("content1"));
            map.put("image", list_iv.get(i).get("replyUserHeadimgurl1"));
            list.add(map);
        }
        adapter = new Carpooling_Detaile_Information_Adapter(list, Housing_Detaile_Information2.this);
        listView.setAdapter(adapter);
        setListViewHeightBasedOnChildren(listView);
    }

    //以下为接口数据部分

    private void GetRequestMessageTop() {
        //访问时弹出dialog对话框
        try {
            progress=new ProgressDialogUtils(this);
            progress.show();
        }catch (Exception e){}

        //访问webservice服务需要的参数
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", Id);

        Log.e("json", "id: " + Id);

        WebServiceUtils.callWebService(URL, MethodNameTop, NameSpace, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(SoapObject result) {
                Log.e("json", "json: " + result);
                try {
                    progress.dismiss();
                }catch (Exception e){}

                if (result != null) {
                    //获取json数据
                    String json = result.getProperty(0).toString();
                    //解析json数据并放入list集合中
                    Log.e("json", "json: " + json);
                    listRecycler = jsonPaseTop(json);
                    textView_name.setText(listRecycler.get(0).get("userName").toString());
                    replyNum.setText(listRecycler.get(0).get("replyNum").toString());
                    textView_time.setText(listRecycler.get(0).get("createTime").toString());
                    textView_lianxiren.setText(listRecycler.get(0).get("contact").toString());
                    textView_dianhua.setText(listRecycler.get(0).get("contactPhone").toString());
                    textView_biaoti.setText(listRecycler.get(0).get("title").toString());
                    textView_miaoshu_2.setText(listRecycler.get(0).get("content").toString());
                    new GlideLoader().displayImage(Housing_Detaile_Information2.this, listRecycler.get(0).get("headimgurl") + "", imageView_touxiang);
                    if (list_pp.size() == 0) {
                        getData();
                        if (point_list2.size() != 0) {


                            reInit();
                        }
                    }
                    getdata();

                    //ssnew GlideLoader().displayImage(Forum_Detailed_Information.this,listRecycler.get(0).get("picPath")+"",imageView_tupian);
                } else {
                    Toast.makeText(Housing_Detaile_Information2.this, "获取WebService数据错误", Toast.LENGTH_SHORT).show();
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
            Log.e("json", "11: " + array);
            for (int i = 0; i < array.length(); i++) {
                Map<String, Object> map = new HashMap<>();
                JSONObject jo = array.getJSONObject(i);
                String title = jo.getString("title");
                String contact = jo.getString("contact");
                String content = jo.getString("content");
                String userName = jo.getString("userName");
                String replyNum = jo.getString("replyNum");
                String headimgurl = jo.getString("headimgurl");
                String createTime = jo.getString("createTime");
                String contactPhone = jo.getString("contactPhone");
                String infoType = jo.getString("infoType");
                JSONArray array_xinxi = new JSONArray();
                JSONArray picPath1 = new JSONArray();
                try {
                    array_xinxi = jo.getJSONArray("replySet");
                    picPath1 = jo.getJSONArray("picPathSet");
                    Log.e("json", "33: " + picPathSet);
                } catch (Exception e) {
                    Toast.makeText(Housing_Detaile_Information2.this, "没有图片", Toast.LENGTH_SHORT).show();
                }
                for (int j = 0; j < array_xinxi.length(); j++) {
                    Map<String, Object> map_iv = new HashMap<>();
                    JSONObject jo1 = array_xinxi.getJSONObject(j);
                    content1 = jo1.getString("content");
                    userId1 = jo1.getString("replyUserName");
                    replyUserHeadimgurl1 = jo1.getString("replyUserHeadimgurl");

                    replyTime1 = jo1.getString("replyTime");

                    replyUserName1 = jo1.getString("replyUser");
                    map_iv.put("content1", content1);
                    map_iv.put("userId1", userId1);
                    map_iv.put("replyUserHeadimgurl1", replyUserHeadimgurl1);
                    map_iv.put("travelId1", travelId1);
                    map_iv.put("replyTime1", replyTime1);
                    map_iv.put("isAnonymous1", isAnonymous1);
                    map_iv.put("replyUserName1", userId1);
                    list_iv.add(map_iv);
                }

                for (int j = 0; j < picPath1.length(); j++) {
                    Map<String, Object> map_iv = new HashMap<>();
                    JSONObject jo2 = picPath1.getJSONObject(j);
                    picPath = jo2.getString("picPath");
                    map_iv.put("picPath", picPath);
                    point_list2.add(map_iv);
                }
                //map.put("title", title);
                map.put("contact", contact);
                map.put("content", content);
                map.put("userName", userName);
                map.put("headimgurl", headimgurl);
                map.put("createTime", createTime);
                map.put("title", title);
                map.put("contactPhone", contactPhone);
                map.put("replyNum", replyNum);
                map.put("infoType", infoType);


                //Toast.makeText(Carpooling_Detaile_Information.this, title, Toast.LENGTH_SHORT).show();
                listGridTop.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listGridTop;
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
        list_pp.clear();
        for (int i = 0; i < point_list.size(); i++) {
            linearLayout.removeView(point_list.get(i));
        }
        point_list.clear();
    }

    private void reInit() {
        viewPager.setCurrentItem(currentItem);
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

    private void getData() {
        if (point_list2.size() == 0) {
            ImageView image = new ImageView(this);
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            image.setImageResource(R.drawable.poster1);
            list_pp.add(image);
            return;
        }
        for (int i = 0; i < point_list2.size(); i++) {
            ImageView image = new ImageView(this);
            //           image.setScaleType(ImageView.ScaleType.FIT_XY);
            try{new GlideLoader().displayImage(Housing_Detaile_Information2.this,point_list2.get(i).get("picPath")+"",image);}catch (Exception e){};

//            image.setImageResource(images[i]);
            list_pp.add(image);
            ImageView image_point = new ImageView(this);
            image_point.setScaleType(ImageView.ScaleType.CENTER_CROP);
            image_point.setImageResource(R.mipmap.image_indicator);
            linearLayout.addView(image_point);
            point_list.add(image_point);
        }
    }

}
