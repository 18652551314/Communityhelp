package com.example.administrator.communityhelp.my_other;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.administrator.communityhelp.BaseActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/1/7.
 */
public class CameraTakeCut extends BaseActivity {
    public static final int CUT_OK = 1100;
    public static int CUTTIME = 0;
    public static Bitmap photocut = null;
    public static File photofile = null;//有文件的File
    public static int  CameraRequestCode;
    public static int CameraResultCode;
    public static Intent StartCutIntent = new Intent("com.android.camera.action.CROP");
    public static Intent CameraData;
    String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static Bitmap getTakePic(Context context, int resultCode, Intent data) {
        Bitmap bitmap = null;
        FileOutputStream b;
        if (resultCode == Activity.RESULT_OK) {
            String sdStatus = Environment.getExternalStorageState();
            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                Log.i("TestFile",
                        "SD card is not avaiable/writeable right now.");
                return null;
            }
            String name = new SimpleDateFormat("yyyy_MM_ddhhmmss").format(new Date()) + ".jpg";
            Toast.makeText(context, name, Toast.LENGTH_LONG).show();
            Bundle bundle = data.getExtras();
            bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
            b = null;
            //???????????????????????????????为什么不能直接保存在系统相册位置呢？？？？？？？？？？？？
            File file = new File(Environment.getExternalStorageDirectory() + "/ImageSelector/Pictures/");
            file.mkdirs();// 创建文件夹
            String fileName = Environment.getExternalStorageDirectory() + "/ImageSelector/Pictures/" + name;
            File file2 = new File(Environment.getExternalStorageDirectory() + "/ImageSelector/Pictures/" + name);
            try {
                b = new FileOutputStream(file2);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    b.flush();
                    b.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }

    public static String getTakePicPath(Context context, int resultCode, Intent data) {
        Bitmap bitmap = null;
        String fileName = null;
        FileOutputStream b;
        if (resultCode == Activity.RESULT_OK) {
            String sdStatus = Environment.getExternalStorageState();
            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                Log.i("TestFile",
                        "SD card is not avaiable/writeable right now.");
                return null;
            }
            String name = new SimpleDateFormat("yyyy_MM_ddhhmmss").format(new Date()) + ".jpg";
            Toast.makeText(context, name, Toast.LENGTH_LONG).show();
            Bundle bundle = data.getExtras();
            bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
            b = null;
            //???????????????????????????????为什么不能直接保存在系统相册位置呢？？？？？？？？？？？？
            File file = new File(Environment.getExternalStorageDirectory() + "/ImageSelector/Pictures/");
            file.mkdirs();// 创建文件夹
            fileName = Environment.getExternalStorageDirectory() + "/ImageSelector/Pictures/" + name;
            File file2 = new File(Environment.getExternalStorageDirectory() + "/ImageSelector/Pictures/" + name);
            try {
                b = new FileOutputStream(file2);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    b.flush();
                    b.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return fileName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!hasPermission(permission)) {
            hasRequse(1, permission);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    public  void cutPic(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        Bitmap bitmap = null;//拍到的照片
        FileOutputStream b = null;
        photocut = null;//裁剪后的照片
        if (resultCode == Activity.RESULT_OK) {
            String sdStatus = Environment.getExternalStorageState();
            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                Log.i("TestFile",
                        "SD card is not avaiable/writeable right now.");
                return;
            }
            String name = new SimpleDateFormat("yyyy_MM_ddhhmmss").format(new Date()) + ".jpg";
            Bundle bundle = data.getExtras();
            bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
            b = null;
            //???????????????????????????????为什么不能直接保存在系统相册位置呢？？？？？？？？？？？？
            File file = new File(Environment.getExternalStorageDirectory() + "/ImageSelector/Pictures/");
            file.mkdirs();// 创建文件夹
            String fileName = Environment.getExternalStorageDirectory() + "/ImageSelector/Pictures/" + name;
            photofile = new File(Environment.getExternalStorageDirectory() + "/ImageSelector/Pictures/" + name);
            try {
                b = new FileOutputStream(photofile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    b.flush();
                    b.close();
                    if (photofile.exists() && CUTTIME == 0) {
                        CUTTIME++;
                        clipPhoto(Uri.fromFile(photofile));//开始裁减图片
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == CUT_OK) {
            if (data != null) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    photocut = extras.getParcelable("data");
                    try {
                        b = new FileOutputStream(photofile);
                        photocut.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            b.flush();
                            b.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }


    public static Bitmap getCutPic() {
        return photocut;
    }

    public static String getCutPicPath() {
        return photofile.getPath();
    }

    //裁剪图片的方法
    public void clipPhoto(Uri uri) {
        StartCutIntent.setDataAndType(uri, "image/*");
        // 下面这个crop = true是设置在开启的Intent中设置显示的VIEW可裁剪
        StartCutIntent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例，这里设置的是正方形（长宽比为1:1）
        StartCutIntent.putExtra("aspectX", 1);
        StartCutIntent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        StartCutIntent.putExtra("outputX", 200);
        StartCutIntent.putExtra("outputY", 200);
        StartCutIntent.putExtra("return-data", true);
        startActivityForResult(StartCutIntent, CUT_OK);
    }



}
