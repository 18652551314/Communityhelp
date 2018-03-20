package com.example.administrator.communityhelp.thirdpage.publication.bbs;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.communityhelp.BaseActivity;
import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.UpLoadUtils;
import com.example.administrator.communityhelp.my_other.CameraTakeCut;
import com.example.administrator.communityhelp.my_other.GlideLoader;
import com.example.administrator.communityhelp.my_other.PictureCut;
import com.example.administrator.communityhelp.thirdpage.about_us.WebSerVieceUtil;
import com.example.administrator.communityhelp.webserviceutils.GetTempFileIdLunTan;
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

public class CommunityActivity extends BaseActivity implements AdapterCallBack, View.OnClickListener {
    ProgressDialogUtils progress;
    View view;
    AlertDialog dialog;
    LinearLayout layout_back, go_layout_CommunityBbs;
    TextView textView_send;
    EditText editText_title;
    EditText editText_dailog;
    //点击显示图片
    GridView gridView;
    //选择是照相机还是图库
    PopupWindow popwindow;
    //popwindow的点击对象
    TextView text_take_pic;
    //popwindow的点击对象
    TextView text_select_pic;
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
    //读写权限
    String permission[] = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    //表示最后一张的常量
    static final int LAST_PICTURE = -1;
    String tit, con, comIds, userId;
    //以下为借口部分数据
    private List<Map<String, Object>> listGridTop, listGridCenter;
    String ures;
    String msaage;
    public static final String NameSpace = "http://bbs.service.zhidisoft.com";  //命名空间
    public static final String MethodNameTop = "bbsSave";//方法名
    public static final String URL = "http://120.27.5.22:8080/services/bbsService";//地址
    public int mun = 10;
    private List<Map<String, Object>> listRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_bbs);
        progress = new ProgressDialogUtils(this);
        //获取屏幕宽高
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        getPermission();
        init();
        setClick();
        //设置显示的图片
        pathlist.add(image + "");
        saveImagePath(pathlist);
        adapter = new GridViewImageAdapter(this, this, pathlist, screenWidth);
        gridView.setAdapter(adapter);
        //点击事件,如果是最后一个开启pop,如果不是跳转activity
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == pathlist.size() - 1) {
                    showPopWindow();
                } else {
                    Intent intent = new Intent(CommunityActivity.this, PictureViewPagerActivity.class);
                    intent.putStringArrayListExtra("path", (ArrayList<String>) pathlist);
                    intent.putExtra("position", position);
                    intent.putExtra("class", "CommunityActivity");
                    startActivity(intent);
                }
            }
        });

    }

    private void setClick() {
        layout_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        go_layout_CommunityBbs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tit = editText_title.getText().toString();
                con = editText_dailog.getText().toString();
                if (tit.equals("")) {
                    toast("请输入标题");
                    return;
                }
                if (con.equals("")) {
                    toast("请输入内容");
                    return;
                }
                GetRequestMessageTop();
            }
        });
    }

    private void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void init() {
        go_layout_CommunityBbs = (LinearLayout) findViewById(R.id.go_layout_CommunityBbs);
        layout_back = (LinearLayout) findViewById(R.id.back_layout_CommunityBbs);
        textView_send = (TextView) findViewById(R.id.textView_sendMyBbs);
        editText_title = (EditText) findViewById(R.id.editext_BbsTitle);
        editText_dailog = (EditText) findViewById(R.id.editext_BbsDailog);
        gridView = (GridView) findViewById(R.id.grideView_Bbs_picture);

        userId = getUserData().getUserId();
        comIds = getUserData().getCommunityCode();
    }

    public void getPermission() {
        if (!hasPermission(permission)) {
            hasRequse(1, permission);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (popwindow != null) {
            popwindow.dismiss();
        }
    }

    //图片设置
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


    @Override
    public void sendMessage(int position) {
        if (position == LAST_PICTURE) {
            //跳出popwindow
            showPopWindow();
        } else {

        }
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
        try{ progress.show();}catch (Exception e){}
        List<String> list = new ArrayList<>();
        List<Object> value = new ArrayList<>();
        String[] fileNames = new String[pathlist.size() - 1];
        File[] files = new File[pathlist.size() - 1];
        for (int i = 0; i < pathlist.size() - 1; i++) {
            files[i] = new File(pathlist.get(i));
            fileNames[i] = files[i].getName();
        }
        list.add("title");
        list.add("content");
        list.add("comIds");
        list.add("userId");
        value.add(tit);
        value.add(con);
        value.add(comIds);
        value.add(userId);
        Map<String, String> map = new HashMap<>();
        map.put("title", tit);
        map.put("content", con);
        map.put("comIds", comIds);
        map.put("userId", userId);
        map.put("tempFileIds", "");
        if (pathlist.size() == 1) {
            final WebSerVieceUtil web = new WebSerVieceUtil("http://bbs.service.zhidisoft.com", "bbsSave", "bbsService", map);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String s = web.GetStringMessage();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progress.dismiss();
//                                Toast.makeText(CommunityActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                                showMyDialog();
                            }
                        });
                    } catch (Exception e) {
                        progress.dismiss();
                        Toast.makeText(CommunityActivity.this, "没有网络", Toast.LENGTH_SHORT).show();
                    }
                }
            }).start();

        } else {
            UpLoadUtils.getNetData("http://120.27.5.22:8080/services/bbsService",
                    "http://bbs.service.zhidisoft.com", "bbsSave", true, new UpLoadUtils.OnRequestListener() {
                        @Override
                        public void successed(SoapObject data) {
                            Log.e("isSuccess", data.getProperty(0).toString());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    Toast.makeText(CommunityActivity.this, "发布成功(包含图片)", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(CommunityActivity.this, "发布失败，请检查网络设置", Toast.LENGTH_SHORT).show();
                                   progress.dismiss();
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
            Log.e("json", "rse: " + result);
            String jjh = "[" + result + "]";
            JSONArray array = new JSONArray(jjh);
//            JSONObject object =
//            JSONArray array = object.getJSONArray("rows");new JSONObject(result);
            Log.e("json", "ffff: " + array);
            for (int i = 0; i < array.length(); i++) {
                Map<String, Object> map = new HashMap<>();
                JSONObject jo = array.getJSONObject(i);
                String result11 = jo.getString("result");
                String msg = jo.getString("msg");
                map.put("result11", result11);
                map.put("msg", msg);
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
    //    public void getfileId() {
//        pathlist.remove(pathlist.size() - 1);
//        String newPath = "";
//        for (int i = 0; i < pathlist.size(); i++) {
//            newPath = PictureCut.saveBitmapToSdcard(BitmapFactory.decodeFile(pathlist.get(i)));
//            final String finalNewPath = newPath;
//            String s = GetTempFileIdLunTan.upLoad(new File(newPath), finalNewPath);
//            if (tempFileIds.toString().equals("")) {
//                tempFileIds.append(s);
//            } else {
//                tempFileIds.append(";" + s);
//            }
//            Log.e("id", tempFileIds.toString());
//        }
//    }

}