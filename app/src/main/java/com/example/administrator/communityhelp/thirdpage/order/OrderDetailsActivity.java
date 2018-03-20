package com.example.administrator.communityhelp.thirdpage.order;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.communityhelp.BaseActivity;
import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.firstpage.taoshihui.TaoShiHui_DingDanXiangQingActivity;
import com.example.administrator.communityhelp.firstpage.taoshihui.TaoShiHui_ShangPingXiangQingActivity;
import com.example.administrator.communityhelp.firstpage.taoshihui.TaoShiHui_SurePayActivity;
import com.example.administrator.communityhelp.my_other.GlideLoader;
import com.example.administrator.communityhelp.webserviceutils.ProgressDialogUtils;
import com.example.administrator.communityhelp.webserviceutils.WebServiceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDetailsActivity extends BaseActivity {
    TextView textView_name;
    TextView textView_currentPrice;
    TextView textView_previous_Price;
    TextView textView_orderNum;
    TextView textView_orderTime;
    TextView textView_telNum;
    TextView textView_count;
    TextView textView_Cost;
    TextView textView_payMethoed;
    TextView textView_sendMethoed;
    TextView textView_back;
    Button btn_pay;
    ImageView imageView;
    String goodsId;
    ProgressDialogUtils progress;

    public static final String NameSpace = "http://goods.service.zhidisoft.com";  //命名空间
    public static final String MethodName = "orderInfo";//方法名
    public static final String URL = "http://120.27.5.22:8080/services/goodsOrderService";//地址

    @Override
    public void init() {
        super.init();
        setContentView(R.layout.activity_order_details);
        initView();
        GetRequestMessageRecycler();
        textView_previous_Price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        textView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OrderDetailsActivity.this,TaoShiHui_ShangPingXiangQingActivity.class);
                intent.putExtra("id",goodsId);
                startActivity(intent);
            }
        });
    }

    private void initView() {
         textView_name= (TextView) findViewById(R.id.textView_myOrder_goodsName_details);
         textView_currentPrice= (TextView) findViewById(R.id.textView_myOrder_current_price_details);
         textView_previous_Price= (TextView) findViewById(R.id.textView_myOrder_privious_price_details);
         textView_orderNum= (TextView) findViewById(R.id.textView_orderNumber_order_detail);
         textView_orderTime= (TextView) findViewById(R.id.textView_orderTime_order_detail);
         textView_telNum= (TextView) findViewById(R.id.textView_orderTel_order_detail);
         textView_count= (TextView) findViewById(R.id.textView_orderCount_order_detail);
         textView_Cost= (TextView) findViewById(R.id.textView_orderCost_order_detail);
         textView_payMethoed= (TextView) findViewById(R.id.textView_orderPayMethoed_order_detail);
         textView_sendMethoed= (TextView) findViewById(R.id.textView_orderSendMethoed_order_detail);
         textView_back= (TextView) findViewById(R.id.textView_back);
         btn_pay= (Button) findViewById(R.id.btn_pay);
         imageView= (ImageView) findViewById(R.id.imageView_myOrder_details);
    }

    private void GetRequestMessageRecycler() {
        //访问时弹出dialog对话框
        progress=new ProgressDialogUtils(this);
        progress.show();
        //访问webservice服务需要的参数
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", this.getIntent().getStringExtra("id"));
        WebServiceUtils.callWebService(URL, MethodName, NameSpace, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(SoapObject result) {
                progress.dismiss();
                if (result != null) {
                    //获取json数据
                    String json = result.getProperty(0).toString();
                    //解析json数据并放入list集合中
                    //Log.e("json", "json: "+json);
                    try {
                        JSONArray array = new JSONArray("["+json+"]");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jo = array.getJSONObject(i);
                            final String id = jo.getString("id");
                            String orderGoodsDetailForms = jo.getString("orderGoodsDetailForms");
                            String orderNo = jo.getString("orderNo");
                            String createOrderTime = jo.getString("createOrderTime");
                            final String goodsCount = jo.getString("goodsCount");
                            final String money = jo.getString("money");
                            String payWay = jo.getString("payWay");
                            String psfs = jo.getString("psfs");
                            String dianhua = jo.getString("dianhua");
                            textView_orderNum.setText("订单号: "+orderNo);
                            textView_orderTime.setText("下单时间: "+createOrderTime);
                            textView_telNum.setText("购买手机号: "+dianhua);
                            textView_count.setText("数量: "+goodsCount);
                            textView_Cost.setText("总价: "+money+"元");
                            textView_payMethoed.setText("支付方式: "+payWay);
                            textView_sendMethoed.setText("配送方式: "+psfs);
                            JSONArray arr = new JSONArray(orderGoodsDetailForms);
                            for (int j = 0; j < arr.length(); j++) {
                                JSONObject jos = arr.getJSONObject(j);
                                goodsId = jos.getString("goodsId");
                                String oldPrice = jos.getString("oldPrice");
                                String discountPrice = jos.getString("discountPrice");
                                final String goodsName = jos.getString("goodsName");
                                String picPath = jos.getString("picPath");
                                textView_name.setText(goodsName);
                                textView_currentPrice.setText(discountPrice+"元");
                                textView_previous_Price.setText(oldPrice+"元");
                                new GlideLoader().displayImage(OrderDetailsActivity.this,picPath, imageView);
                                Log.e("orderStateCode", OrderDetailsActivity.this.getIntent().getStringExtra("orderStateCode"));
                                if(!OrderDetailsActivity.this.getIntent().getStringExtra("orderStateCode").equals("consumption")){
                                    btn_pay.setVisibility(View.VISIBLE);
                                    btn_pay.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(OrderDetailsActivity.this, TaoShiHui_SurePayActivity.class);
                                            intent.putExtra("id",goodsId);
                                            intent.putExtra("name",goodsName);
                                            DecimalFormat decimalFormat = new DecimalFormat("###################.###########");
                                            Double price=Double.parseDouble(money)/Double.parseDouble(goodsCount);
                                            intent.putExtra("price",decimalFormat.format(price));
                                            intent.putExtra("totalPrice",money);
                                            intent.putExtra("buyNum",goodsCount);
                                            intent.putExtra("orderId",id);
                                            startActivity(intent);
                                        }
                                    });
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast toast = Toast.makeText(OrderDetailsActivity.this, "", Toast.LENGTH_SHORT);
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
}
