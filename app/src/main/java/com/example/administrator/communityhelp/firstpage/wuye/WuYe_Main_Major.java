package com.example.administrator.communityhelp.firstpage.wuye;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.communityhelp.BaseActivity;
import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.firstpage.bianmin.Travel_Page;
import com.example.administrator.communityhelp.firstpage.bianmin.activity.Activity_Main_Major;
import com.example.administrator.communityhelp.firstpage.bianmin.guide.Guide_Main_MajorActivity;
import com.example.administrator.communityhelp.firstpage.bianmin.periphery.ZhouBianYouHui_Main_MajorActivity;
import com.example.administrator.communityhelp.firstpage.bianmin.telephone.Telephone_Main_MajorActivity;
import com.example.administrator.communityhelp.firstpage.wuye.notice.Notice_Mian_MajorActivity;
import com.example.administrator.communityhelp.firstpage.wuye.pay.PaymentHall;
import com.example.administrator.communityhelp.firstpage.wuye.problem.PeoblemLeaveMessage;
import com.example.administrator.communityhelp.myadapter.BianMingListViewAdapter;
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
public class WuYe_Main_Major extends BaseActivity implements View.OnClickListener {
    //各种布局
    private TextView back;
    Intent intent, intent1, intent2;
    private RelativeLayout shequhuodong, banshizhinan, bianmingdianhua, chuxingchaxun, zhoubianyouhui;
    private ListView listView_bianming;
    private Map<String, Object> jsonMap, jsonMapChild;
    private List<Map<String, Object>> list, listChild;
    private BianMingListViewAdapter adapter;
    public static final String NameSpace = "http://info.service.zhidisoft.com";  //命名空间
    public static final String MethodName = "columnList ";//方法名
    public static final String MethodNameChild = "columnChildList  ";//方法名
    public static final String URL = "http://120.27.5.22:8080/services/bmInfoService";//地址
    private Map<String, Object> mapCode;
    private String communityCode;
    private ProgressDialogUtils progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wuye_main_major);
        initView();
        listChild = new ArrayList<>();
        GetMessage();
        listView_bianming.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String columnCode = listChild.get(position).get("code").toString();
                String columnId = listChild.get(position).get("id").toString();
                if(columnCode.equals("tzgg")){
                    Intent intent = new Intent(WuYe_Main_Major.this, Notice_Mian_MajorActivity.class);
                    intent.putExtra("columnCode",columnCode);
                    startActivity(intent);
                }else if(columnCode.equals("wyly")){
                    Intent intent = new Intent(WuYe_Main_Major.this, PeoblemLeaveMessage.class);
                    intent.putExtra("columnId",columnId);
                    startActivity(intent);
                }else if(columnCode.equals("jf")){
                    Intent intent = new Intent(WuYe_Main_Major.this, PaymentHall.class);
                    intent.putExtra("columnCode",columnCode);
                    startActivity(intent);
                }
            }
        });
    }

    //初始化
    private void initView() {
        //控件
        findViewById(R.id.textview_back_bianmin).setOnClickListener(this);
        listView_bianming = (ListView) findViewById(R.id.listView_bianming);

    }

    private void GetMessage() {
        //访问时弹出dialog对话框
        progress=new ProgressDialogUtils(this);
        progress.show();
        //访问webservice服务需要的参数
        HashMap<String, Object> map = new HashMap<>();
        communityCode = getUserData().getCommunityCode() + "";
        mapCode = getCommunityCode();
        if (communityCode.equals("null")) {
            communityCode = mapCode.get("communityCode") + "";
        }
        map.put("communityCode", communityCode);
        WebServiceUtils.callWebService(URL, MethodName, NameSpace, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(SoapObject result) {
                progress.dismiss();
                if (result != null) {
                    //获取json数据
                    String json = result.getProperty(0).toString();
                    //解析json数据并放入list集合中
                    list = jsonPase(json);
                    for (int i = 0; i < list.size(); i++) {
                        Log.e("jsonMap", list.get(i).get("code").toString());
                        if (list.get(i).get("code").toString().equals("xx")) {
                            GetMessageChild();
                        }
                    }
                } else {
                    Toast toast = Toast.makeText(WuYe_Main_Major.this, "", Toast.LENGTH_SHORT);
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

    private void GetMessageChild() {
        //访问webservice服务需要的参数
        HashMap<String, Object> map = new HashMap<>();
        communityCode = getUserData().getCommunityCode() + "";
        mapCode = getCommunityCode();
        if (communityCode.equals("null")) {
            communityCode = mapCode.get("communityCode") + "";
        }
        map.put("communityCode", communityCode);
        map.put("columnCode", "wy");
        WebServiceUtils.callWebService(URL, MethodNameChild, NameSpace, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(SoapObject result) {
                if (result != null) {
                    //获取json数据
                    String json = result.getProperty(0).toString();
                    ProgressDialogUtils.dismissProgressDialog();
                    //解析json数据并放入list集合中
                    try {
                        JSONArray array = new JSONArray("[" + json + "]");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jo = array.getJSONObject(i);
                            String results = jo.getString("result");
                            String msg = jo.getString("msg");
                            String value = jo.getString("value");
                            if (results.equals("true")) {
                                JSONArray arr = new JSONArray(value);
                                for (int j = 0; j < arr.length(); j++) {
                                    jsonMapChild = new HashMap<>();
                                    JSONObject js = arr.getJSONObject(j);
                                    String id = js.getString("id");
                                    String publishShow = js.getString("publishShow");
                                    String desc = js.getString("desc");
                                    String name = js.getString("name");
                                    String childShow = js.getString("childShow");
                                    String code = js.getString("code");
                                    String type = js.getString("type");
                                    String bmColumnImgPath = js.getString("bmColumnImgPath");
                                    jsonMapChild.put("id", id);
                                    jsonMapChild.put("publishShow", publishShow);
                                    jsonMapChild.put("desc", desc);
                                    jsonMapChild.put("name", name);
                                    jsonMapChild.put("childShow", childShow);
                                    jsonMapChild.put("code", code);
                                    jsonMapChild.put("type", type);
                                    jsonMapChild.put("bmColumnImgPath", bmColumnImgPath);
                                    listChild.add(jsonMapChild);
                                }
                                adapter = new BianMingListViewAdapter(listChild, WuYe_Main_Major.this);
                                listView_bianming.setAdapter(adapter);
                            } else {
                                Toast toast = Toast.makeText(WuYe_Main_Major.this, "", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                View view = getLayoutInflater().inflate(R.layout.toastview, null);
                                TextView toastText = (TextView) view.findViewById(R.id.toast_text);
                                toastText.setText(msg);
                                toast.setView(view);
                                toast.show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast toast = Toast.makeText(WuYe_Main_Major.this, "", Toast.LENGTH_SHORT);
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
    public List<Map<String, Object>> jsonPase(String result) {
        list = new ArrayList();
        try {
            JSONArray array = new JSONArray(result);
            for (int i = 0; i < array.length(); i++) {
                jsonMap = new HashMap<>();
                JSONObject jo = array.getJSONObject(i);
                String id = jo.getString("id");
                String name = jo.getString("name");
                String code = jo.getString("code");
                String type = jo.getString("type");
                String bmColumnImgPath = jo.getString("bmColumnImgPath");
                String children = jo.getString("children");
                String childNum = jo.getString("childNum");
                jsonMap.put("id", id);
                jsonMap.put("name", name);
                jsonMap.put("code", code);
                jsonMap.put("type", type);
                jsonMap.put("bmColumnImgPath", bmColumnImgPath);
                jsonMap.put("children", children);
                jsonMap.put("childNum", childNum);
                list.add(jsonMap);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    //获取当前选择的社区
    public Map<String, Object> getCommunityCode() {
        mapCode = new HashMap<>();
        String select = "select * from community";
        Cursor cursor = dbReader.rawQuery(select, null);
        while (cursor.moveToNext()) {
            String communityCode = cursor.getString(cursor.getColumnIndex("communityCode"));
            mapCode.put("communityCode", communityCode);
        }
        return mapCode;
    }

    //点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textview_back_bianmin:
                finish();
                break;
        }
    }

}
