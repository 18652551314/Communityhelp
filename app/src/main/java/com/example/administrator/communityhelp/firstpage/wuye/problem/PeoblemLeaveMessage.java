package com.example.administrator.communityhelp.firstpage.wuye.problem;


import android.Manifest;
import android.app.Dialog;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.internal.NavigationMenu;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.communityhelp.BaseActivity;
import com.example.administrator.communityhelp.Login_Main;
import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.UpLoadUtils;
import com.example.administrator.communityhelp.my_other.CameraTakeCut;
import com.example.administrator.communityhelp.my_other.GlideLoader;
import com.example.administrator.communityhelp.thirdpage.UserMessage;
import com.example.administrator.communityhelp.thirdpage.publication.bbs.AdapterCallBack;
import com.example.administrator.communityhelp.thirdpage.publication.bbs.GridViewImageAdapter;
import com.example.administrator.communityhelp.thirdpage.publication.bbs.PictureViewPagerActivity;
import com.example.administrator.communityhelp.webserviceutils.ProgressDialogUtils;
import com.example.administrator.communityhelp.webserviceutils.WebServiceUtils;
import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


/**
 * Created by Administrator on 2017/1/5.
 */

public class PeoblemLeaveMessage extends CameraTakeCut implements View.OnClickListener, AdapterCallBack {
    private Button btn_recoder;
    private EditText content;
    private TextView telText;
    private String telephone;
    private MediaRecorder recorder;
    private boolean isRecording;
    private File file;
    private File newFile;
    private long start, end;
    private ImageView image_voice;
    private ImageView image_delete;
    private TextView text_duration;
    private MediaPlayer player;
    private AnimationDrawable ad;
    private Dialog dialog;
    public static String URL="http://120.27.5.22:8080/services/problemService";
    public static String NAMESPACE="http://problem.service.zhidisoft.com";
    public static String METHODNAME="PropertyFeedBackSave";
    private List<File> fileList=new ArrayList<>();
    private ProgressDialogUtils progress;


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

