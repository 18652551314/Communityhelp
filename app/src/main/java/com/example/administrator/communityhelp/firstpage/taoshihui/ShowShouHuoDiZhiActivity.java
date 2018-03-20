package com.example.administrator.communityhelp.firstpage.taoshihui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.communityhelp.BaseActivity;
import com.example.administrator.communityhelp.R;
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

public class ShowShouHuoDiZhiActivity extends BaseActivity implements View.OnClickListener {
    private List<Map<String, Object>> list;
    private ListView listView;
    private ShowAddressListViewAdapter adapter;
    private Intent intent;
    public static final String NameSpaceAddress = "http://member.service.zhidisoft.com";  //命名空间
    public static final String MethodNameAddress = "addressList";//方法名
    public static final String URLAddress = "http://120.27.5.22:8080/services/addressService";//地址
    private List<Map<String, Object>> listAddress;
    private ImageView imageView;
    private boolean flag=false;
    private ProgressDialogUtils progress;

    @Override
    public void init() {
        super.init();
        setContentView(R.layout.activity_show_shou_huo_di_zhi);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        GetAddressMessageRecycler();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(ShowShouHuoDiZhiActivity.this, TaoShiHui_SurePayActivity.class);
                intent.putExtra("name",listAddress.get(position).get("xingming")+"");
                intent.putExtra("phone",listAddress.get(position).get("dianhua")+"");
                intent.putExtra("address",listAddress.get(position).get("address")+"");
                adapter.setCurrent(position);
                adapter.notifyDataSetChanged();
                setResult(1, intent);
                finish();
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
                    adapter = new ShowAddressListViewAdapter(listAddress, ShowShouHuoDiZhiActivity.this);
                    listView.setAdapter(adapter);
                } else {
                    Toast toast = Toast.makeText(ShowShouHuoDiZhiActivity.this, "", Toast.LENGTH_SHORT);
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

    private void initView() {
        listView = (ListView) findViewById(R.id.listView_showAddress);
        findViewById(R.id.ShowAddressActivity_back).setOnClickListener(this);
        findViewById(R.id.layout_manageAddress).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ShowAddressActivity_back:
                finish();
                break;
            case R.id.layout_manageAddress:
                intent = new Intent(this, ManageShouHuoDiZhiActivity.class);
                startActivity(intent);
                break;
        }
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
                Log.e("add1", xingming+dianhua+address);
                Log.e("add2", this.getIntent().getStringExtra("address"));
                if((xingming+dianhua+address).equals(this.getIntent().getStringExtra("address"))){
                    flag=true;
                }else{
                    flag=false;
                }
                Log.e("flag1", flag+"");
                addressMap.put("flag", flag);
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

}
