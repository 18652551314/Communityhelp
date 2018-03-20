package com.example.administrator.communityhelp.thirdpage.publication.bbs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.example.administrator.communityhelp.BaseActivity;
import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.firstpage.taoshihui.ShouHuoDiZhiActivity;
import com.example.administrator.communityhelp.my_other.BigPhotoViewPager;
import com.example.administrator.communityhelp.my_other.GlideLoader;
import com.example.administrator.communityhelp.my_other.PictureCut;
import com.example.administrator.communityhelp.webserviceutils.ProgressDialogUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PictureViewPagerActivity extends BaseActivity implements View.OnClickListener {
    boolean isPageChanged = false;
    int with, height, times;
    double scale;
    int currentWith, currentHeight;
    PhotoViewAttacher attacher;
    List<PhotoViewAttacher> attachers=new ArrayList<>();
    WebView webview;
    int control_state;
    PhotoView photoview;
    List<PhotoView> webViews = new ArrayList<>();
    ZoomControls zoomctrl;
    View view;
    AlertDialog dialog;
    BigPhotoViewPager viewPager;
    List<View> list;
    List<String> pathlist;
    List<ImageView> images;
    ImageView imageview_progress1, imageview_progress2, imageview_progress3,
            imageview_progress4, imageview_progress5;
    ImageView imageView_back;
    ImageView imageView_delete;
    Intent intent;
    int currentPosition;
    PictureViewPagerAdapter adapter;
    int image = R.drawable.pic_jia;
    DisplayMetrics dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_view_pager);
        init();
        //获取图片资源,装入list<View>
        getImage();
        //获取屏幕宽高
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        with = dm.widthPixels;
        height = dm.heightPixels;
        zoomctrl.hide();
        currentPosition = intent.getIntExtra("position", 0);
        //设置背景点
        setPointCount(currentPosition);
        adapter = new PictureViewPagerAdapter(list);
        viewPager.setAdapter(adapter);
        //在grideview点击了第几张 viewpager就显示第几页
        viewPager.setCurrentItem(currentPosition);
        //设置viewPager监听事件
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (control_state <= 2) {
                    zoomctrl.setAnimation(new AlphaAnimation(1.0f, 0f));
                    zoomctrl.show();
                }
                with = dm.widthPixels;
                height = dm.heightPixels;
                control_state = 4;

            }

            @Override
            public void onPageSelected(int position) {
//                Bitmap bitmap = BitmapFactory.decodeFile(pathlist.get(position));
//                scale = ((double) with) / (double) bitmap.getWidth();
//                height = bitmap.getHeight();
                control_state = 4;
                //改变进度背景
                isPageChanged = true;
                currentPosition = position;
                setPointCount(position);
                with = dm.widthPixels;
                height = dm.heightPixels;
                times=0;
//                zoomctrl.setIsZoomInEnabled(true);
//                zoomctrl.setIsZoomOutEnabled(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        imageView_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDialogView("确定要删除该图片？");
            }
        });
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//设置phtoview的触碰事件
//        photoview.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_MOVE:
//                        if (control_state <= 2) {
//                            zoomctrl.setAnimation(new AlphaAnimation(0.1f, 1.0f));
//                            zoomctrl.show();
//                        }
//                        zoomctrl.show();
//                        control_state = 4;
//                        break;
//                }
//                return false;
//            }
//        });
        //设置放大缩小按钮的点击事件
        zoomctrl.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (times >= 4) {
                    zoomctrl.setIsZoomInEnabled(false);
                    return;
                }
                if (times >= -4) {
                    times++;
                    zoomctrl.setIsZoomOutEnabled(true);
                }
//               webViews.get(currentPosition).zoomIn();
                with = (int) (with * 1.3);
                height = (int) (height * 1.3);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) with, height);
                webViews.get(currentPosition).setLayoutParams(layoutParams);
//                float minScale = attachers.get(currentPosition).getMinimumScale();
//                float newScale = minScale - 0.3f;
//                attachers.get(currentPosition).setScale(newScale, true);
                control_state = 4;

            }
        });

//设置放大缩小的点击事件
        zoomctrl.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (times <= -4) {
                    zoomctrl.setIsZoomOutEnabled(false);
                    return;
                }
                if (times <= 4) {
                    times--;
                    zoomctrl.setIsZoomInEnabled(true);
                }
//                webViews.get(currentPosition).zoomOut();
                with = (int) (with / 1.3);
                height = (int) (height / 1.3);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) with, height);
                webViews.get(currentPosition).setLayoutParams(layoutParams);
