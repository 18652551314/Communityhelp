package com.example.administrator.communityhelp.firstpage.taoshihui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class AddOrDeleteAddressActivity extends BaseActivity implements View.OnClickListener{
    private EditText editText_name,editText_phone,editText_address;
    private TextView textView_row,textView_dialog_message;
    private PopupWindow popupWindow;
    private LinearLayout layout_cancel;
    private ListView listView_row;
    private AddressListViewAdapter adapter;
    private List list;
    private String adrName[]={"上街区","中原区","二七区","惠济区","管城区","经济开发区",
            "郑州新区","金水区","高新开发区"};
    public static final String NameSpaceAddress = "http://member.service.zhidisoft.com";  //命名空间
    public static final String MethodNameAddress = "addressSave";//方法名
    public static final String MethodNameAddressRemove = "addressRemove";//方法名
    public static final String URLAddress = "http://120.27.5.22:8080/services/addressService";//地址
    private List<Map<String, Object>> listAddress;
    private String row, address, username, phone;
    private View view;
    private AlertDialog dialog;

    @Override
    public void init() {
        super.init();
        setContentView(R.layout.activity_add_or_delete_address);
        initView();
        editText_name.setText(this.getIntent().getStringExtra("name"));
        editText_phone.setText(this.getIntent().getStringExtra("phone"));
        editText_address.setText(this.getIntent().getStringExtra("address"));
        textView_row.setText(this.getIntent().getStringExtra("qu"));
    }

    private void initView() {
        editText_name= (EditText) findViewById(R.id.editText_name);
        editText_phone= (EditText) findViewById(R.id.editText_phone);
        editText_address= (EditText) findViewById(R.id.editText_address);
        textView_row= (TextView) findViewById(R.id.textView_row);
        findViewById(R.id.textView_back).setOnClickListener(this);
        findViewById(R.id.textView_row).setOnClickListener(this);
        findViewById(R.id.image_deleteAddress).setOnClickListener(this);
        findViewById(R.id.btn_save).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textView_back:
                finish();
                break;
            case R.id.textView_row:
                View view=getLayoutInflater().inflate(R.layout.rowpopuwindow,null);
                layout_cancel= (LinearLayout) view.findViewById(R.id.layout_cancel);
                listView_row= (ListView) view.findViewById(R.id.listView_row);
                popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT, true);
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
                list=new ArrayList();
                getListViewData();
                adapter=new AddressListViewAdapter(list,this);
                listView_row.setAdapter(adapter);
                listView_row.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        textView_row.setText(list.get(position)+"");
                        backgroundAlpha(1f);
                        popupWindow.dismiss();
                    }
                });
                break;
            case R.id.image_deleteAddress:
                setDialogView();
                break;
            case R.id.btn_sure:
                DeleteAddressMessageRecycler();
                dialog.dismiss();
                break;
            case R.id.btn_cancel:
                dialog.dismiss();
                break;
            case R.id.btn_save:
                if (textView_row.getText().toString().equals("")) {
                    Toast.makeText(AddOrDeleteAddressActivity.this, "请选择街道信息！", Toast.LENGTH_SHORT).show();
                } else if (editText_address.getText().toString().equals("")) {
                    Toast.makeText(AddOrDeleteAddressActivity.this, "请输入详细地址信息！", Toast.LENGTH_SHORT).show();
                } else if (editText_name.getText().toString().equals("")) {
                    Toast.makeText(AddOrDeleteAddressActivity.this, "请输入收货人信息！", Toast.LENGTH_SHORT).show();
                } else if (editText_phone.getText().toString().equals("")) {
                    Toast.makeText(AddOrDeleteAddressActivity.this, "请输入联系电话信息！", Toast.LENGTH_SHORT).show();
                } else {
                    UpdateAddressMessageRecycler();
                }
                break;
        }
    }

    private void UpdateAddressMessageRecycler() {
        //访问时弹出dialog对话框
        //progress=new ProgressDialogUtils(this);
        //progress.show();
        //访问webservice服务需要的参数
        row = textView_row.getText().toString();
        address = editText_address.getText().toString();
        username = editText_name.getText().toString();
        phone = editText_phone.getText().toString();
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", this.getIntent().getStringExtra("id"));
        map.put("xingming", username);
        map.put("dianhua", phone);
        map.put("guojia", this.getIntent().getStringExtra("guojia"));
        map.put("sheng", this.getIntent().getStringExtra("sheng"));
        map.put("shi", this.getIntent().getStringExtra("shi"));
        map.put("qu", row);
        map.put("xiangxi", address);
        map.put("youbian", this.getIntent().getStringExtra("youbian"));
        map.put("moren", this.getIntent().getStringExtra("moren"));
        map.put("userId", getUserData().getUserId());
        WebServiceUtils.callWebService(URLAddress, MethodNameAddress, NameSpaceAddress, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(SoapObject result) {
                //progress.dismiss();
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
                                //Toast若是new出来，则需设置setView
                                Toast toast = Toast.makeText(AddOrDeleteAddressActivity.this, "", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                View view = getLayoutInflater().inflate(R.layout.toastview, null);
                                TextView toastText = (TextView) view.findViewById(R.id.toast_text);
                                toastText.setText(msg);
                                toast.setView(view);
                                toast.show();
                                finish();
                            }else{
                                //Toast若是new出来，则需设置setView
                                Toast toast = Toast.makeText(AddOrDeleteAddressActivity.this, "", Toast.LENGTH_SHORT);
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
                    //Toast若是new出来，则需设置setView
                    Toast toast = Toast.makeText(AddOrDeleteAddressActivity.this, "", Toast.LENGTH_SHORT);
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

    private void DeleteAddressMessageRecycler() {
        //访问时弹出dialog对话框
        //ProgressDialogUtils.showProgressDialog(this, "玩命加载中...");
        //访问webservice服务需要的参数
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", this.getIntent().getStringExtra("id"));
        WebServiceUtils.callWebService(URLAddress, MethodNameAddressRemove, NameSpaceAddress, map, new WebServiceUtils.WebServiceCallBack() {
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
                                //Toast若是new出来，则需设置setView
                                Toast toast = Toast.makeText(AddOrDeleteAddressActivity.this, "", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                View view = getLayoutInflater().inflate(R.layout.toastview, null);
                                TextView toastText = (TextView) view.findViewById(R.id.toast_text);
                                toastText.setText(msg);
                                toast.setView(view);
                                toast.show();
                                finish();
                            }else{
                                //Toast若是new出来，则需设置setView
                                Toast toast = Toast.makeText(AddOrDeleteAddressActivity.this, "", Toast.LENGTH_SHORT);
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
                    //Toast若是new出来，则需设置setView
                    Toast toast = Toast.makeText(AddOrDeleteAddressActivity.this, "", Toast.LENGTH_SHORT);
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

    private void getListViewData() {
        for (int i = 0; i < adrName.length; i++) {
            list.add(adrName[i]);
        }
    }

    private void setDialogView() {
        view = getLayoutInflater().inflate(R.layout.dialog_view, null);
        view.findViewById(R.id.btn_sure).setOnClickListener(this);
        view.findViewById(R.id.btn_cancel).setOnClickListener(this);
        view.getBackground().setAlpha(150);
        textView_dialog_message= (TextView) view.findViewById(R.id.textView_dialog_message);
        textView_dialog_message.setText("是否删除该地址信息？");
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

    //设置屏幕半透明
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }
}
