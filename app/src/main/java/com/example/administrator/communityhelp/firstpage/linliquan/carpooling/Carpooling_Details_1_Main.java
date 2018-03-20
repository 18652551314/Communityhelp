package com.example.administrator.communityhelp.firstpage.linliquan.carpooling;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.administrator.communityhelp.BaseActivity;
import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.webserviceutils.ProgressDialogUtils;
import com.example.administrator.communityhelp.webserviceutils.WebServiceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Carpooling_Details_1_Main extends BaseActivity implements View.OnClickListener{
    View view;
    AlertDialog dialog;
    TextView textView,editText_shijian;
    DatePickerDialog datePickerDialog;
    ProgressDialogUtils progress;
    Intent intent_uesr;
    String time,start,end,infoType,carInfo,startTime,content,contact,contactPhone,comIds,tempFileIds,userId;
    //以下为借口部分数据
    private List<Map<String, Object>> listGridTop, listGridCenter;
    String ures;
    public static final String NameSpace = "http://info.service.zhidisoft.com";  //命名空间
    public static final String MethodNameTop = "carPoolingInfoSave";//方法名
    public static final String URL = "http://120.27.5.22:8080/services/carPoolingInfoService";//地址
    public int mun = 10;
    private List<Map<String, Object>> listRecycler;
    EditText editText_qidian,editText_zhongdian,editText_ren,editText_dianhua,editText_chexing,editText_mass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carpooling__details_1_main);
        textView = (TextView) findViewById(R.id.carpooling_details_tv_time);
        intent_uesr=getIntent();
        userId=intent_uesr.getStringExtra("user_id");
        comIds=intent_uesr.getStringExtra("CommunityCode");
        TextView textViewHead= (TextView) findViewById(R.id.first_tit_tourism);
        textViewHead.setText("找拼车");
        editText_qidian= (EditText) findViewById(R.id.car_qidian_2);
        editText_zhongdian= (EditText) findViewById(R.id.car_zhongdian_2);
        editText_shijian= (TextView) findViewById(R.id.carpooling_details_tv_time);
        editText_dianhua= (EditText) findViewById(R.id.car_dianhua_2);
        editText_ren= (EditText) findViewById(R.id.car_lianxiren_2);
        editText_mass= (EditText) findViewById(R.id.car_miaoshu_2);

        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickDlg();
                    return true;
                }
                return false;
            }

        });
        textView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickDlg();
                }
            }
        });
    }
    public void car_ll_2(View v) {
        start=editText_qidian.getText().toString();
        end=editText_zhongdian.getText().toString();
        //carInfo=editText_chexing.getText().toString();
        startTime=editText_shijian.getText().toString();
        content=editText_mass.getText().toString();
        contact=editText_ren.getText().toString();
        contactPhone=editText_dianhua.getText().toString();
        infoType="zcz";
        tempFileIds="";
        if (start.equals("") || end.equals("")  || startTime.equals("")
                || contactPhone.equals("")) {
            Toast.makeText(this,"请输入完整信息",Toast.LENGTH_SHORT).show();
            return;
        }
        GetRequestMessageTop();
//        finish();
    }
    public void carpooling_details_ll(View v) {
        finish();
    }

    protected void showDatePickDlg() {
        Calendar calendar = Calendar.getInstance();
        datePickerDialog= new DatePickerDialog(Carpooling_Details_1_Main.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                time=year + "-" + monthOfYear + "-" + dayOfMonth;
                Carpooling_Details_1_Main.this.textView.setText(year + "-" + monthOfYear+1 + "-" + dayOfMonth);
                showtimePickDlg();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    protected void showtimePickDlg() {
        Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(Carpooling_Details_1_Main.this,new TimePickerDialog.OnTimeSetListener() {

            @Override

            public void onTimeSet(TimePicker view,int hourOfDay,int minute)

            {
                Carpooling_Details_1_Main.this.textView.setText(time+" "+hourOfDay+":"+minute+"00");

            }

        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();

    }

    //以下为接口数据部分

    private void GetRequestMessageTop() {
        //访问时弹出dialog对话框
        progress=new ProgressDialogUtils(this);
        progress.show();
        //访问webservice服务需要的参数
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", "");
        map.put("start", start);
        map.put("end", end);//297ebe0e593e1bf9015944c131201242
        map.put("infoType", "zcz");
        map.put("carInfo", "");
        map.put("startTime", startTime);
        map.put("content", content);
        map.put("contact", contact);
        map.put("contactPhone", contactPhone);
        map.put("comIds", comIds);
        map.put("tempFileIds", tempFileIds);
        map.put("userId", userId);
        //map.put("curTime", System.currentTimeMillis() + "");

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
                    setDialogView();
                } else {
                    Toast.makeText(Carpooling_Details_1_Main.this, "获取WebService数据错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //解析json数据
    public List<Map<String, Object>> jsonPaseTop(String result) {
        listGridTop = new ArrayList();
        try {
            String name="["+result+"]";
            JSONArray array = new JSONArray(name);
//            JSONObject object = new JSONObject(result);
//            JSONArray array = object.getJSONArray("rows");
            Log.e("json", "json111: " + result);
            for (int i = 0; i < array.length(); i++) {
                Map<String, Object> map = new HashMap<>();
                JSONObject jo = array.getJSONObject(i);
                String msg = jo.getString("msg");

                map.put("msg", msg);


                Toast.makeText(Carpooling_Details_1_Main.this, msg, Toast.LENGTH_SHORT).show();
                listGridTop.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listGridTop;
    }

    //设置弹窗
    private void setDialogView() {
        view = getLayoutInflater().inflate(R.layout.dialog_view_black, null);
        view.findViewById(R.id.btn_cancel_dialog_black).setOnClickListener(this);
        view.getBackground().setAlpha(150);
        dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == event.KEYCODE_BACK) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        dialog.show();
    }
    //弹窗点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel_dialog_black:
                dialog.dismiss();
                finish();
                break;

        }
    }

}
