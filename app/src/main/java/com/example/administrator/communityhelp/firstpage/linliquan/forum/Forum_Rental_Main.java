package com.example.administrator.communityhelp.firstpage.linliquan.forum;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.communityhelp.BaseActivity;
import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.my_other.GlideLoader;
import com.example.administrator.communityhelp.myadapter.Firstpageviewpager;
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

public class Forum_Rental_Main extends BaseActivity {

    ProgressDialogUtils progress;
    TextView textView_tit,textView_con;
    String tit,con,comIds,userId;
    //以下为借口部分数据
    private List<Map<String, Object>> listGridTop, listGridCenter;
    String ures;
    String msaage;
    public static final String NameSpace = "http://info.service.zhidisoft.com";  //命名空间
    public static final String MethodNameTop = "esInfoList";//方法名
    public static final String URL = "http://120.27.5.22:8080/services/esInfoService";//地址
    public int mun = 10;
    private List<Map<String, Object>> listRecycler;
    public void Forum_details_main_ll_1(View v) {
        finish();
    }
    public void Forum_details_main_ll_2(View v) {
        tit=textView_tit.getText().toString();
        con=textView_con.getText().toString();
        GetRequestMessageTop();
    }

    @Override
    public void init() {
        super.init();
        setContentView(R.layout.activity_forum__rental__main);
        textView_tit= (TextView) findViewById(R.id.et_tourism);
        textView_con= (TextView) findViewById(R.id.et_tourism_3);
        userId=getUserData().getUserId();
        comIds=getUserData().getCommunityCode();
    }
    //以下为接口数据部分

    private void GetRequestMessageTop() {
        //访问时弹出dialog对话框
        progress=new ProgressDialogUtils(this);
        progress.show();
        //访问webservice服务需要的参数
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageNo", "-1");
        map.put("pageSize", 7);
        map.put("columnCode", "");
        map.put("communityCode", "");
        map.put("sellOrBuy", "sell");
        map.put("condition", "");
        map.put("curTime", System.currentTimeMillis() + "");
        Log.e("json", "id: " + "");

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
                    msaage=listRecycler.get(0).get("msg").toString();
                    Toast.makeText(Forum_Rental_Main.this, msaage, Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(Forum_Rental_Main.this, "获取WebService数据错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //解析json数据
    public List<Map<String, Object>> jsonPaseTop(String result) {
        listGridTop = new ArrayList();
        try {
            String jjh="["+result+"]";
            JSONArray array = new JSONArray(jjh);
//            JSONObject object =
//            JSONArray array = object.getJSONArray("rows");new JSONObject(result);
            Log.e("json", "json: " + array);
            for (int i = 0; i < array.length(); i++) {
                Map<String, Object> map = new HashMap<>();
                JSONObject jo = array.getJSONObject(i);
                String title = jo.getString("title");
                String msg = jo.getString("msg");
                map.put("title", title);
                map.put("msg", msg);


                listGridTop.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listGridTop;
    }
}
