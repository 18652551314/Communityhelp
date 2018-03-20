package com.example.administrator.communityhelp.firstpage.linliquan.second_hand;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.communityhelp.BaseActivity;
import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.UpLoadUtils;
import com.example.administrator.communityhelp.firstpage.linliquan.recruit.Recruit_Details_main;
import com.example.administrator.communityhelp.firstpage.linliquan.recruit.Recruit_Details_main_1;
import com.example.administrator.communityhelp.firstpage.linliquan.tourism.Tourism_Details_Main;
import com.example.administrator.communityhelp.my_other.CameraTakeCut;
import com.example.administrator.communityhelp.my_other.GlideLoader;
import com.example.administrator.communityhelp.thirdpage.publication.bbs.AdapterCallBack;
import com.example.administrator.communityhelp.thirdpage.publication.bbs.CommunityActivity;
import com.example.administrator.communityhelp.thirdpage.publication.bbs.GridViewImageAdapter;
import com.example.administrator.communityhelp.thirdpage.publication.bbs.PictureViewPagerActivity;
import com.example.administrator.communityhelp.webserviceutils.ProgressDialogUtils;
import com.example.administrator.communityhelp.webserviceutils.WebServiceUtils;
import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Seond_Hand_Details_Main extends CameraTakeCut implements AdapterCallBack,View.OnClickListener{
    View view;
    AlertDialog dialog;
    static String URL = "http://120.27.5.22:8080/services/esInfoService";
    static String NameSpace ="http://info.service.zhidisoft.com";
    static String MethoedName = "esInfoSaveBySell";
    //以下为借口部分数据
    private List<Map<String, Object>> listGridTop, listGridCenter;
    String id, title, content, contact, contactPhone, price,oldPrice,oldNew,exchange,type, comIds, tempFileIds, userId;
    private List<Map<String, Object>> listRecycler;
    EditText editText1,editText2,editText3,editText4,editText5;
    TextView textView,textViewKind;
    Intent intent;
    View popupView;
    ProgressDialogUtils progress;
    //选择二手类型的window
    PopupWindow popupWindow;
    //选择照片的window
    PopupWindow popwindow;
    TextView text_select_pic;
    TextView text_take_pic;
    TextView text;
    RelativeLayout relativeLayout1, RelativeLayout2, RelativeLayout3, RelativeLayout4, RelativeLayout5, RelativeLayout6, RelativeLayout7, RelativeLayout8, RelativeLayout9;
    LinearLayout linearLayout;
    GridView gridView;
    int screenWidth;
    int screenHeight;
    GridViewImageAdapter adapter;
    int image = R.drawable.pic_jia;
    //选择照片的存放路径的容器
    List<String> pathlist=new ArrayList<>();
    //拍照的存放照片的容器
    List<String> cameraList=new ArrayList<>();
    @Override
    public void init() {
        super.init();
        setContentView(R.layout.activity_seond__hand__details__main);
        popupView = Seond_Hand_Details_Main.this.getLayoutInflater().inflate(R.layout.ershoupopuwindow, null);
        text = (TextView) findViewById(R.id.tv_second_kind);
        gridView= (GridView) findViewById(R.id.grideView_secondhand_pic);
        //获取屏幕宽高
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight=dm.heightPixels;
        //设置显示的图片
        pathlist.add(image + "");
        saveImagePath(pathlist);
        adapter=new GridViewImageAdapter(this,this,pathlist,screenWidth);
        gridView.setAdapter(adapter);
        //点击事件,如果是最后一个开启pop,如果不是跳转activity
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == pathlist.size() - 1) {
                    showPopWindow();
                } else {
                    Intent intent = new Intent(Seond_Hand_Details_Main.this, PictureViewPagerActivity.class);
                    intent.putStringArrayListExtra("path", (ArrayList<String>) pathlist);
                    intent.putExtra("position", position);
                    startActivity(intent);
                }
            }
        });
        editText1= (EditText) findViewById(R.id.et_tourism_secondHand);
        editText2= (EditText) findViewById(R.id.et_tourism1_secondHand);
        editText3= (EditText) findViewById(R.id.et_tourism2_secondHand);
        editText4= (EditText) findViewById(R.id.et_tourism_3_secondHand);
        textView= (TextView) findViewById(R.id.first_tit_tourism_secondHand);
        editText5=(EditText) findViewById(R.id.et_tourism4_secondHand);
        textView.setText("二手转让发布");
        editText1.setHint("输入标题");
        editText2.setHint("输入联系人");
        editText3.setHint("输入联系电话");
        editText4.setHint("输入描述");
        editText5.setHint("输入转让价（选填）");

    }

    public void tourism_details_main_ll_1(View v) {
        finish();
    }
    public void fabu_secondhandSell(View v) {
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
        List<String> list = new ArrayList<>();
        List<Object> value = new ArrayList<>();
        String[] fileNames = new String[pathlist.size() - 1];
        File[] files = new File[pathlist.size() - 1];
        for (int i = 0; i < pathlist.size() - 1; i++) {
            files[i] = new File(pathlist.get(i));
            fileNames[i] = files[i].getName();
        }
        list.add("id");
        list.add("title");
        list.add("content");
        list.add("contact");
        list.add("contactPhone");
        list.add("price");
        list.add("oldPrice");
        list.add("oldNew");
        list.add("exchange");
        list.add("type");
        list.add("comIds");
        list.add("userId");
        value.add("");
        value.add(title);
        value.add(content);
        value.add(contact);
        value.add(contactPhone);
        value.add(price);
        value.add(oldPrice);
        value.add(oldNew);
        value.add(exchange);
        value.add(type);
        value.add(comIds);
        value.add(userId);
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", "");
        map.put("title", title);
        map.put("content", content);//297ebe0e593e1bf9015944c131201242
        map.put("contact", contact);
        map.put("contactPhone", contactPhone);
        map.put("price",price);
        map.put("oldPrice",oldPrice);
        map.put("oldNew",oldNew);
        map.put("exchange",exchange);
        map.put("type", type);
        map.put("comIds", comIds);
        map.put("tempFileIds", tempFileIds);
        map.put("userId", userId);
        //map.put("curTime", System.currentTimeMillis() + "");
        if (pathlist.size() == 1) {
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
                        Toast.makeText(Seond_Hand_Details_Main.this, "获取WebService数据错误", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            //访问时弹出dialog对话框
            progress=new ProgressDialogUtils(this);
            progress.show();
            UpLoadUtils.getNetData(URL,
                    NameSpace, MethoedName, true, new UpLoadUtils.OnRequestListener() {
                        @Override
                        public void successed(SoapObject data) {
                            Log.e("isSuccess", data.getProperty(0).toString());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Seond_Hand_Details_Main.this, "发布成功(包含图片)", Toast.LENGTH_SHORT).show();
                                    progress.dismiss();
                                    setDialogView();
                                }
                            });
                        }
                        @Override
                        public void failed(String str) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Seond_Hand_Details_Main.this, "发布失败，请检查网络设置s", Toast.LENGTH_SHORT).show();
                                    ProgressDialogUtils.dismissProgressDialog();
                                }
                            });
                        }
                    }, list, value, files, fileNames);
        }
    }

    public void second_hand_rl(View v) {
        backgroundAlpha(0.5f);
        popupWindow = new PopupWindow(popupView, 800, 1700, true);
        relativeLayout1 = (android.widget.RelativeLayout) popupView.findViewById(R.id.second_popu_shuma);
        RelativeLayout2 = (android.widget.RelativeLayout) popupView.findViewById(R.id.second_popu_yinxiang);
        RelativeLayout3 = (android.widget.RelativeLayout) popupView.findViewById(R.id.second_popu_baojian);
        RelativeLayout4 = (android.widget.RelativeLayout) popupView.findViewById(R.id.second_popu_jiaju);
        RelativeLayout5 = (android.widget.RelativeLayout) popupView.findViewById(R.id.second_popu_muying);
        RelativeLayout6 = (android.widget.RelativeLayout) popupView.findViewById(R.id.second_popu_fushi);
        RelativeLayout7 = (android.widget.RelativeLayout) popupView.findViewById(R.id.second_popu_jiadian);
        RelativeLayout8 = (android.widget.RelativeLayout) popupView.findViewById(R.id.second_popu_bangong);
        RelativeLayout9 = (android.widget.RelativeLayout) popupView.findViewById(R.id.second_popu_qita);
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

    @Override
    public void sendMessage(int position) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImageSelector.IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Get Image Path List
            pathlist = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
            for (String path : pathlist) {
                Log.i("ImagePathList", path);
            }
        }
        if (requestCode == 1111 && data != null) {
            String takePicPath = CameraTakeCut.getTakePicPath(this, Activity.RESULT_OK, data);
            if (takePicPath.equals("")) {
            } else {
                cameraList.add(takePicPath);
                for (String path :
                        cameraList) {
                    pathlist.add(path);
                }
            }
        }
        pathlist.add(image + "");
        adapter.setPathlist(pathlist);
        adapter.notifyDataSetChanged();
        saveImagePath(pathlist);

    }

    private void showPopWindow() {
        View view = View.inflate(this, R.layout.bbs_pic_select_popwindow, null);
        popwindow = new PopupWindow(view);
        popwindow.setFocusable(true);
        popwindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popwindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayout layout1 = (LinearLayout) view.findViewById(R.id.linearLayout_dismiss_pop1);
        LinearLayout layout2 = (LinearLayout) view.findViewById(R.id.linearLayout_dismiss_pop2);
        text_select_pic = (TextView) view.findViewById(R.id.textView_selpic);
        text_take_pic = (TextView) view.findViewById(R.id.textView_takepic);
//        popwindow.setAnimationStyle(R.style.PicPopupAnimation);
        popwindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        text_select_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPicture();
            }
        });
        text_take_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePictuere();
            }
        });
        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popwindow.dismiss();
            }
        });
        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popwindow.dismiss();
            }
        });
    }
    private void takePictuere() {
        pathlist.clear();
        if (cameraList.size() >= 5) {
            Toast.makeText(this,"拍照不要超过5张",Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1111);
    }

    //选择照片的方法
    private void selectPicture() {
        pathlist.clear();
        ImageConfig imageConfig
                = new ImageConfig.Builder(new GlideLoader())
                .steepToolBarColor(getResources().getColor(R.color.blue))
                .titleBgColor(getResources().getColor(R.color.blue))
                .titleSubmitTextColor(getResources().getColor(R.color.white))
                .titleTextColor(getResources().getColor(R.color.white))
                .crop(1, 1, screenWidth, screenHeight / 2)
                // 开启多选   （默认为多选）
                .mutiSelect()
                // 多选时的最大数量   （默认 9 张）
                .mutiSelectMaxSize(5)
                // 开启拍照功能 （默认关闭）
//                .showCamera()
                // 已选择的图片路径
                .pathList((ArrayList<String>) pathlist)
                // 拍照后存放的图片路径（默认 /temp/picture） （会自动创建）
                .filePath("/ImageSelector/Pictures")
                .build();
        ImageSelector.open(this, imageConfig);   // 开启图片选择器
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(popwindow!=null){
            popwindow.dismiss();
        }
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
                Toast.makeText(Seond_Hand_Details_Main.this, msg, Toast.LENGTH_SHORT).show();
                listGridTop.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listGridTop;
    }


    //删除图片回来重新加载的事件
    @Override
    protected void onStart() {
        super.onStart();
        pathlist.clear();
        Cursor cursor = dbReader.query("imagePath", null, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String imagePath = cursor.getString(cursor.getColumnIndex("imagePath"));
            pathlist.add(imagePath);
            Log.e("path", imagePath);
        }
        adapter.setPathlist(pathlist);
        adapter.notifyDataSetChanged();
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