    //屏幕宽高
    int screenWidth;
    int screenHeight;
    //grideView适配器
    ProblemGridViewImageAdapter adapter;
    String permission[] = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO};

    @Override
    public void init() {
        super.init();
        if (!hasPermission(permission)) {
            hasRequse(1, permission);
        }
        setContentView(R.layout.peoblem_leave_message);
        findViewById(R.id.iv_wuyeliuyan_dianhua).setOnClickListener(this);
        findViewById(R.id.wuye_take_photo).setOnClickListener(this);
        findViewById(R.id.wuye_tijiao).setOnClickListener(this);
        findViewById(R.id.wuyeliuyan1_back).setOnClickListener(this);
        telText= (TextView) findViewById(R.id.textview_wuyedianhua2);
        HashMap<String,Object> map=new HashMap<>();
        String communityCode=getUserData().getCommunityCode();
        if(communityCode==null){
            Cursor cursor=dbReader.rawQuery("select * from community",null);
            while(cursor.moveToNext()){
                communityCode=cursor.getString(cursor.getColumnIndex("communityCode"));
            }
        }
        String userId=getUserData().getUserId();
        if(userId==null){
            userId="";
        }
        map.put("communityCode",communityCode);
        map.put("userId",userId);
        WebServiceUtils.callWebService(URL, "propertyContactNum", NAMESPACE, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(SoapObject result) {
                if(result!=null){
                    String json=result.getProperty(0).toString();
                    try {
                        JSONObject object=new JSONObject(json);
                        telephone=object.getString("contactNum");
                        telText.setText(telephone);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        content= (EditText) findViewById(R.id.pingtailiuyan_editext);
        image_voice = (ImageView) findViewById(R.id.image_voice);
        image_voice.setOnClickListener(this);
        image_delete = (ImageView) findViewById(R.id.image_delete);
        image_delete.setOnClickListener(this);
        text_duration = (TextView) findViewById(R.id.text_recordertime);
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "没有SD卡", Toast.LENGTH_SHORT).show();
            return;
        }
        file = new File(Environment.getExternalStorageDirectory() + "/recoder");
        if (!file.exists()) {
            file.mkdir();
        }
        file.deleteOnExit();
        btn_recoder = (Button) findViewById(R.id.wuye_press_talk);
        btn_recoder.setClickable(true);

        btn_recoder.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btn_recoder.setText("松开 结束");
                        btn_recoder.setFocusable(false);
                        if (!isRecording) {
                            View view = getLayoutInflater().inflate(R.layout.recoder_voice_animation, null);
                            ImageView image_recorder = (ImageView) view.findViewById(R.id.image_recorder);
                            image_recorder.setImageResource(R.drawable.voice_recording);
                            ad = (AnimationDrawable) image_recorder.getDrawable();
                            ad.start();
                            dialog = new Dialog(PeoblemLeaveMessage.this, R.style.dialog);
                            Window dialogWindow = dialog.getWindow();
                            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                            dialogWindow.setGravity(Gravity.TOP);
                            lp.x = 50; // 新位置X坐标
                            lp.y = 200; // 新位置Y坐标
                            lp.width = 200; // 宽度
                            lp.height = 200; // 高度
                            lp.alpha = 0.6f; // 透明度

                            // 当Window的Attributes改变时系统会调用此函数,可以直接调用以应用上面对窗口参数的更改,也可以用setAttributes
                            // dialog.onWindowAttributesChanged(lp);
                            dialogWindow.setAttributes(lp);
                            dialog.setContentView(view);
                            dialog.show();
                            recorder = getInstance();
                            recorder.setMaxDuration(60000);
                            //调用MediaRecorder的start()与stop()间隔不能小于1秒(有时候大于1秒也崩)，否则必崩。
                            recorder.setOnErrorListener(null);
                            recorder.setOnInfoListener(null);
                            //resert后recoder会回到设置状态，需重新设置
                            newFile = new File(file, "/message" + ".amr");
                            recorder.setOutputFile(newFile.getPath());
                            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 设置MediaRecorder的音频源为麦克风
                            //在线播放3gp格式player低版本会出错，故用amr
                            recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);// 设置MediaRecorder录制的音频格式
                            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);// 设置MediaRecorder录制音频的编码为amr
                            try {
                                recorder.prepare();
                                recorder.start();
                                Date dStart = new Date();
                                start = dStart.getTime();
                                isRecording = true;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        return false;
                    case MotionEvent.ACTION_MOVE:
                        if (isRecording) {
                            if (!(event.getX() > 0 && event.getX() <= v.getWidth() && event.getY() > 0 && event.getY() <= v.getHeight())) {
                                Date dEnd = new Date();
                                end = dEnd.getTime();
                                if (end - start < 100) {
                                    btn_recoder.setText("按住 说话");
                                    btn_recoder.setFocusable(true);
                                    dialog.dismiss();
                                    ad.stop();
                                    //不用resert
                                    return false;
                                }
                                if (end - start < 1000) {
                                    isRecording = false;
                                    btn_recoder.setText("按住 说话");
                                    btn_recoder.setFocusable(true);
                                    dialog.dismiss();
                                    ad.stop();
                                    recorder.reset();
                                } else {
                                    cancel(end - start);
                                }
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (isRecording) {
                            Date dEnd = new Date();
                            end = dEnd.getTime();
                            if (end - start < 100) {
                                btn_recoder.setText("按住 说话");
                                btn_recoder.setFocusable(true);
                                dialog.dismiss();
                                ad.stop();
                                return false;
                            }
                            if (end - start < 1000) {
                                isRecording = false;
                                btn_recoder.setText("按住 说话");
                                btn_recoder.setFocusable(true);
                                dialog.dismiss();
                                ad.stop();
                                recorder.reset();
                            } else {
                                cancel(end - start);
                            }
                        }
                }
                return true;
            }
        });

        //获取屏幕宽高
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        adapter = new ProblemGridViewImageAdapter(this, this, pathlist, screenWidth);
        gridView = (GridView) findViewById(R.id.grideView_wuye);
        gridView.setAdapter(adapter);
        //点击事件,如果是最后一个开启pop,如果不是跳转activity
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PeoblemLeaveMessage.this, PictureViewPagerActivity.class);
                intent.putStringArrayListExtra("path", (ArrayList<String>) pathlist);
                intent.putExtra("problem", "problem");
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });


    }

    public void cancel(long duration) {
        dialog.dismiss();
        ad.stop();
        ViewGroup.LayoutParams layoutParams = image_voice.getLayoutParams();
        layoutParams.width = 160 + (int) duration / 120;
        image_voice.setLayoutParams(layoutParams);
        image_voice.setVisibility(View.VISIBLE);
        image_delete.setVisibility(View.VISIBLE);
        text_duration.setVisibility(View.VISIBLE);
        text_duration.setText(duration / 1000 + "''");
        recorder.stop();
        recorder.release();
        recorder = null;//不为null会出异常
        isRecording = false;
        btn_recoder.setText("按住 说话");
        btn_recoder.setFocusable(true);
    }

    public MediaRecorder getInstance() {
        if (recorder == null) {
            return new MediaRecorder();
        } else {
            return recorder;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_wuyeliuyan_dianhua:
                if(!"".equals(telephone)){
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+telephone));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else{
                    Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    View view = getLayoutInflater().inflate(R.layout.toastview, null);
                    TextView toastText = (TextView) view.findViewById(R.id.toast_text);
                    toastText.setText("暂时没有提供物业电话！");
                    toast.setView(view);
                    toast.show();
                }
                break;
            case R.id.wuye_take_photo:
                showPopWindow();
                break;
            case R.id.image_voice:
                final Handler handler = new Handler();
                image_voice.setImageResource(R.drawable.voice_play);
                ad = (AnimationDrawable) image_voice.getDrawable();
                ad.start();//涉及到UI线程的改变，在200ms持续期间，走到while(true),导致阻塞，动画不执行，阻塞结束，start和stop又几乎同时执行
                try {
                    player = new MediaPlayer();
                    player.setDataSource(newFile.getPath());
                    player.prepare();
                    player.start();
//                    while(true){
//                        flag=player.isPlaying();
//                        if(flag==false){
//                            ad.stop();
//                            image_voice.setImageResource(R.mipmap.chatfrom_voice_playing);
//                            break;
//                        }
//                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            boolean flag = false;
                            while (true) {
                                flag = player.isPlaying();
                                if (!flag) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            ad.stop();
                                            image_voice.setImageResource(R.mipmap.chatfrom_voice_playing);
                                        }
                                    });
                                    break;
                                }
                            }
                        }
                    }).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.image_delete:
                newFile.delete();
                image_voice.setVisibility(View.GONE);
                image_delete.setVisibility(View.GONE);
                text_duration.setVisibility(View.GONE);
                break;
            case R.id.wuye_tijiao:
                if(!isLoad()){
                    Intent intent=new Intent(this, Login_Main.class);
                    startActivity(intent);
                    break;
                }
                //这里上传服务器
                List<String> list=new ArrayList<>();
                List<Object> values=new ArrayList<>();
                list.add("title");
                list.add("content");
                list.add("contact");
                list.add("contactPhone");
                list.add("communityCode");
                list.add("vflength");
                list.add("userId");
                values.add("");
                values.add(content.getText().toString());
                values.add(getUserMessage().getNickName());
                values.add(getUserData().getUserName());
                values.add(getUserData().getCommunityCode());
                values.add(String.valueOf((end-start)/1000));
                values.add(getUserData().getUserId());
                if(newFile!=null){
                    fileList.add(newFile);
                }
                if(pathlist.size()!=0){
                    for(int i=0;i<pathlist.size();i++){
                        File file=new File(pathlist.get(i));
                        fileList.add(file);
                    }
                }
                File[] files=null;
                String[] fileName=null;
                if(fileList.size()!=0){
                    progress=new ProgressDialogUtils(this);
                    progress.show();
                    files=new File[fileList.size()];
                    fileName=new String[fileList.size()];
                    for(int i=0;i<fileList.size();i++){
                        files[i]=fileList.get(i);
                        fileName[i]=fileList.get(i).getName();
                    }
                    UpLoadUtils.getNetData(URL, NAMESPACE, METHODNAME, true, new UpLoadUtils.OnRequestListener() {
                        @Override
                        public void successed(SoapObject data) {
                            progress.dismiss();
                            String json=data.getProperty(0).toString();
                            Log.e("tag",json);
                            dataClear();
                        }

                        @Override
                        public void failed(String str) {
                            Log.e("tag",str);
                        }
                    },list,values,files,fileName);
                }else{
                    progress=new ProgressDialogUtils(this);
                    progress.show();
                    HashMap<String,Object> map=new HashMap<>();
                    map.put("title","");
                    map.put("content",content.getText().toString());
                    map.put("contact",getUserMessage().getNickName());
                    map.put("contactPhone",getUserData().getUserName());
                    map.put("communityCode",getUserData().getCommunityCode());
                    map.put("tempFileIds","");
                    map.put("vflength","");
                    map.put("userId",getUserData().getUserId());
                    WebServiceUtils.callWebService(URL, METHODNAME, NAMESPACE, map, new WebServiceUtils.WebServiceCallBack() {
                        @Override
                        public void callBack(SoapObject result) {
                            progress.dismiss();
                            if(result!=null){
                                String json=result.getProperty(0).toString();
                                Log.e("tag",json);
                                dataClear();
                            }
                        }
                    });
                }
                break;
            case R.id.wuyeliuyan1_back:
                finish();
                break;
        }
    }

    private void dataClear() {
        content.setText("");
        if(newFile!=null){
            newFile.delete();
            image_voice.setVisibility(View.GONE);
            image_delete.setVisibility(View.GONE);
            text_duration.setVisibility(View.GONE);
        }
        if(adapter!=null){
            pathlist.clear();
            adapter.notifyDataSetChanged();
        }
        Toast.makeText(this,"提交成功",Toast.LENGTH_SHORT).show();
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.setPathlist(pathlist);
                adapter.notifyDataSetChanged();
            }
        });
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

}
