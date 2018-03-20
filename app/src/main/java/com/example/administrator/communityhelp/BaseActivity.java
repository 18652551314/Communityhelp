package com.example.administrator.communityhelp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.communityhelp.mysql.SqlHelper;
import com.example.administrator.communityhelp.mysql.UserData;
import com.example.administrator.communityhelp.thirdpage.UserMessage;
import com.example.administrator.communityhelp.thirdpage.about_us.WebSerVieceUtil;
import com.example.administrator.communityhelp.thirdpage.personal_information.PersonnalMessageActivity;
import com.google.gson.Gson;
import com.zhy.autolayout.AutoLayoutActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/28.
 */
public class BaseActivity extends AutoLayoutActivity {
    LinearLayout layout_back, go_layout_CommunityBbs;
    public FileId fileId = null;
    public String tempFileId = null;
    PopupWindow popupWindow;
    View popupView;
    TextView text1;
    Button button_shezhi, button2_quxiao;
    public SQLiteDatabase dbweiter;
    public SQLiteDatabase dbReader;
    SqlHelper helper;
    public String jsonFileId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = new SqlHelper(this);
        dbReader = helper.getReadableDatabase();
        dbweiter = helper.getWritableDatabase();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();

    }

    public void init() {
    }

    public boolean hasPermission(String... permission) {
        for (String permissiom : permission) {
            if (ActivityCompat.checkSelfPermission(this, permissiom) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public void hasRequse(int code, String... permission) {
        ActivityCompat.requestPermissions(this, permission, Codes.PHONE_REQLIESTCODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Codes.PHONE_REQLIESTCODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
        }
    }

    //更改屏幕透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    //判断网络
    public boolean isNetworkAvailable(Activity activity) {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    System.out.println(i + "===状态===" + networkInfo[i].getState());
                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    //弹出警告框
    public void isweb(final View v) {
        if (isNetworkAvailable(this)) {
//            Toast.makeText(getApplicationContext(), "当前有可用网络！", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "当前没有可用网络！", Toast.LENGTH_LONG).show();
            backgroundAlpha(0.5f);
            popupView = this.getLayoutInflater().inflate(R.layout.popupview_wangluo, null);
            DisplayMetrics dm = new DisplayMetrics();
            //获取屏幕信息
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            int screenWidth = dm.widthPixels;
            int screenHeigh = dm.heightPixels;
            popupWindow = new PopupWindow(popupView, screenWidth*3/4, screenHeigh/4, true);
            button_shezhi = (Button) popupView.findViewById(R.id.wangluo_shezhi);
            button2_quxiao = (Button) popupView.findViewById(R.id.wangluo_quxiao);
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F8F8F8")));
            // TODO: 2016/5/17 设置可以获取焦点
            popupWindow.setFocusable(true);
            popupWindow.setAnimationStyle(R.style.AnimBottom);
            popupView.post(new Runnable() {
                @Override
                public void run() {
                    popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
                }
            });

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
            button_shezhi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                    startActivity(intent);
                    popupWindow.dismiss();
                }
            });
            button2_quxiao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
        }

    }


    //计算listview的高度 并设置
    public void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public UserData getUserData() {
        String select = "select * from user";
        Cursor cursor = dbReader.rawQuery(select, null);
        UserData data = new UserData();
        while (cursor.moveToNext()) {
            String communityCode = cursor.getString(cursor.getColumnIndex("communityCode"));
            String communityName = cursor.getString(cursor.getColumnIndex("communityName"));
            boolean state = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("state")));
            boolean isAdmin = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("isAdmin")));
            String userName = cursor.getString(cursor.getColumnIndex("userName"));
            String userId = cursor.getString(cursor.getColumnIndex("userId"));
            String uuid = cursor.getString(cursor.getColumnIndex("uuid"));
            String msg = cursor.getString(cursor.getColumnIndex("msg"));
            data.setCommunityCode(communityCode);
            data.setCommunityName(communityName);
            data.setIsAdmin(isAdmin);
            data.setState(state);
            data.setMsg(msg);
            data.setUserId(userId);
            data.setUserName(userName);
            data.setUuid(uuid);
        }
        return data;
    }

    public boolean isLoad() {
        return (getUserData().getUserId() != null) && (getUserData().getUserId() != "");
    }

    public void tranceLogin(Context context) {
        Intent intent = new Intent(context, Login_Main.class);
        startActivity(intent);
    }

    public void saveImagePath(List<String> pathList) {
        dbweiter.delete("imagePath", null, null);
        for (int i = 0; i < pathList.size(); i++) {
            ContentValues values = new ContentValues();
            values.put("imagePath", pathList.get(i));
            dbweiter.insert("imagePath", null, values);
        }
    }

    public String getBase64(String path) {
        File file = new File(path);
        FileInputStream inputFile = null;
        byte[] buffer = new byte[0];
        try {
            inputFile = new FileInputStream(file);
            buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
            inputFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.encodeToString(buffer, Base64.DEFAULT);
    }

    public UserMessage getUserMessage() {
        UserMessage userMessage = new UserMessage();
        Cursor cursor = dbReader.query("userMessage", null, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            userMessage.setHeadImg(cursor.getString(cursor.getColumnIndex("headImage")));
            userMessage.setNickName(cursor.getString(cursor.getColumnIndex("nickName")));
            userMessage.setAccount(cursor.getString(cursor.getColumnIndex("account")));
        }
        return userMessage;
    }

    public class FileId {
        /**
         * tempFileId : 402881ee500cb80a01500cea28e90003;402881ee500cb80a01500cea29280004;
         */
        private String tempFileId;

        public String getTempFileId() {
            return tempFileId;
        }

        public void setTempFileId(String tempFileId) {
            this.tempFileId = tempFileId;
        }
    }

}

