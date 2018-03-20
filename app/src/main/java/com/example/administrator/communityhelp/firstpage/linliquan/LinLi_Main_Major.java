package com.example.administrator.communityhelp.firstpage.linliquan;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.communityhelp.BaseActivity;
import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.firstpage.linliquan.carpooling.Carpooling_Main;
import com.example.administrator.communityhelp.firstpage.linliquan.forum.Forum_Main;
import com.example.administrator.communityhelp.firstpage.linliquan.housing_rental.Housing_Rental;
import com.example.administrator.communityhelp.firstpage.linliquan.look_for_sth.Look_For_Sth_Main;
import com.example.administrator.communityhelp.firstpage.linliquan.recruit.Recruit_Main;
import com.example.administrator.communityhelp.firstpage.linliquan.second_hand.Second_Hand_Main;
import com.example.administrator.communityhelp.firstpage.linliquan.tourism.Tourism_Main;
import com.example.administrator.communityhelp.my_other.GlideLoader;
import com.example.administrator.communityhelp.myadapter.Carpooling_Detaile_Information_Adapter;
import com.example.administrator.communityhelp.myadapter.Firstpageviewpager;
import com.example.administrator.communityhelp.myadapter.LiLinQuanGridAdapter;
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

/**
 * Created by Administrator on 2016/12/30.
 */