//                float maxScale = attachers.get(currentPosition).getMaximumScale();
//                float newScale = maxScale + 0.3f;
//                attachers.get(currentPosition).setScale(newScale, true);
                control_state = 4;

            }
        });
    }


    private void setPointCount(int currentPosition) {
        //让与选择图片数量匹配的圆点背景色为常规色
        for (int i = 0; i < pathlist.size(); i++) {
            images.get(i).setVisibility(View.VISIBLE);
            images.get(i).setBackground(getResources().getDrawable(R.drawable.circlesmall));
        }
        for (int i = pathlist.size(); i < 5; i++) {
            images.get(i).setVisibility(View.GONE);
        }
        //根据当前图片的位置将圆点背景色改为绿色
        images.get(currentPosition).setBackground(getResources().getDrawable(R.drawable.circlesmall_green));
    }

    @Override
    public void init() {
        super.init();
        zoomctrl = (ZoomControls) findViewById(R.id.zoomCtrl_picContrl);
        viewPager = (BigPhotoViewPager) findViewById(R.id.viewPager_bbsPictureShow);
        imageview_progress1 = (ImageView) findViewById(R.id.imageView_viewPagerProgress1);
        imageview_progress2 = (ImageView) findViewById(R.id.imageView_viewPagerProgress2);
        imageview_progress3 = (ImageView) findViewById(R.id.imageView_viewPagerProgress3);
        imageview_progress4 = (ImageView) findViewById(R.id.imageView_viewPagerProgress4);
        imageview_progress5 = (ImageView) findViewById(R.id.imageView_viewPagerProgress5);
        imageView_back = (ImageView) findViewById(R.id.imageView_back);
        imageView_delete = (ImageView) findViewById(R.id.imageView_deletePic);
        images = new ArrayList<>();
        images.add(imageview_progress1);
        images.add(imageview_progress2);
        images.add(imageview_progress3);
        images.add(imageview_progress4);
        images.add(imageview_progress5);
    }

    public void getImage() {
        list = new ArrayList<>();
        intent = getIntent();
        pathlist = intent.getStringArrayListExtra("path");
        String situation = "1";
        situation = intent.getStringExtra("problem");
        if (situation != null && situation.equals("problem")) {
        } else {
            //移除grideview最后一个相片选择的路径
            pathlist.remove(pathlist.size() - 1);
        }
        refreshList();
    }

    public void refreshList() {
        list.clear();
        for (String s :
                pathlist) {
            View view = getLayoutInflater().inflate(R.layout.bbs_picture_show_viewpager_item, null);
            photoview = (PhotoView) view.findViewById(R.id.photoView_bbsPictureShow_item);
            new GlideLoader().displayImage(this, s, photoview);
            webViews.add(photoview);
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(720, 1280);
//           photoview.setLayoutParams(layoutParams);
//            attacher=new PhotoViewAttacher(photoview);
//            attachers.add(attacher);

//            photoview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//            new GlideLoader().displayImage(this, s, PhotoView);
//            photoview.setImageBitmap(PictureCut.getLittleImg(BitmapFactory.decodeFile(s), 30));
            list.add(view);
        }
    }


    private void setDialogView(String message) {
        view = getLayoutInflater().inflate(R.layout.dialog_view, null);
        view.findViewById(R.id.btn_sure).setOnClickListener(this);
        view.findViewById(R.id.btn_cancel).setOnClickListener(this);
        TextView textMessage = (TextView) view.findViewById(R.id.textView_dialog_message);
        textMessage.setText(message);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sure:
                pathlist.remove(currentPosition);
                refreshList();
                List<String> newPath = new ArrayList<>();
                for (String s : pathlist) {
                    newPath.add(s);
                }
                newPath.add(image + "");
                saveImagePath(newPath);
                if (list.size() == 0) {
                    finish();
                    return;
                }
                adapter.refreshList(list);
                adapter.notifyDataSetChanged();
//                viewPager.setAdapter(adapter);
                dialog.dismiss();
                if (currentPosition >= list.size()) {
                    currentPosition = list.size() - 1;
                }
                viewPager.setCurrentItem(currentPosition);
                setPointCount(currentPosition);
                break;
            case R.id.btn_cancel:
                dialog.dismiss();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ProgressDialogUtils.showProgressDialog(PictureViewPagerActivity.this,"加载中");
                finish();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
//                        if (isPageChanged) {
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    for (WebView web : webViews) {
//                                        web.getSettings().setDisplayZoomControls(false);
//                                    }
//                                }
//                            });
//                        }
                        Thread.sleep(1000);
                        control_state--;
//                        isPageChanged = false;
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                for (WebView web : webViews) {
//                                    web.getSettings().setDisplayZoomControls(true);
//                                }
//                            }
//                        });
                        if (control_state < 2 && control_state >= 1) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    zoomctrl.setAnimation(new AlphaAnimation(1.0f, 0f));
                                    zoomctrl.hide();
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        ProgressDialogUtils.showProgressDialog(PictureViewPagerActivity.this,"加载中");
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        ProgressDialogUtils.dismissProgressDialog();
    }
}
