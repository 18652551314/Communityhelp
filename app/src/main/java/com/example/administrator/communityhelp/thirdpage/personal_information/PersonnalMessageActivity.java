package com.example.administrator.communityhelp.thirdpage.personal_information;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.communityhelp.BaseActivity;
import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.UpLoadUtils;
import com.example.administrator.communityhelp.firstpage.linliquan.look_for_sth.Look_For_Sth_Details_Main;
import com.example.administrator.communityhelp.my_other.CameraTakeCut;
import com.example.administrator.communityhelp.my_other.CircularImage;
import com.example.administrator.communityhelp.my_other.GlideLoader;
import com.example.administrator.communityhelp.my_other.PictureCut;
import com.example.administrator.communityhelp.thirdpage.UserMessage;
import com.example.administrator.communityhelp.thirdpage.about_us.WebSerVieceUtil;
import com.example.administrator.communityhelp.webserviceutils.ProgressDialogUtils;
import com.google.gson.Gson;
import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorActivity;

import org.kobjects.base64.Base64;
import org.ksoap2.serialization.SoapObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonnalMessageActivity extends CameraTakeCut {
    ProgressDialogUtils progress;
    //网络请求的返回数据
    String jsonStr;
    //图片裁剪的管理者
    CameraTakeCut cutManager = new CameraTakeCut();
    LinearLayout linearLayoutBack;
    CircularImage imageView_head_Personnal;
    TextView textView_name_Personnal;
    TextView textView_telNum_Personnal;
    PopupWindow popwindow;
    Button btn_take_picture;
    Button btn_select_picture;
    Button btn_pic_cancel;
    Button btn_login;
    //存放图片地址的集合
    List<String> pathlist = new ArrayList<>();
    //屏幕宽高
    int screenWidth;
    int screenHeight;
    //读写权限
    String permission[] = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    //修改密码的点击对象
    RelativeLayout passwordLayout;
    //文件选择返回码
    final int FILE_SELECT_CODE = 2100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personnal_message);
        progress=new ProgressDialogUtils(this);
        //获取屏幕宽高
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        //获取权限；
        getPermisson();
        init();
        //加载头像
        setHeadPicture();
        linearLayoutBack = (LinearLayout) findViewById(R.id.back_layout);
        //返回键点击事件
        linearLayoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //头像点击事件
        imageView_head_Personnal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopWindow();
            }
        });
        //昵称点击事件
        textView_name_Personnal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonnalMessageActivity.this, RenameMyCallingActivity.class);
                startActivity(intent);
            }
        });
        //密码修改点击事件
        passwordLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonnalMessageActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
        //登录事件
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoad()) {
                    btn_login.setText("退出登录");
                    //清空所有数据
                    dbweiter.delete("user", null, null);
                    dbweiter.delete("userMessage", null, null);
                    finish();
                } else {
                }
            }
        });
    }

    @Override
    public void init() {
        super.init();
        imageView_head_Personnal = (CircularImage) findViewById(R.id.imageView_head_Personnal);
        textView_name_Personnal = (TextView) findViewById(R.id.textView_name_Personnal);
        textView_telNum_Personnal = (TextView) findViewById(R.id.textView_phoneNum_Personnal);
        passwordLayout = (RelativeLayout) findViewById(R.id.password_layout);
        btn_login = (Button) findViewById(R.id.btn_outLogin);
    }

    public void setHeadPicture() {
        imageView_head_Personnal.setMaxWidth(screenWidth * 2 / 7);
        imageView_head_Personnal.setMaxHeight(screenWidth * 2 / 7);
        //获取用户登录基本网络数据
        Map<String, String> map = new HashMap();
        map.put("userId", getUserData().getUserId());
        final WebSerVieceUtil webSerVieceUtil = new WebSerVieceUtil("http://member.service.zhidisoft.com", "memberInfo", "memberService", map);
//设置从网络获取的头像
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    jsonStr = webSerVieceUtil.GetStringMessage();
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PersonnalMessageActivity.this, "没有网络", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                Gson gson = new Gson();
                UserMessage userMessage = new UserMessage();
                userMessage = gson.fromJson(jsonStr, UserMessage.class);
                Log.e("cat", userMessage.getHeadImg());
                final Bitmap bitmap1 = BitmapFactory.decodeFile(userMessage.getHeadImg());
                final UserMessage finalUserMessage = userMessage;
                imageView_head_Personnal.post(new Runnable() {
                    @Override
                    public void run() {
                        //等待
                        new GlideLoader().displayImage(PersonnalMessageActivity.this, finalUserMessage.getHeadImg(), imageView_head_Personnal);
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (popwindow != null) {
            popwindow.dismiss();
        }
    }


    private void showPopWindow() {
//        //设置背景透明度
//        WindowManager.LayoutParams params=PersonnalMessageActivity.this.getWindow().getAttributes();
//        params.alpha=0.7f;
//        PersonnalMessageActivity.this.getWindow().setAttributes(params);
        //开启popwindow
        View view = View.inflate(this, R.layout.popwindow_personnal_item, null);
        popwindow = new PopupWindow(view);
        popwindow.setFocusable(true);
        popwindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popwindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        btn_take_picture = (Button) view.findViewById(R.id.btn_take_picture);
        btn_select_picture = (Button) view.findViewById(R.id.btn_select_from_loacal);
        btn_pic_cancel = (Button) view.findViewById(R.id.btn_pic_cancel);
        popwindow.setAnimationStyle(R.style.PicPopupAnimation);
        popwindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        //点及其他地方消失
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.head_pop);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popwindow.dismiss();
            }
        });

        //拍照点击事件
        btn_take_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasPermission(permission)) {
                    //初始化裁剪状态
                    cutManager.CUTTIME = 0;
                    takePicture();
                } else {
                    getPermisson();
                }

            }
        });
        //选择照片事件
        btn_select_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasPermission(permission)) {
                    showFileChooser();
//                    selectPicture();
                } else {
                    getPermisson();
                }
            }
        });
        btn_pic_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popwindow.dismiss();
            }
        });
    }
    // 打开文件选择器

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //设置选择文件的类型（*/*）表示没有限制
//        intent.setType("*/*");
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
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
                .mutiSelectMaxSize(1)
                // 开启拍照功能 （默认关闭）
