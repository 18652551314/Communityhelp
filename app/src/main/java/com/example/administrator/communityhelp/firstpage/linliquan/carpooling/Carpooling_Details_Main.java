package com.example.administrator.communityhelp.firstpage.linliquan.carpooling;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.UpLoadUtils;
import com.example.administrator.communityhelp.my_other.CameraTakeCut;
import com.example.administrator.communityhelp.my_other.GlideLoader;
import com.example.administrator.communityhelp.myadapter.LunTan_One_ViewAdapter;
import com.example.administrator.communityhelp.thirdpage.about_us.WebSerVieceUtil;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Carpooling_Details_Main extends CameraTakeCut implements AdapterCallBack, View.OnClickListener {
    View view;
    ProgressDialogUtils progress;
    AlertDialog dialog;
    TextView textView;
    DatePickerDialog datePickerDialog;
    Intent intent_uesr;
    String time, start, end, infoType, carInfo, startTime, content, contact, contactPhone, comIds, tempFileIds, userId;
    //点击显示图片
    GridView gridView;
    //选择是照相机还是图库
    PopupWindow popwindow;
    //popwindow的点击对象
    TextView text_take_pic;
    //popwindow的点击对象
    TextView text_select_pic, editText_shijian;
    //存放选择图片的容器
    List<String> pathlist = new ArrayList<>();
    //存放拍照的容器
    List<String> cameraList = new ArrayList<>();
    //最后一张照片
    int image = R.drawable.pic_jia;
    //屏幕宽高
    int screenWidth;
    int screenHeight;
    //grideView适配器
    GridViewImageAdapter adapter;
    TextView textHead;
    EditText editText_qidian, editText_zhongdian, editText_ren, editText_dianhua, editText_chexing, editText_mass;
    //以下为借口部分数据
    private List<Map<String, Object>> listGridTop, listGridCenter;
    String ures;
    public static final String NameSpace = "http://info.service.zhidisoft.com";  //命名空间
    public static final String MethodNameTop = "carPoolingInfoSave";//方法名
    public static final String URL = "http://120.27.5.22:8080/services/carPoolingInfoService";//地址
    public int mun = 10;
    private List<Map<String, Object>> listRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carpooling__details__main);
        init();
        intent_uesr = getIntent();
        userId = intent_uesr.getStringExtra("user_id");
        comIds = intent_uesr.getStringExtra("CommunityCode");
        //设置显示的图片
        pathlist.add(image + "");
        saveImagePath(pathlist);
        adapter = new GridViewImageAdapter(this, this, pathlist, screenWidth);
        gridView = (GridView) findViewById(R.id.grideView_carPooling);
        gridView.setAdapter(adapter);
        //点击事件,如果是最后一个开启pop,如果不是跳转activity
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == pathlist.size() - 1) {
                    showPopWindow();
                } else {
                    Intent intent = new Intent(Carpooling_Details_Main.this, PictureViewPagerActivity.class);
                    intent.putStringArrayListExtra("path", (ArrayList<String>) pathlist);
                    intent.putExtra("position", position);
                    startActivity(intent);
                }
            }
        });
        textView = (TextView) findViewById(R.id.carpooling_details_tv_time);
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

    public void car_ll(View v) {
        start = editText_qidian.getText().toString();
        end = editText_zhongdian.getText().toString();
        carInfo = editText_chexing.getText().toString();
        startTime = editText_shijian.getText().toString();
        content = editText_mass.getText().toString();
        contact = editText_ren.getText().toString();
        contactPhone = editText_dianhua.getText().toString();
        infoType="zck";
        tempFileIds = "";
        if (start.equals("") || end.equals("") || carInfo.equals("") || startTime.equals("")
                || contactPhone.equals("")) {
            Toast.makeText(this, "请输入完整信息", Toast.LENGTH_SHORT).show();
            return;
        }
        GetRequestMessageTop();
//        finish();
    }


    @Override
    public void init() {
        super.init();
        //获取屏幕宽高
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        editText_qidian = (EditText) findViewById(R.id.car_qidian_1);
        editText_zhongdian = (EditText) findViewById(R.id.car_zhongdian_1);
        editText_shijian = (TextView) findViewById(R.id.carpooling_details_tv_time);
        editText_dianhua = (EditText) findViewById(R.id.car_dianhua_1);
        editText_chexing = (EditText) findViewById(R.id.car_chexing_1);
        editText_ren = (EditText) findViewById(R.id.car_lianxiren_1);
        editText_mass = (EditText) findViewById(R.id.car_miaoshu_1);

    }

    public void carpooling_details_ll(View v) {
        finish();
    }

    protected void showDatePickDlg() {
        Calendar calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(Carpooling_Details_Main.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                time = year + "-" + monthOfYear + "-" + dayOfMonth;
                Carpooling_Details_Main.this.textView.setText(year + "-" + monthOfYear + 1 + "-" + dayOfMonth);
                showtimePickDlg();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    protected void showtimePickDlg() {
        Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(Carpooling_Details_Main.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Carpooling_Details_Main.this.textView.setText(time + " " + hourOfDay + ":" + minute + ":00");
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }

    @Override
    public void sendMessage(int position) {

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (popwindow != null) {
            popwindow.dismiss();
        }
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
            Toast.makeText(this, "拍照不要超过5张", Toast.LENGTH_SHORT).show();
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
    //以下为接口数据部分

    private void GetRequestMessageTop() {
        List<String> list = new ArrayList<>();
        List<Object> value = new ArrayList<>();
        String[] fileNames = new String[pathlist.size() - 1];
        File[] files = new File[pathlist.size() - 1];
        for (int i = 0; i < pathlist.size() - 1; i++) {
            files[i] = new File(pathlist.get(i));
            fileNames[i] = files[i].getName();
        }
        list.add("id");
        list.add("start");
        list.add("end");
        list.add("infoType");
        list.add("carInfo");
        list.add("startTime");
        list.add("content");
        list.add("contact");
        list.add("contactPhone");
        list.add("comIds");
        list.add("userId");
        value.add("");
        value.add(start);
        value.add(end);
        value.add(infoType);
        value.add(carInfo);
        value.add(startTime);
        value.add(content);
        value.add(contact);
        value.add(contactPhone);
        value.add(comIds);
        value.add(userId);
        Map<String, String> map = new HashMap<>();
        map.put("id", "");
        map.put("start", start);
        map.put("end", end);
        map.put("infoType", infoType);
        map.put("carInfo", carInfo);
        map.put("startTime", startTime);
        map.put("content", content);
        map.put("contact", contact);
        map.put("contactPhone", contactPhone);
        map.put("comIds", comIds);
        map.put("userId", userId);
        map.put("tempFileIds", "");
        if (pathlist.size() == 1) {
            final WebSerVieceUtil web = new WebSerVieceUtil("http://info.service.zhidisoft.com", "carPoolingInfoSave", "carPoolingInfoService", map);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String s = web.GetStringMessage();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Carpooling_Details_Main.this, "发布成功", Toast.LENGTH_SHORT).show();
                                showMyDialog();
                            }
                        });
                    } catch (Exception e) {
                        Toast.makeText(Carpooling_Details_Main.this, "没有网络", Toast.LENGTH_SHORT).show();
                    }
                }
            }).start();

        } else {
            //访问时弹出dialog对话框
            progress=new ProgressDialogUtils(this);
            progress.show();
            UpLoadUtils.getNetData("http://120.27.5.22:8080/services/carPoolingInfoService",
                    "http://info.service.zhidisoft.com", "carPoolingInfoSave", true, new UpLoadUtils.OnRequestListener() {
                        @Override
                        public void successed(SoapObject data) {
                            Log.e("isSuccess", data.getProperty(0).toString());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Carpooling_Details_Main.this, "发布成功(包含图片)", Toast.LENGTH_SHORT).show();
                                    progress.dismiss();
                                    showMyDialog();
                                }
                            });
                        }

                        @Override
                        public void failed(String str) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Carpooling_Details_Main.this, "发布失败，请检查网络设置s", Toast.LENGTH_SHORT).show();
                                    ProgressDialogUtils.dismissProgressDialog();
                                }
                            });
                        }
                    }, list, value, files, fileNames);
        }
    }

    private void showMyDialog() {
        setDialogView();
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


                Toast.makeText(Carpooling_Details_Main.this, msg, Toast.LENGTH_SHORT).show();
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

}