public class LinLi_Main_Major extends BaseActivity implements AdapterView.OnItemClickListener,View.OnClickListener {
    private LiLinQuanGridAdapter liLinQuanGridAdapter;
    private List<HashMap<String, Object>> list;
    private GridView lilinGridView;
    private String[] iconName = {"社区论坛", "拼车信息", "二手市场", "自助旅游", "求职招聘", "寻物招领", "房屋租售"};
    private int[] icon = {R.drawable.shequluntan, R.drawable.pinchexinxi, R.drawable.ershoushichang, R.drawable.ziyoulvxing, R.drawable.qiuzhizhaopin, R.drawable.shiwuzhaoling, R.drawable.fangwuchuzu};
    private TextView back;
    Intent intent,intent1,intent2,intent3,intent4,intent5,intent6;
    ProgressDialogUtils progress;
    //以下为借口部分数据
    private List<Map<String, Object>> listGridTop, listGridCenter;
    String ures;
    public static final String NameSpace = "http://info.service.zhidisoft.com";  //命名空间
    public static final String MethodNameTop = "columnChildList";//方法名
    public static final String URL = "http://120.27.5.22:8080/services/bmInfoService";//地址
    public int mun = 10;
    Intent intent_id;
    String Id,content1,userId1,replyUserHeadimgurl1,travelId1,replyTime1,isAnonymous1,replyUserName1,picPathSet,picPath;
    TextView textView_name,textView_time,textView_qidian,textView_con,textView_zhongdian,textView_chufa,textView_lianxiren
            ,textView_dianhua,textView_miaoshu,textView_miaoshu_2;
    private List<Map<String, Object>> listRecycler;
    private List<Map<String, Object>> list_iv=new ArrayList();
    private ViewPager viewPager;
    private LinearLayout linearLayout;
    private int currentItem;
    private List<ImageView> point_list = new ArrayList<>();
    private Firstpageviewpager adapter1;
    private List<Map<String, Object>> point_list2=new ArrayList();
    private List<ImageView> list_pp = new ArrayList<>();
    private Map<String,Object> mapCode;
    private String communityCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.linli_main_major);
        back= (TextView) findViewById(R.id.textview_back_linli);
        back.setOnClickListener(this);
        lilinGridView = (GridView) findViewById(R.id.linli_gridview);
        lilinGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        list = new ArrayList<HashMap<String, Object>>();
        GetRequestMessageTop();

    }

    public List<HashMap<String, Object>> getData() {
        //cion和iconName的长度是相同的，这里任选其一都可以
        for (int i = 0; i < listRecycler.size(); i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("image", listGridTop.get(i).get("bmColumnImgPath"));
            map.put("text", listGridTop.get(i).get("name"));
            list.add(map);
        }

        liLinQuanGridAdapter = new LiLinQuanGridAdapter(list, this);
        lilinGridView.setAdapter(liLinQuanGridAdapter);
        lilinGridView.setOnItemClickListener(this);
        return list;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                intent=new Intent(this, Forum_Main.class);
                startActivity(intent);
                break;
            case 1:
                intent1=new Intent(this, Carpooling_Main.class);
                startActivity(intent1);
                break;
            case 2:
                intent2=new Intent(this, Second_Hand_Main.class);
                startActivity(intent2);
                break;
            case 3:
                intent3=new Intent(this, Tourism_Main.class);
                startActivity(intent3);
                break;
            case 4:
                intent4=new Intent(this, Recruit_Main.class);
                startActivity(intent4);
                break;
            case 5:
                intent5=new Intent(this, Look_For_Sth_Main.class);
                startActivity(intent5);
                break;
            case 6:
                intent6=new Intent(this, Housing_Rental.class);
                startActivity(intent6);
                break;
        }
    }
    //back事件
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textview_back_linli:
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
        communityCode=getUserData().getCommunityCode()+"";
        mapCode=getCommunityCode();
        if(communityCode.equals("null")){
            communityCode=mapCode.get("communityCode")+"";
        }
        map.put("communityCode",communityCode );
        map.put("columnCode","hd");

        Log.e("json", "id: " + Id);

        WebServiceUtils.callWebService(URL, MethodNameTop, NameSpace, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(SoapObject result) {
//                Log.e("json", "json: " + result);
                progress.dismiss();
                if (result != null) {
                    //获取json数据
                    String json = result.getProperty(0).toString();
                    //解析json数据并放入list集合中
//                    Log.e("json", "json: " + json);
                    listRecycler = jsonPaseTop(json);
                    getData();
                    //new GlideLoader().displayImage(LinLi_Main_Major.this,listRecycler.get(0).get("headimgurl")+"",imageView_touxiang);


                    //ssnew GlideLoader().displayImage(Forum_Detailed_Information.this,listRecycler.get(0).get("picPath")+"",imageView_tupian);
                } else {
                    Toast.makeText(LinLi_Main_Major.this, "获取WebService数据错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //获取当前选择的社区
    public Map<String,Object> getCommunityCode() {
        mapCode=new HashMap<>();
        String select = "select * from community";
        Cursor cursor = dbReader.rawQuery(select, null);
        while (cursor.moveToNext()) {
            String communityCode = cursor.getString(cursor.getColumnIndex("communityCode"));
            mapCode.put("communityCode",communityCode);
        }
        return mapCode;
    }

    //解析json数据
    public List<Map<String, Object>> jsonPaseTop(String result) {
        listGridTop = new ArrayList();
        try {
            String jjh="["+result+"]";
            JSONArray array = new JSONArray(jjh);
//            JSONObject object =
//            JSONArray array = object.getJSONArray("rows");new JSONObject(result);
//            Log.e("json", "11: " + array);
            for (int i = 0; i < array.length(); i++) {
                Map<String, Object> map = new HashMap<>();
                JSONObject jo = array.getJSONObject(i);
                //String title = jo.getString("title");
                String value = jo.getString("value");

                    JSONArray array1 = new JSONArray(value);
                    for (int j = 0; j < array1.length(); j++) {
                        Map<String, Object> map1 = new HashMap<>();
                        JSONObject jo1 = array1.getJSONObject(j);
                        String bmColumnImgPath = jo1.getString("bmColumnImgPath");
                        String name = jo1.getString("name");

                        map1.put("name", name);
                        map1.put("bmColumnImgPath", bmColumnImgPath);
                        listGridTop.add(map1);
                    }





                //Toast.makeText(Carpooling_Detaile_Information.this, title, Toast.LENGTH_SHORT).show();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listGridTop;
    }

}
