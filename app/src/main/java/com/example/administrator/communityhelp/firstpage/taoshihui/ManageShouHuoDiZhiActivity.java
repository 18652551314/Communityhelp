package com.example.administrator.communityhelp.firstpage.taoshihui;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.communityhelp.BaseActivity;
import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.myadapter.AddAddressListViewAdapter;
import com.example.administrator.communityhelp.myadapter.ShowAddressListViewAdapter;
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

public class ManageShouHuoDiZhiActivity extends BaseActivity implements View.OnClickListener {
    private List<Map<String, Object>> list;
    private ListView listView;
    private AddAddressListViewAdapter adapter;
    private Intent intent;

    public static final String NameSpaceAddress = "http://member.service.zhidisoft.com";  //命名空间
    public static final String MethodNameAddress = "addressList";//方法名
    public static final String URLAddress = "http://120.27.5.22:8080/services/addressService";//地址
    private List<Map<String, Object>> listAddress;
    private LinearLayout layout_addAddress;
    private ProgressDialogUtils progress;

    @Override
    public void init() {
        super.init();
        setContentView(R.layout.activity_manage_shou_huo_di_zhi);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        GetAddressMessageRecycler();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(ManageShouHuoDiZhiActivity.this, AddOrDeleteAddressActivity.class);
                intent.putExtra("id",listAddress.get(position).get("id")+"");
                intent.putExtra("name",listAddress.get(position).get("xingming")+"");
                intent.putExtra("youbian",listAddress.get(position).get("youbian")+"");
                intent.putExtra("phone",listAddress.get(position).get("dianhua")+"");
                intent.putExtra("sheng",listAddress.get(position).get("sheng")+"");
                intent.putExtra("shi",listAddress.get(position).get("shi")+"");
                intent.putExtra("guojia",listAddress.get(position).get("guojia")+"");
                intent.putExtra("address",listAddress.get(position).get("xiangxi")+"");
                intent.putExtra("qu",listAddress.get(position).get("qu")+"");
                intent.putExtra("moren",listAddress.get(position).get("moren")+"");
                startActivity(intent);
            }
        });
    }

    private void GetAddressMessageRecycler() {
        //访问时弹出dialog对话框
        progress=new ProgressDialogUtils(this);
        progress.show();
        //访问webservice服务需要的参数
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", getUserData().getUserId());
        WebServiceUtils.callWebService(URLAddress, MethodNameAddress, NameSpaceAddress, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(SoapObject result) {
                progress.dismiss();
                if (result != null) {
                    //获取json数据
                    String json = result.getProperty(0).toString();
                    //解析json数据并放入list集合中
                    listAddress=jsonPaseAddress(json);
                    adapter = new AddAddressListViewAdapter(listAddress, ManageShouHuoDiZhiActivity.this);
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);
                } else {
                    Toast toast = Toast.makeText(ManageShouHuoDiZhiActivity.this, "", Toast.LENGTH_SHORT);
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
                String youbian = jo.getString("youbian");
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
                addressMap.put("youbian", youbian);
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

    private void initView() {
        listView = (ListView) findViewById(R.id.listView_showAddress);
        findViewById(R.id.ShowAddressActivity_back).setOnClickListener(this);
        findViewById(R.id.layout_addAddress).setOnClickListener(this);
        layout_addAddress= (LinearLayout) findViewById(R.id.layout_addAddress);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ShowAddressActivity_back:
                finish();
                break;
            case R.id.layout_addAddress:
                intent = new Intent(this, ShouHuoDiZhiActivity.class);
                startActivity(intent);
                break;
        }
    }
}
