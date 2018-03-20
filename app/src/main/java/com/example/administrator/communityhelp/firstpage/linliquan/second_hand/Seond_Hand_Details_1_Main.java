package com.example.administrator.communityhelp.firstpage.linliquan.second_hand;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.communityhelp.BaseActivity;
import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.UpLoadUtils;
import com.example.administrator.communityhelp.webserviceutils.ProgressDialogUtils;
import com.example.administrator.communityhelp.webserviceutils.WebServiceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Seond_Hand_Details_1_Main extends BaseActivity implements View.OnClickListener{
    View view;
    AlertDialog dialog;
    static String URL = "http://120.27.5.22:8080/services/esInfoService";
    static String NameSpace = "http://info.service.zhidisoft.com";
    static String MethoedName = "esInfoSaveByBuy";
    //以下为借口部分数据
    private List<Map<String, Object>> listGridTop, listGridCenter;
    String id, title, content, contact, contactPhone, price, oldPrice, oldNew, exchange, type, comIds, tempFileIds, userId;
    private List<Map<String, Object>> listRecycler;
    EditText editText1, editText2, editText3, editText4, editText5;
    TextView textView, textViewKind;
    Intent intent;
    View popupView;
    PopupWindow popupWindow;
    TextView text;
    RelativeLayout relativeLayout1, RelativeLayout2, RelativeLayout3, RelativeLayout4, RelativeLayout5, RelativeLayout6, RelativeLayout7, RelativeLayout8, RelativeLayout9;
    LinearLayout linearLayout;
    ProgressDialogUtils progress;
    @Override
    public void init() {
        super.init();
        setContentView(R.layout.activity_seond__hand__details_1_main);
        popupView = Seond_Hand_Details_1_Main.this.getLayoutInflater().inflate(R.layout.ershoupopuwindow, null);
        text = (TextView) findViewById(R.id.tv_secondHandBuy);
        editText1 = (EditText) findViewById(R.id.et_tourism_secondHandBuy);
        editText2 = (EditText) findViewById(R.id.et_tourism1_secondHandBuy);
        editText3 = (EditText) findViewById(R.id.et_tourism2_secondHandBuy);
        editText4 = (EditText) findViewById(R.id.et_tourism_3_secondHandBuy);
        textView = (TextView) findViewById(R.id.first_tit_tourism_buy);
        editText5 = (EditText) findViewById(R.id.et_tourism4_secondHandBuy);
        textView.setText("二手求购发布");
        editText1.setHint("输入标题");
        editText2.setHint("输入联系人");
        editText3.setHint("输入联系电话");
        editText4.setHint("输入描述");
        editText5.setHint("输入求购价（选填）");
    }


    public void tourism_details_main_ll_1(View v) {
        finish();
    }

    public void second_hand_rl(View v) {
        backgroundAlpha(0.5f);
        popupWindow = new PopupWindow(popupView, 800, 1700, true);
        relativeLayout1 = (RelativeLayout) popupView.findViewById(R.id.second_popu_shuma);
        RelativeLayout2 = (RelativeLayout) popupView.findViewById(R.id.second_popu_yinxiang);
        RelativeLayout3 = (RelativeLayout) popupView.findViewById(R.id.second_popu_baojian);
        RelativeLayout4 = (RelativeLayout) popupView.findViewById(R.id.second_popu_jiaju);
        RelativeLayout5 = (RelativeLayout) popupView.findViewById(R.id.second_popu_muying);
        RelativeLayout6 = (RelativeLayout) popupView.findViewById(R.id.second_popu_fushi);
        RelativeLayout7 = (RelativeLayout) popupView.findViewById(R.id.second_popu_jiadian);
        RelativeLayout8 = (RelativeLayout) popupView.findViewById(R.id.second_popu_bangong);
        RelativeLayout9 = (RelativeLayout) popupView.findViewById(R.id.second_popu_qita);
        linearLayout = (LinearLayout) popupView.findViewById(R.id.second_popu_quxiao);

        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F8F8F8")));
        // TODO: 2016/5/17 设置可以获取焦点
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.AnimBottom);
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
        popupWindow.getContentView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_MENU && event.getRepeatCount() == 0
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                    return true;
                }
                return false;
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        relativeLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setText("数码");
                popupWindow.dismiss();
            }
        });
        RelativeLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setText("音像");
                popupWindow.dismiss();
            }
        });
        RelativeLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setText("美容保健");
                popupWindow.dismiss();
            }
        });
        RelativeLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setText("家居日用品");
                popupWindow.dismiss();
            }
        });
        RelativeLayout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setText("母婴用品");
                popupWindow.dismiss();
            }
        });
        RelativeLayout6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setText("服饰用品");
                popupWindow.dismiss();
            }
        });
        RelativeLayout7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setText("家具家电");
                popupWindow.dismiss();
            }
        });
        RelativeLayout8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setText("办公用品");
                popupWindow.dismiss();
            }
        });
        RelativeLayout9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setText("其他");
                popupWindow.dismiss();
            }
        });
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupWindow.dismiss();
            }
        });
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    public void fabu_secondhandBuy(View v) {
        getData();
        if(title.equals("")){
            Toast.makeText(this,"请输入标题",Toast.LENGTH_SHORT).show();
            return;
        }
        if(contact.equals("")||contactPhone.equals("")||content.equals("")){
            Toast.makeText(this,"请输入完整信息",Toast.LENGTH_SHORT).show();
            return;
        }
        SendMessageToSeviece();
    }

    private void SendMessageToSeviece() {
        //访问时弹出dialog对话框
        progress=new ProgressDialogUtils(this);
        progress.show();
        //访问webservice服务需要的参数
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", "");
        map.put("title", title);
        map.put("content", content);//297ebe0e593e1bf9015944c131201242
        map.put("contact", contact);
        map.put("contactPhone", contactPhone);
        map.put("price", price);

        map.put("oldNew", oldNew);

        map.put("type", type);
        map.put("comIds", comIds);
        map.put("tempFileIds", tempFileIds);
        map.put("userId", userId);
        //map.put("curTime", System.currentTimeMillis() + "");
        WebServiceUtils.callWebService(URL, MethoedName, NameSpace, map, new WebServiceUtils.WebServiceCallBack() {
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
                    Toast.makeText(Seond_Hand_Details_1_Main.this, "获取WebService数据错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //解析json数据
    public List<Map<String, Object>> jsonPaseTop(String result) {
        listGridTop = new ArrayList();
        try {
            String name = "[" + result + "]";
            JSONArray array = new JSONArray(name);
//            JSONObject object = new JSONObject(result);
//            JSONArray array = object.getJSONArray("rows");
            Log.e("json", "json111: " + result);
            for (int i = 0; i < array.length(); i++) {
                Map<String, Object> map = new HashMap<>();
                JSONObject jo = array.getJSONObject(i);
                String msg = jo.getString("msg");
                map.put("msg", msg);
                Toast.makeText(Seond_Hand_Details_1_Main.this, msg, Toast.LENGTH_SHORT).show();
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

    public void getData() {
        id = "";
        title = editText1.getText().toString();
        content = editText4.getText().toString();
        contact = editText2.getText().toString();
        contactPhone = editText3.getText().toString();
        price="";
        oldPrice="";
        oldNew="";
        exchange="";
        type = text.getText().toString();
        comIds = getUserData().getCommunityCode();
        tempFileIds = "";
        userId=getUserData().getUserId();
    }

}
