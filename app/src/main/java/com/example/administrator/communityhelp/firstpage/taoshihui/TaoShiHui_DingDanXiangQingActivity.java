package com.example.administrator.communityhelp.firstpage.taoshihui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.communityhelp.BaseActivity;
import com.example.administrator.communityhelp.Login_Main;
import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.myadapter.TaoShiHuiRecycleViewAdapter;
import com.example.administrator.communityhelp.webserviceutils.ProgressDialogUtils;
import com.example.administrator.communityhelp.webserviceutils.WebServiceUtils;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaoShiHui_DingDanXiangQingActivity extends BaseActivity implements View.OnClickListener{
    private TextView dingdan_name,dingdan_price,dingdan_totalPrice,textView_goodsNum;
    private EditText editText_buyNum;
    private double price,totalPrice;
    private Button btn_minus;
    private int num=1;

    public static final String NameSpace = "http://goods.service.zhidisoft.com";  //命名空间
    public static final String MethodNameRecycler = "goodsOrderSave";//方法名
    public static final String URLRecycler = "http://120.27.5.22:8080/services/goodsOrderService";//地址

    private Map<String, Object> jsonMap;
    private ProgressDialogUtils progress;

    @Override
    public void init() {
        super.init();
        setContentView(R.layout.activity_tao_shi_hui__ding_dan_xiang_qing);
        initView();
        jsonMap = new HashMap<>();
        dingdan_name.setText(this.getIntent().getStringExtra("name"));
        dingdan_price.setText(this.getIntent().getStringExtra("price"));
        textView_goodsNum.setText(this.getIntent().getStringExtra("goodsNum"));
        dingdan_totalPrice.setText(dingdan_price.getText());
        setPrice();
    }

    private void initView() {
        findViewById(R.id.TaoShiHui_backSPXQ).setOnClickListener(this);
        findViewById(R.id.btn_minus).setOnClickListener(this);
        findViewById(R.id.btn_add).setOnClickListener(this);
        findViewById(R.id.btn_submitOrder).setOnClickListener(this);
        btn_minus= (Button) findViewById(R.id.btn_minus);
        dingdan_name= (TextView) findViewById(R.id.dingdan_name);
        dingdan_price= (TextView) findViewById(R.id.dingdan_price);
        dingdan_totalPrice= (TextView) findViewById(R.id.dingdan_totalPrice);
        textView_goodsNum= (TextView) findViewById(R.id.textView_goodsNum);
        editText_buyNum= (EditText) findViewById(R.id.editText_buyNum);
    }

    @Override
    public void onClick(View v) {
        if(!editText_buyNum.getText().toString().equals("")){
            num=Integer.parseInt(editText_buyNum.getText().toString());
        }else{
            num=0;
        }
        switch (v.getId()){
            case R.id.TaoShiHui_backSPXQ:
                finish();
                break;
            case R.id.btn_minus:
                num--;
                if(num<=1){
                    num=1;
                    btn_minus.setBackgroundResource(R.mipmap.sq_sl_minus2);
                }
                setPrice();
                break;
            case R.id.btn_add:
                num++;
                if(num>1){
                    btn_minus.setBackgroundResource(R.mipmap.sq_sl_minus);
                }
                setPrice();
                break;
            case  R.id.btn_submitOrder:
                if((getUserData().getUserId()+"").equals("null")){
                    Intent intent=new Intent(this, Login_Main.class);
                    startActivity(intent);
                    finish();
                }else{
                    GetRequestMessageRecycler();
                }
                break;
        }
    }

    private void setPrice() {
        price=Double.parseDouble(dingdan_price.getText().toString().substring(0,dingdan_price.getText().toString().indexOf("元")));
        editText_buyNum.setText(num+"");
        //设置editText光标一直在文本最后
        editText_buyNum.setSelection(editText_buyNum.length());
        editText_buyNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!editText_buyNum.getText().toString().equals("")){
                    num=Integer.parseInt(editText_buyNum.getText().toString());
                    if(num<=1){
                        btn_minus.setBackgroundResource(R.mipmap.sq_sl_minus2);
                    }else{
                        btn_minus.setBackgroundResource(R.mipmap.sq_sl_minus);
                    }
                }else{
                    num=0;
                }
                totalPrice=price*num;
                DecimalFormat decimalFormat = new DecimalFormat("###################.###########");
                dingdan_totalPrice.setText(decimalFormat.format(totalPrice)+"元");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void GetRequestMessageRecycler() {
        //访问时弹出dialog对话框
        progress=new ProgressDialogUtils(this);
        progress.show();
        //访问webservice服务需要的参数
        HashMap<String, Object> map = new HashMap<>();
        map.put("totalNum", editText_buyNum.getText().toString());
        map.put("total", dingdan_totalPrice.getText().toString());
        map.put("merchantsGoodsId", this.getIntent().getStringExtra("merchantsGoodsId"));
        map.put("userId", getUserData().getUserId());
        WebServiceUtils.callWebService(URLRecycler, MethodNameRecycler, NameSpace, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(SoapObject result) {
                progress.dismiss();
                if (result != null) {
                    //获取json数据
                    String json = result.getProperty(0).toString();
                    //解析json数据并放入list集合中
                    //Log.e("json", "json: "+json);
                    jsonMap = jsonPaseRecycler("["+json+"]");
                    if(jsonMap.get("result").toString().equals("true")){
                        Intent intent=new Intent(TaoShiHui_DingDanXiangQingActivity.this,TaoShiHui_SurePayActivity.class);
                        intent.putExtra("id",TaoShiHui_DingDanXiangQingActivity.this.getIntent().getStringExtra("merchantsGoodsId"));
                        intent.putExtra("name",dingdan_name.getText().toString());
                        intent.putExtra("price",dingdan_price.getText().toString());
                        intent.putExtra("totalPrice",dingdan_totalPrice.getText().toString());
                        intent.putExtra("buyNum",editText_buyNum.getText().toString());
                        intent.putExtra("orderId",jsonMap.get("orderId").toString());
                        Log.e("orderId", jsonMap.get("orderId").toString());
                        startActivity(intent);
                        finish();
                    }else{
                        Toast toast = Toast.makeText(TaoShiHui_DingDanXiangQingActivity.this, "", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        View view = getLayoutInflater().inflate(R.layout.toastview, null);
                        TextView toastText = (TextView) view.findViewById(R.id.toast_text);
                        toastText.setText(jsonMap.get("msg").toString());
                        toast.setView(view);
                        toast.show();
                    }
                } else {
                    Toast toast = Toast.makeText(TaoShiHui_DingDanXiangQingActivity.this, "", Toast.LENGTH_SHORT);
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
    public Map<String, Object> jsonPaseRecycler(String json) {
        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                JSONObject jo = array.getJSONObject(i);
                String result = jo.getString("result");
                String msg = jo.getString("msg");
                String orderId = jo.getString("orderId");
                jsonMap.put("result", result);
                jsonMap.put("msg", msg);
                jsonMap.put("orderId", orderId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonMap;
    }

}
