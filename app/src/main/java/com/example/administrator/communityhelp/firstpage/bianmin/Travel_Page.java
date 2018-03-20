package com.example.administrator.communityhelp.firstpage.bianmin;

import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.communityhelp.BaseActivity;
import com.example.administrator.communityhelp.R;

import com.example.administrator.communityhelp.myadapter.BianMingListViewAdapter;
import com.example.administrator.communityhelp.myadapter.TravelPageGridAdapter;
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
public class Travel_Page extends BaseActivity{
    private TextView back;
    private List<HashMap<String, Object>> list;
    private GridView travelPageGridView;
    private TravelPageGridAdapter travelPageGridAdapter;

    private Map<String, Object> jsonMapChild;
    private List<Map<String, Object>> listChild;
    public static final String NameSpace = "http://info.service.zhidisoft.com";  //命名空间
    public static final String MethodNameChild = "columnChildList  ";//方法名
    public static final String URL = "http://120.27.5.22:8080/services/bmInfoService";//地址
    private Map<String, Object> mapCode;
    private String communityCode;
    private WebView webView;
    private ProgressDialogUtils progress;

    @Override
    public void init() {
        super.init();
        setContentView(R.layout.travel_page);
        back = (TextView) findViewById(R.id.textview_back_travel_page);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        travelPageGridView = (GridView) findViewById(R.id.travel_page_gridview);
        listChild=new ArrayList<>();
        GetMessageChild();
        travelPageGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(Travel_Page.this,WebViewActivity.class);
                intent.putExtra("url",listChild.get(position).get("url").toString());
                intent.putExtra("name",listChild.get(position).get("name").toString());
                startActivity(intent);
            }
        });
    }

    private void GetMessageChild() {
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
        map.put("columnCode", this.getIntent().getStringExtra("columnCode"));
        WebServiceUtils.callWebService(URL, MethodNameChild, NameSpace, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(SoapObject result) {
                progress.dismiss();
                if (result != null) {
                    //获取json数据
                    String json = result.getProperty(0).toString();
                    //解析json数据并放入list集合中
                    Log.e("json2", json);
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
                                    String url = js.getString("url");
                                    String bmColumnImgPath = js.getString("bmColumnImgPath");
                                    jsonMapChild.put("id", id);
                                    jsonMapChild.put("publishShow", publishShow);
                                    jsonMapChild.put("desc", desc);
                                    jsonMapChild.put("name", name);
                                    jsonMapChild.put("childShow", childShow);
                                    jsonMapChild.put("code", code);
                                    jsonMapChild.put("type", type);
                                    jsonMapChild.put("url", url);
                                    jsonMapChild.put("bmColumnImgPath", bmColumnImgPath);
                                    listChild.add(jsonMapChild);
                                }
                                travelPageGridAdapter = new TravelPageGridAdapter(listChild,Travel_Page.this);
                                travelPageGridView.setAdapter(travelPageGridAdapter);
                            } else {
                                Toast toast = Toast.makeText(Travel_Page.this, "", Toast.LENGTH_SHORT);
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
                    Toast toast = Toast.makeText(Travel_Page.this, "", Toast.LENGTH_SHORT);
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


}