//                .showCamera()
                // 已选择的图片路径
                .pathList((ArrayList<String>) pathlist)
                // 拍照后存放的图片路径（默认 /temp/picture） （会自动创建）
                .filePath("/ImageSelector/Pictures")
                .build();
        ImageSelector.open(this, imageConfig);   // 开启图片选择器

    }

    //拍照并且切割的方法
    private void takePicture() {
        pathlist.clear();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }

    //以下为选择图片并进行处理的功能，写在onActivityResult方法中
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImageSelector.IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Get Image Path List
            pathlist = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
//            for (String path : pathlist) {
//                Log.i("ImagePathList", path);
//            }
            //设置头像
            setImageView();
        }
        //还未检测
        if (requestCode == 1) {
            cutPic(requestCode, resultCode, data);
        }
        if (requestCode == CUT_OK && data != null) {
            String path = photofile.getPath();
            if (path.equals("") | path == null) {
            } else {
                pathlist.clear();
                pathlist.add(path);
            }
            //设置头像
            setImageView();
        } else if (requestCode == CUT_OK && data == null) {
            photofile.delete();
        }
        if (requestCode == FILE_SELECT_CODE) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                String path = FileUtils.getPath(this, uri);
                pathlist.clear();
                pathlist.add(path);
                setImageView();
            }
        }
        try {
            new GlideLoader().displayImage(this, pathlist.get(0), imageView_head_Personnal);
        } catch (Exception e) {
            Toast.makeText(this, "没有选择照片", Toast.LENGTH_LONG).show();
        }
    }

    public void SendImageMessage(final String path) {
//        List list = new ArrayList();
//        List value = new ArrayList();
//        File files[] = new File[1];
//        File file = new File(path);
//        files[0] = file;
//        String fileNames[] = new String[1];
//        fileNames[0] = file.getName();
//        list.add("userId");
//        list.add("fileName");
//        value.add(fileNames[0]);
//        value.add(getUserData().getUserId());
//        //访问时弹出dialog对话框
//       progress.show();
//        UpLoadUtils.getNetData("http://120.27.5.22:8080/services/memberService",
//                "http://member.service.zhidisoft.com", "uploadHead", true, new UpLoadUtils.OnRequestListener() {
//                    @Override
//                    public void successed(SoapObject data) {
//                        Log.e("isSuccess", data.getProperty(0).toString());
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                progress.dismiss();
//                                new GlideLoader().displayImage(PersonnalMessageActivity.this,path,imageView_head_Personnal);
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void failed(String str) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(PersonnalMessageActivity.this,"设置失败，请检查网络",Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//                }, list, value, files, fileNames);


        progress = new ProgressDialogUtils(this);
        try{  progress.show();}catch (Exception e){e.printStackTrace();}
        Map<String, String> map = new HashMap();
        map.put("userId", getUserData().getUserId());
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        String newpath = PictureCut.saveBitmapToSdcard(PictureCut.ChangeImgeSize(path, 400, 400));
        map.put("fileName", newpath);
        Bitmap maps = BitmapFactory.decodeFile(newpath);
        String PicBase64 = null;
        try {
            PicBase64 = new String(Base64.encode(UpLoadUtils.getThumbUploadPath(path, 480)));
        } catch (Exception e) {
            e.printStackTrace();
        }
//        String PicBase64 = PictureCut.imgToBase64(newpath, maps, "JPEG");
        map.put("file", PicBase64);
        final WebSerVieceUtil webSerVieceUtil = new WebSerVieceUtil("http://member.service.zhidisoft.com", "uploadHead", "memberService", map);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String jsonStr = webSerVieceUtil.GetStringMessage();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new GlideLoader().displayImage(PersonnalMessageActivity.this, path, imageView_head_Personnal);
                            try{ progress.dismiss();}catch (Exception e1){}
                        }
                    });
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PersonnalMessageActivity.this, "没有网络", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
//                Log.e("dog", jsonStr);
            }
        }).start();
    }

    public void getPermisson() {
        if (!hasPermission(permission)) {
            hasRequse(1, permission);
        }
    }

    public void setImageView() {
        Bitmap bitmap = BitmapFactory.decodeFile(pathlist.get(0));
        //把选择的照片进行压缩切成正方形再切成圆形
//        Log.e("cat", pathlist.get(0));
        Bitmap newBitmap = PictureCut.toRoundCorner(PictureCut.ImageCropSquere(PictureCut.getLittleImg(bitmap, 30)), 2);
        //预留发送到服务器的方法
        SendImageMessage(pathlist.get(0));
        setHeadPicture();
        new GlideLoader().displayImage(this, pathlist.get(0), imageView_head_Personnal);
//        imageView_head_Personnal.setImageBitmap(newBitmap);
    }

    private void setAll() {
        setHeadPicture();
        Map<String, String> map = new HashMap<>();
        map.put("userId", getUserData().getUserId());
        final WebSerVieceUtil webSerVieceUtil = new WebSerVieceUtil("http://member.service.zhidisoft.com", "memberInfo", "memberService", map);
//设置从网络获取的头像
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    jsonStr = null;
                    jsonStr = webSerVieceUtil.GetStringMessage();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                    Gson gson = new Gson();
                    UserMessage userMessage = new UserMessage();
                    userMessage = gson.fromJson(jsonStr, UserMessage.class);
//                Log.e("cat", userMessage.getHeadImg());
                    final Bitmap bitmap1 = BitmapFactory.decodeFile(userMessage.getHeadImg());
                    final UserMessage finalUserMessage = userMessage;
                    textView_name_Personnal.post(new Runnable() {
                        @Override
                        public void run() {
                            textView_name_Personnal.setText(finalUserMessage.getNickName());
                        }
                    });
                    textView_telNum_Personnal.post(new Runnable() {
                        @Override
                        public void run() {
                            textView_telNum_Personnal.setText(finalUserMessage.getAccount());
                        }
                    });
                    new GlideLoader().displayImage(PersonnalMessageActivity.this, userMessage.getHeadImg(), imageView_head_Personnal);
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            Toast.makeText(PersonnalMessageActivity.this, "没有网络", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
            }
        }).start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setAll();
    }


    public static class FileUtils {
        public static String getPath(Context context, Uri uri) {

            if ("content".equalsIgnoreCase(uri.getScheme())) {
                String[] projection = {"_data"};
                Cursor cursor = null;

                try {
                    cursor = context.getContentResolver().query(uri, projection, null, null, null);
                    int column_index = cursor.getColumnIndexOrThrow("_data");
                    if (cursor.moveToFirst()) {
                        return cursor.getString(column_index);
                    }
                } catch (Exception e) {
                    // Eat it
                }
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }

            return null;
        }
    }


}

