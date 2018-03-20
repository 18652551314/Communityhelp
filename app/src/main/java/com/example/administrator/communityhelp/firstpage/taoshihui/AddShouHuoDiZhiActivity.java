package com.example.administrator.communityhelp.firstpage.taoshihui;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.communityhelp.BaseActivity;
import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.myadapter.AddressListViewAdapter;
import com.example.administrator.communityhelp.webserviceutils.WebServiceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddShouHuoDiZhiActivity extends BaseActivity implements View.OnClickListener {
    private TextView textView_city, textView_row;
    private EditText editText_address, editText_name, editText_phone;
    private PopupWindow popupWindow;
    private LinearLayout layout_cancel;
    private ListView listView_row;
    private AddressListViewAdapter adapter;
    private List list;
    private String adrName[] = {"上街区", "中原区", "二七区", "惠济区", "管城区", "经济开发区",
            "郑州新区", "金水区", "高新开发区"};
    private String city, row, address, username, phone;
    private Intent intent;
    private CheckBox checkBox;

    public static final String NameSpaceAddress = "http://member.service.zhidisoft.com";  //命名空间
    public static final String MethodNameAddress = "addressSave";//方法名
    public static final String URLAddress = "http://120.27.5.22:8080/services/addressService";//地址
    private List<Map<String, Object>> listAddress;

    @Override
    public void init() {
        super.init();
        setContentView(R.layout.activity_shou_huo_di_zhi);
        initView();
    }

    private void initView() {
        findViewById(R.id.AddressActivity_back).setOnClickListener(this);
        findViewById(R.id.btn_save).setOnClickListener(this);
        textView_city = (TextView) findViewById(R.id.textView_city);
        textView_row = (TextView) findViewById(R.id.textView_row);
        textView_row.setOnClickListener(this);
        editText_address = (EditText) findViewById(R.id.editText_address);
        editText_name = (EditText) findViewById(R.id.editText_name);
        editText_phone = (EditText) findViewById(R.id.editText_phone);
        checkBox = (CheckBox) findViewById(R.id.checkbox);
        checkBox.setChecked(true);
        checkBox.setClickable(false);
        checkBox.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.AddressActivity_back:
                finish();
                break;
            case R.id.btn_save:
                if (textView_row.getText().toString().equals("")) {
                    Toast.makeText(AddShouHuoDiZhiActivity.this, "请选择街道信息！", Toast.LENGTH_SHORT).show();
                } else if (editText_address.getText().toString().equals("")) {
                    Toast.makeText(AddShouHuoDiZhiActivity.this, "请输入详细地址信息！", Toast.LENGTH_SHORT).show();
                } else if (editText_name.getText().toString().equals("")) {
                    Toast.makeText(AddShouHuoDiZhiActivity.this, "请输入收货人信息！", Toast.LENGTH_SHORT).show();
                } else if (editText_phone.getText().toString().equals("")) {
                    Toast.makeText(AddShouHuoDiZhiActivity.this, "请输入联系电话信息！", Toast.LENGTH_SHORT).show();
                } else {
                    AddAddressMessageRecycler();
                }
                break;
            case R.id.textView_row:
                View view = getLayoutInflater().inflate(R.layout.rowpopuwindow, null);
                layout_cancel = (LinearLayout) view.findViewById(R.id.layout_cancel);
                listView_row = (ListView) view.findViewById(R.id.listView_row);
                popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT, true);
                //popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                backgroundAlpha(0.5f);
                layout_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        backgroundAlpha(1f);
                        popupWindow.dismiss();
                    }
                });
                list = new ArrayList();
                getListViewData();
                adapter = new AddressListViewAdapter(list, this);
                listView_row.setAdapter(adapter);
                listView_row.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        textView_row.setText(list.get(position) + "");
                        backgroundAlpha(1f);
                        popupWindow.dismiss();
                    }
                });
                break;
        }
    }

    private void AddAddressMessageRecycler() {
        //访问时弹出dialog对话框
        //ProgressDialogUtils.showProgressDialog(this, "玩命加载中...");
        //访问webservice服务需要的参数
        city = textView_city.getText().toString();
        row = textView_row.getText().toString();
        address = editText_address.getText().toString();
        username = editText_name.getText().toString();
        phone = editText_phone.getText().toString();
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", "");
        map.put("xingming", username);
        map.put("dianhua", phone);
        map.put("guojia", "中国");
        map.put("sheng", "河南省");
        map.put("shi", "郑州市");
        map.put("qu", row);
        map.put("xiangxi", address);
        map.put("youbian", "473000");
        if(checkBox.isChecked()){
            map.put("moren", "Y");
        }else{
            map.put("moren", "N");
        }
        map.put("userId", getUserData().getUserId());
        WebServiceUtils.callWebService(URLAddress, MethodNameAddress, NameSpaceAddress, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(SoapObject result) {
                //ProgressDialogUtils.dismissProgressDialog();
                if (result != null) {
                    //获取json数据
                    String json = result.getProperty(0).toString();
                    //解析json数据并放入list集合中
                    try {
                        JSONArray array = new JSONArray("["+json+"]");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jo = array.getJSONObject(i);
                            String results = jo.getString("result");
                            String msg = jo.getString("msg");
                            if(results.equals("true")){
                                Toast.makeText(AddShouHuoDiZhiActivity.this, msg, Toast.LENGTH_SHORT).show();
                                intent = new Intent(AddShouHuoDiZhiActivity.this, TaoShiHui_SurePayActivity.class);
                                intent.putExtra("name",username);
                                intent.putExtra("phone",phone);
                                intent.putExtra("address",city+row+address);
                                setResult(1, intent);
                                finish();
                            }else{
                                Toast toast = Toast.makeText(AddShouHuoDiZhiActivity.this, "", Toast.LENGTH_SHORT);
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
                    Toast toast = Toast.makeText(AddShouHuoDiZhiActivity.this, "", Toast.LENGTH_SHORT);
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
    public List<Map<String, Object>> jsonPaseAddress(String result) {
        listAddress = new ArrayList();
        try {
            JSONArray array = new JSONArray(result);
            for (int i = 0; i < array.length(); i++) {
                Map<String, Object> addressMap = new HashMap<>();
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
                String address = guojia+sheng+shi+qu+xiangxi;
                addressMap.put("id", id);
                addressMap.put("xingming", xingming);
                addressMap.put("moren", moren);
                addressMap.put("sheng", sheng);
                addressMap.put("shi", shi);
                addressMap.put("qu", qu);
                addressMap.put("guojia", guojia);
                addressMap.put("xiangxi", xiangxi);
                addressMap.put("dianhua", dianhua);
                addressMap.put("address", address);
                listAddress.add(addressMap);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listAddress;
    }

    private void getListViewData() {
        for (int i = 0; i < adrName.length; i++) {
            list.add(adrName[i]);
        }
    }

    //设置屏幕半透明
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }


}
