package com.example.administrator.communityhelp.firstpage.linliquan.forum;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.communityhelp.BaseActivity;
import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.myadapter.LunTan_One_ViewAdapter;
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
 * Created by Administrator on 2017/1/7.
 */
public class Forum_Reply extends BaseActivity implements View.OnClickListener {
    private Intent intent,intent_id;
    private TextView tv_back, tv_send;
    private String Id,user_id;
    EditText forum_huifu_et;
    ProgressDialogUtils progress;
    public static final String NameSpace = "http://bbs.service.zhidisoft.com";  //命名空间
    public static final String MethodNameTop = "bbsReplySave";//方法名
    public static final String URL = "http://120.27.5.22:8080/services/bbsService";//地址
    private List<Map<String, Object>> listGridTop, listGridCenter;
    private List<Map<String, Object>> listRecycler;
    private String massge;
    @Override
    public void init() {
        super.init();
        setContentView(R.layout.forum_reply);
        tv_back = (TextView) findViewById(R.id.textview_back_forum_reply);
        tv_send = (TextView) findViewById(R.id.tv_forum_reply_send);
        findViewById(R.id.textview_back_forum_reply).setOnClickListener(this);
        findViewById(R.id.tv_forum_reply_send).setOnClickListener(this);
        forum_huifu_et= (EditText) findViewById(R.id.forum_huifu_et);
        intent_id=getIntent();
        Id=intent_id.getStringExtra("id");
        user_id=getUserData().getUserId();

    }

    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.textview_back_forum_reply:
                   finish();
                    break;
                case R.id.tv_forum_reply_send:
                    massge=forum_huifu_et.getText().toString();
                    GetRequestMessageTop();
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
        map.put("content", massge);
        map.put("bbsTopicId", Id+"");
        map.put("userId", user_id);//297ebe0e593e1bf9015944c131201242


        WebServiceUtils.callWebService(URL, MethodNameTop, NameSpace, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(SoapObject result) {
                Log.e("json", "jsongogo: " + result);
                progress.dismiss();
                if (result != null) {
                    //获取json数据
                    String json = result.getProperty(0).toString();
                    //解析json数据并放入list集合中
                    Log.e("json", "json: " + json);
                    listRecycler = jsonPaseTop(json);

                } else {
                    Toast.makeText(Forum_Reply.this, "获取WebService数据错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //解析json数据
    public List<Map<String, Object>> jsonPaseTop(String result) {
        listGridTop = new ArrayList();
        try {
            String king="["+result+"]";
            JSONArray array = new JSONArray(king);
//            JSONObject object = new JSONObject(result);
//            JSONArray array = object.getJSONArray("rows");
            Log.e("json", "json111: " + result);
            for (int i = 0; i < array.length(); i++) {
                Map<String, Object> map = new HashMap<>();
                JSONObject jo = array.getJSONObject(i);
                String result1 = jo.getString("result");
                String msg = jo.getString("msg");


                map.put("result1", result1);
                map.put("msg", msg);

                Toast.makeText(Forum_Reply.this, msg, Toast.LENGTH_SHORT).show();
                listGridTop.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listGridTop;
    }
}
