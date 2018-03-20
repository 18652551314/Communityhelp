package com.example.administrator.communityhelp.firstpage.taoshihui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.example.administrator.communityhelp.BaseActivity;
import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.webserviceutils.ProgressDialogUtils;
import com.example.administrator.communityhelp.webserviceutils.WebServiceUtils;
import com.example.administrator.communityhelp.zhifubao.H5PayDemoActivity;
import com.example.administrator.communityhelp.zhifubao.PayResult;
import com.example.administrator.communityhelp.zhifubao.SignUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class TaoShiHui_SurePayActivity extends BaseActivity implements View.OnClickListener {
    private TextView textView_name, textView_price, textView_totalPrice, textView_back,
            textView_userName, textView_phone, textView_address,textView_dialog_message;
    private String name, price, totalPrice, buyNum;
    private LinearLayout layout_adress, layout_dnxf, layout_zt, layout_shsm, layout_wszf, layout_hdfk, layout_xxzf;
    private ImageView image_dnxf, image_zt, image_shsm, image_wszf, image_hdfk, image_xxzf;
    private View view;
    private AlertDialog dialog;
    private boolean sendMethod = false, payMethod = false;
    private Intent intent;
    private String city, row, address, username, phone ,json;
    private List<Map<String, Object>> listRecycler;
    private Map<String, Object> addressMap;
    private int satus, psfsSatus;

    public static final String NameSpace = "http://goods.service.zhidisoft.com";  //命名空间
    public static final String MethodNameRecycler = "goodsPsfs";//方法名
    public static final String URLRecycler = "http://120.27.5.22:8080/services/goodsService";//地址
    public static final String NameSpaceAddress = "http://member.service.zhidisoft.com";  //命名空间
    public static final String MethodNameAddress = "addressList";//方法名
    public static final String URLAddress = "http://120.27.5.22:8080/services/addressService";//地址
    public static final String MethodNamexxzf = "savePayWay";//方法名
    public static final String MethodNamehdfk = "savePayWay_payOnDelivery";//方法名
    public static final String MethodNameSavepsfs = "goodsAddressSave";//方法名
    public static final String URL = "http://120.27.5.22:8080/services/goodsOrderService";//地址
    private ProgressDialogUtils progress;

    @Override
    public void init() {
        super.init();
        setContentView(R.layout.activity_tao_shi_hui__sure_pay);
        initView();
        setSendStaus();
        setPayStaus();
        name = this.getIntent().getStringExtra("name").toString();
        price = this.getIntent().getStringExtra("price").toString().replaceAll("元", "");
        totalPrice = this.getIntent().getStringExtra("totalPrice").toString().replaceAll("元", "");
        buyNum = this.getIntent().getStringExtra("buyNum").toString();
        textView_name.setText(name + "*" + buyNum);
        textView_price.setText("￥" + price);
        textView_totalPrice.setText("￥" + totalPrice);
        textView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        listRecycler=new ArrayList<>();
        GetRequestMessageRecycler();
        GetAddressMessageRecycler();
    }

    private void setPayStaus() {
        image_wszf.setImageResource(R.mipmap.psfs_unselect);
        image_hdfk.setImageResource(R.mipmap.psfs_unselect);
        image_xxzf.setImageResource(R.mipmap.psfs_unselect);
        satus = 0;
    }

    private void setSendStaus() {
        image_dnxf.setImageResource(R.mipmap.psfs_unselect);
        image_zt.setImageResource(R.mipmap.psfs_unselect);
        image_shsm.setImageResource(R.mipmap.psfs_unselect);
    }


    private void setDialogView() {
        view = getLayoutInflater().inflate(R.layout.dialog_view, null);
        view.findViewById(R.id.btn_sure).setOnClickListener(this);
        view.findViewById(R.id.btn_cancel).setOnClickListener(this);
        view.getBackground().setAlpha(150);
        textView_dialog_message= (TextView) view.findViewById(R.id.textView_dialog_message);
        textView_dialog_message.setText("请输入您的配送地址？");
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

    private void initView() {
        textView_name = (TextView) findViewById(R.id.textView_name);
        textView_price = (TextView) findViewById(R.id.textView_price);
        textView_totalPrice = (TextView) findViewById(R.id.textView_totalPrice);
        textView_back = (TextView) findViewById(R.id.payActivity_back);
        textView_userName = (TextView) findViewById(R.id.text_name);
        textView_phone = (TextView) findViewById(R.id.text_phone);
        textView_address = (TextView) findViewById(R.id.text_address);
        layout_adress = (LinearLayout) findViewById(R.id.layout_adress);
        layout_dnxf = (LinearLayout) findViewById(R.id.layout_dnxf);
        layout_zt = (LinearLayout) findViewById(R.id.layout_zt);
        layout_shsm = (LinearLayout) findViewById(R.id.layout_shsm);
        layout_wszf = (LinearLayout) findViewById(R.id.layout_wszf);
        layout_hdfk = (LinearLayout) findViewById(R.id.layout_hdfk);
        layout_xxzf = (LinearLayout) findViewById(R.id.layout_xxzf);
        findViewById(R.id.btn_submitOrder).setOnClickListener(this);
        findViewById(R.id.layout_dnxf).setOnClickListener(this);
        findViewById(R.id.layout_zt).setOnClickListener(this);
        findViewById(R.id.layout_shsm).setOnClickListener(this);
        findViewById(R.id.layout_wszf).setOnClickListener(this);
        findViewById(R.id.layout_hdfk).setOnClickListener(this);
        findViewById(R.id.layout_xxzf).setOnClickListener(this);
        layout_adress.setOnClickListener(this);
        image_dnxf = (ImageView) findViewById(R.id.image_dnxf);
        image_zt = (ImageView) findViewById(R.id.image_zt);
        image_shsm = (ImageView) findViewById(R.id.image_shsm);
        image_wszf = (ImageView) findViewById(R.id.image_wszf);
        image_hdfk = (ImageView) findViewById(R.id.image_hdfk);
        image_xxzf = (ImageView) findViewById(R.id.image_xxzf);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sure:
                layout_adress.setVisibility(View.VISIBLE);
                setSendStaus();
                intent = new Intent(this, AddShouHuoDiZhiActivity.class);
                startActivityForResult(intent, 100);
                dialog.dismiss();
                break;
            case R.id.btn_cancel:
                layout_adress.setVisibility(View.GONE);
                setSendStaus();
                sendMethod = false;
                dialog.dismiss();
                break;
            case R.id.layout_dnxf:
                setSendStaus();
                image_dnxf.setImageResource(R.mipmap.psfs_select);
                sendMethod = true;
                psfsSatus = 1;
                layout_adress.setVisibility(View.GONE);
                break;
            case R.id.layout_zt:
                setSendStaus();
                psfsSatus = 2;
                image_zt.setImageResource(R.mipmap.psfs_select);
                sendMethod = true;
                layout_adress.setVisibility(View.GONE);
                break;
            case R.id.layout_shsm:
                GetAddressMessageRecycler();
                setSendStaus();
                psfsSatus = 3;
                image_shsm.setImageResource(R.mipmap.psfs_select);
                sendMethod = true;
                break;
            case R.id.layout_wszf:
                setPayStaus();
                image_wszf.setImageResource(R.mipmap.psfs_select);
                payMethod = true;
                satus = 3;
                break;
            case R.id.layout_hdfk:
                setPayStaus();
                image_hdfk.setImageResource(R.mipmap.psfs_select);
                payMethod = true;
                satus = 1;
                break;
            case R.id.layout_xxzf:
                setPayStaus();
                image_xxzf.setImageResource(R.mipmap.psfs_select);
                payMethod = true;
                satus = 2;
                break;
            case R.id.btn_submitOrder:
                if (sendMethod == false) {
                    Toast toast = Toast.makeText(TaoShiHui_SurePayActivity.this, "", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    View view = getLayoutInflater().inflate(R.layout.toastview, null);
                    TextView toastText = (TextView) view.findViewById(R.id.toast_text);
                    toastText.setText("请选择配送方式");
                    toast.setView(view);
                    toast.show();
                } else if (payMethod == false) {
                    Toast toast = Toast.makeText(TaoShiHui_SurePayActivity.this, "", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    View view = getLayoutInflater().inflate(R.layout.toastview, null);
                    TextView toastText = (TextView) view.findViewById(R.id.toast_text);
                    toastText.setText("请选择支付方式");
                    toast.setView(view);
                    toast.show();
                } else {
                    if (satus == 1) {
                        GethdfkMessageRecycler();
                    } else if (satus == 2) {
                        GetxxzfMessageRecycler();
                    } else {
                        //支付宝
                        pay();
                    }
                }
                break;
            case R.id.layout_adress:
                intent = new Intent(this, ShowShouHuoDiZhiActivity.class);
                String name=textView_userName.getText().toString();
                String phone=textView_phone.getText().toString();
                String address=textView_address.getText().toString().replaceAll("收货地址: ","");
                intent.putExtra("address",name+phone+address);
                startActivityForResult(intent, 100);
                break;
        }
    }

    //调用支付宝
    private void pay() {
        if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE) || TextUtils.isEmpty(SELLER)) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
                            finish();
                        }
                    }).show();
            return;
        }
        String orderInfo = getOrderInfo(textView_name.getText().toString(), "该测试商品的详细描述", totalPrice);

        /**
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
         */
        String sign = sign(orderInfo);
        try {
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(TaoShiHui_SurePayActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            switch (resultCode) {
                case 1:
                    username = data.getExtras().getString("name");
                    address = data.getExtras().getString("address");
                    phone = data.getExtras().getString("phone");
                    if (username != null && phone != null && address != null) {
                        textView_userName.setText(username);
                        textView_phone.setText(phone);
                        textView_address.setText("收货地址: " + address);
                    } else {
                        layout_adress.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    }

    private void GetRequestMessageRecycler() {
        //访问时弹出dialog对话框
        progress=new ProgressDialogUtils(this);
        progress.show();
        //访问webservice服务需要的参数
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", this.getIntent().getStringExtra("id"));
        WebServiceUtils.callWebService(URLRecycler, MethodNameRecycler, NameSpace, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(SoapObject result) {
                progress.dismiss();
                if (result != null) {
                    //获取json数据
                    String json = result.getProperty(0).toString();
                    //解析json数据并放入list集合中
                    Log.e("json", "json: " + json);
                    try {
                        JSONObject object = new JSONObject(json);
                        JSONArray array = object.getJSONArray("psfs");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jo = array.getJSONObject(i);
                            String text = jo.getString("text");
                            String code = jo.getString("code");
                            Log.e("code", code);
                            if (code.equals("ps")) {
                                layout_shsm.setVisibility(View.VISIBLE);
                            } else if (code.equals("zt")) {
                                layout_zt.setVisibility(View.VISIBLE);
                            } else if (code.equals("dnxf")) {
                                layout_dnxf.setVisibility(View.VISIBLE);
                            }
                        }
                        JSONArray arr = object.getJSONArray("zffs");
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject jo = arr.getJSONObject(i);
                            String text = jo.getString("text");
                            String code = jo.getString("code");
                            if (code.equals("hdfk")) {
                                layout_hdfk.setVisibility(View.VISIBLE);
                            } else if (code.equals("wszf")) {
                                layout_wszf.setVisibility(View.VISIBLE);
                            } else if (code.equals("xxzf")) {
                                layout_xxzf.setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast toast = Toast.makeText(TaoShiHui_SurePayActivity.this, "", Toast.LENGTH_SHORT);
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

    private void GetAddressMessageRecycler() {
        //访问时弹出dialog对话框
        //ProgressDialogUtils.showProgressDialog(this, "玩命加载中...");
        //访问webservice服务需要的参数
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", getUserData().getUserId());
        WebServiceUtils.callWebService(URLAddress, MethodNameAddress, NameSpaceAddress, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(SoapObject result) {
                //ProgressDialogUtils.dismissProgressDialog();
                if (result != null) {
                    //获取json数据
                    json = result.getProperty(0).toString();
                    //解析json数据并放入list集合中
                    Log.e("json1", "json1: " + json);
                    if (json.equals("[]")) {
                        layout_adress.setVisibility(View.GONE);
                        setDialogView();
                    }
                    try {
                        JSONArray array = new JSONArray(json);
                        for (int i = 0; i < array.length(); i++) {
                            addressMap = new HashMap<>();
                            JSONObject jo = array.getJSONObject(i);
                            String id = jo.getString("id");
                            String xingming = jo.getString("xingming");
                            String moren = jo.getString("moren");
                            String sheng = jo.getString("sheng");
                            String shi = jo.getString("shi");
                            String qu = jo.getString("qu");
                            String guojia = jo.getString("guojia");
                            String xiangxi = jo.getString("xiangxi");
                            String dianhua = jo.getString("dianhua");
                            addressMap.put("id", id);
                            addressMap.put("xingming", xingming);
                            addressMap.put("moren", moren);
                            addressMap.put("sheng", sheng);
                            addressMap.put("shi", shi);
                            addressMap.put("qu", qu);
                            addressMap.put("guojia", guojia);
                            addressMap.put("xiangxi", xiangxi);
                            addressMap.put("dianhua", dianhua);
                            listRecycler.add(addressMap);
                            if(username==null){
                                if (moren.equals("Y")) {
                                    textView_userName.setText(xingming);
                                    textView_phone.setText(dianhua);
                                    textView_address.setText("收货地址: " + guojia + sheng + shi + qu + xiangxi);
                                }
                                if(layout_shsm.getVisibility()==View.GONE){
                                    layout_adress.setVisibility(View.GONE);
                                }else{
                                    layout_adress.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast toast = Toast.makeText(TaoShiHui_SurePayActivity.this, "", Toast.LENGTH_SHORT);
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

    private void GetxxzfMessageRecycler() {
        //访问时弹出dialog对话框
        //ProgressDialogUtils.showProgressDialog(this, "玩命加载中...");
        //访问webservice服务需要的参数
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", getUserData().getUserId());
        map.put("orderInfoId", this.getIntent().getStringExtra("orderId").replaceAll(",", ""));
        WebServiceUtils.callWebService(URL, MethodNamexxzf, NameSpace, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(SoapObject result) {
                //ProgressDialogUtils.dismissProgressDialog();
                if (result != null) {
                    //获取json数据
                    String json = result.getProperty(0).toString();
                    //解析json数据并放入list集合中
                    Log.e("json", "json: " + json);
                    try {
                        JSONArray array = new JSONArray("[" + json + "]");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jo = array.getJSONObject(i);
                            String results = jo.getString("result");
                            String msg = jo.getString("msg");
                            if (results.equals("true")) {
                                Toast.makeText(TaoShiHui_SurePayActivity.this, msg, Toast.LENGTH_SHORT).show();
                                if (sendMethod == false) {
                                    Toast toast = Toast.makeText(TaoShiHui_SurePayActivity.this, "", Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    View view = getLayoutInflater().inflate(R.layout.toastview, null);
                                    TextView toastText = (TextView) view.findViewById(R.id.toast_text);
                                    toastText.setText("请选择配送方式");
                                    toast.setView(view);
                                    toast.show();
                                } else if (payMethod == false) {
                                    Toast toast = Toast.makeText(TaoShiHui_SurePayActivity.this, "", Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    View view = getLayoutInflater().inflate(R.layout.toastview, null);
                                    TextView toastText = (TextView) view.findViewById(R.id.toast_text);
                                    toastText.setText("请选择支付方式");
                                    toast.setView(view);
                                    toast.show();
                                } else {
                                    SavepsfsMessageRecycler();
                                }
                            } else {
                                Toast toast = Toast.makeText(TaoShiHui_SurePayActivity.this, "", Toast.LENGTH_SHORT);
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
                    Toast toast = Toast.makeText(TaoShiHui_SurePayActivity.this, "", Toast.LENGTH_SHORT);
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

    private void GethdfkMessageRecycler() {
        //访问时弹出dialog对话框
        //ProgressDialogUtils.showProgressDialog(this, "玩命加载中...");
        //访问webservice服务需要的参数
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", getUserData().getUserId());
        map.put("orderInfoId", this.getIntent().getStringExtra("orderId").replaceAll(",", ""));
        WebServiceUtils.callWebService(URL, MethodNamehdfk, NameSpace, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(SoapObject result) {
                //ProgressDialogUtils.dismissProgressDialog();
                if (result != null) {
                    //获取json数据
                    String json = result.getProperty(0).toString();
                    //解析json数据并放入list集合中
                    Log.e("json", "json: " + json);
                    try {
                        JSONArray array = new JSONArray("[" + json + "]");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jo = array.getJSONObject(i);
                            String results = jo.getString("result");
                            String msg = jo.getString("msg");
                            if (results.equals("true")) {
                                SavepsfsMessageRecycler();
                            } else {
                                Toast toast = Toast.makeText(TaoShiHui_SurePayActivity.this, "", Toast.LENGTH_SHORT);
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
                    Toast toast = Toast.makeText(TaoShiHui_SurePayActivity.this, "", Toast.LENGTH_SHORT);
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

    private void SavepsfsMessageRecycler() {
        //访问时弹出dialog对话框
        //ProgressDialogUtils.showProgressDialog(this, "玩命加载中...");
        //访问webservice服务需要的参数
        HashMap<String, Object> map = new HashMap<>();
        map.put("orderid", this.getIntent().getStringExtra("orderId").replaceAll(",", ""));
        map.put("addressid", addressMap.get("id").toString());
        if (psfsSatus == 1) {
            map.put("psfs", "dnxf");
        } else if (psfsSatus == 2) {
            map.put("psfs", "zt");
        } else if (psfsSatus == 3) {
            map.put("psfs", "ps");
        }
        Log.e("orderId", this.getIntent().getStringExtra("orderId").replaceAll(",", ""));
        Log.e("id", addressMap.get("id").toString());
        Log.e("psfs", "addressMap: " + map.get("psfs"));
        WebServiceUtils.callWebService(URL, MethodNameSavepsfs, NameSpace, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(SoapObject result) {
                //ProgressDialogUtils.dismissProgressDialog();
                if (result != null) {
                    //获取json数据
                    String json = result.getProperty(0).toString();
                    //解析json数据并放入list集合中
                    Log.e("json3", "json3: " + json);
                    try {
                        JSONArray array = new JSONArray("[" + json + "]");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jo = array.getJSONObject(i);
                            String results = jo.getString("result");
                            String msg = jo.getString("msg");
                            if (results.equals("true")) {
                                Toast toast = Toast.makeText(TaoShiHui_SurePayActivity.this, "", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                View view = getLayoutInflater().inflate(R.layout.toastview, null);
                                TextView toastText = (TextView) view.findViewById(R.id.toast_text);
                                toastText.setText(msg);
                                toast.setView(view);
                                toast.show();
                                intent = new Intent(TaoShiHui_SurePayActivity.this, TaoShiHui_PaySuccessActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast toast = Toast.makeText(TaoShiHui_SurePayActivity.this, "", Toast.LENGTH_SHORT);
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
                    Toast toast = Toast.makeText(TaoShiHui_SurePayActivity.this, "", Toast.LENGTH_SHORT);
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

    // 商户PID
    public static final String PARTNER = "2088021821653648";
    // 商户收款账号
    public static final String SELLER = "505886138@qq.com";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICXQIBAAKBgQDykTgVi//39MaPKq3VqUsgbryo8i+zu44Hr8qhSKdXqrLHL0cyh/qGwvTGHvYxxp5Jz0KXssU32HkZz49JBntEIeJtUsfv69HOBhEORV/1F5g5J7KUgfFKBi9q8xXl/I5gtVrv4eqgIlitjzyj6Bba3vanWzwZXTGSYOb2UEr5yQIDAQABAoGAOF+VmDRcPcUubFFv6wsYQaZaxCzc9zlsjc7GGeunsJEkUXRkaJL/n1gpyO2jK/hMCz1W7aiHey4dHumFFGSkOsKEU7FLss+pzH05FqZTHdp1IdnYPopce+WIHn3DIwSD+Y3ePQgW572ZRoaMk9EcdOJUTp7bDNovXfqCBu/pd4ECQQD/XRriEckVJZCUaoXGDnrMleVrGI5eWep6DdCvxC0w5CuzR3Dytj+xoQwf74/uh9N64eTMGsT1bkHMfC78aGyxAkEA8yvziGdsDA+fQ9+DjIQvFcu2mcqCEdvPdCEhr4R36AlfLnxlKZiWfF1nc2I3U7HJm+WMMynORmEAxz5unfNEmQJBANV1/6XgViWOLChUTxS7P91Ko+b9NO0b3ow+hiaXJ2uKIBmR65GH1QBn7hm4CKnM8nPy5m3TJrc+flQvrpshs1ECQQCHS0nY96nO4BY9nitDz/uehdQXputNYl3+/7wNOoe4Kxaw93cVeJcppJI9SUT9JOrF+SZTBQyGbcwQDvVeng3pAkBY5NTSJ6f/zqlNPD2Uy4Q+T/HGx2QrfbkVVhk/Zu2ObZXvswRNTBkus6fZpMhrcmkevlqn/ksFN7KO2L3rzdgm";
    // 支付宝公钥
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
    private static final int SDK_PAY_FLAG = 1;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(TaoShiHui_SurePayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(TaoShiHui_SurePayActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(TaoShiHui_SurePayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    /**
     * call alipay sdk pay. 调用SDK支付
     *
     */

    /**
     * get the sdk version. 获取SDK版本号
     */
    public void getSDKVersion() {
        PayTask payTask = new PayTask(this);
        String version = payTask.getVersion();
        Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
    }

    /**
     * 原生的H5（手机网页版支付切natvie支付） 【对应页面网页支付按钮】
     *
     * @param v
     */
    public void h5Pay(View v) {
        Intent intent = new Intent(this, H5PayDemoActivity.class);
        Bundle extras = new Bundle();
        /**
         * url是测试的网站，在app内部打开页面是基于webview打开的，demo中的webview是H5PayDemoActivity，
         * demo中拦截url进行支付的逻辑是在H5PayDemoActivity中shouldOverrideUrlLoading方法实现，
         * 商户可以根据自己的需求来实现
         */
        String url = "http://m.taobao.com";
        // url可以是一号店或者淘宝等第三方的购物wap站点，在该网站的支付过程中，支付宝sdk完成拦截支付
        extras.putString("url", url);
        intent.putExtras(extras);
        startActivity(intent);
    }

    /**
     * create the order info. 创建订单信息
     */
    private String getOrderInfo(String subject, String body, String price) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     */
    private String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    private String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }
}
